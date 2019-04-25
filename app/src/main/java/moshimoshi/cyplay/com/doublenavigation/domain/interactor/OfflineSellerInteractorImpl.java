package moshimoshi.cyplay.com.doublenavigation.domain.interactor;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import moshimoshi.cyplay.com.doublenavigation.model.business.DeviceConfiguration;
import moshimoshi.cyplay.com.doublenavigation.utils.PBKDF2Hash;
import ninja.cyplay.com.apilibrary.domain.executor.InteractorExecutor;
import ninja.cyplay.com.apilibrary.domain.executor.MainThreadExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.AbstractInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.Interactor;
import ninja.cyplay.com.apilibrary.domain.interactor.SellerInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.responses.BaseResponse;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ADeviceConfiguration;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AUser;
import ninja.cyplay.com.apilibrary.models.business.OAuth2Credentials;

/**
 * Created by andre on 15-Aug-18.
 */

public class OfflineSellerInteractorImpl extends AbstractInteractor implements SellerInteractor {

    private IDatabaseHandler _databaseHandler;


    public OfflineSellerInteractorImpl(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        super(interactorExecutor, mainThreadExecutor);
        _databaseHandler = databaseHandler;
    }

    @Override
    public void accessToken(final String username, final String password, String client_id, String client_secret, final ResourceRequestCallback<OAuth2Credentials> callback) {
        getInteractorExecutor().run(new Interactor() {
            @Override
            public void run() {
                JSONObject result = new JSONObject();

                final BaseException exception = new BaseException();
                try {
                    JSONArray sellers = _databaseHandler.getSeller(username);

                    boolean isValid = false;
                    if (sellers.length() > 0) {
                        String djangoHashPassword = sellers.getJSONObject(0).getString("password");
                        PBKDF2Hash pbkdf2Hash = new PBKDF2Hash(djangoHashPassword);
                        isValid = pbkdf2Hash.checkString(password);
                    }

                    if (isValid) {
                        result = new JSONObject("{'access_token': 'WDLqWwNTIDdbHLcV44oAFR0UpULYXq', 'token_type': 'Bearer', 'expires_in': 36000, 'refresh_token': 'xTOm7Ox9Hlkk3Jr7AjQJARgI3O2DT4', 'scope': 'seller'}");
                        String shopId = sellers.getJSONObject(0).getJSONObject("basket").getString("shop_id");
                        _databaseHandler.setCurrentShop(shopId);
                    } else {
                        BaseResponse response = new BaseResponse("invalid_grant", "Invalid credentials given.");
                        exception.setType(ExceptionType.BUSINESS);
                        exception.setResponse(response);
                    }
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                }

                Gson gson = new Gson();
                final OAuth2Credentials resource = gson.fromJson(result.toString(), OAuth2Credentials.class);

                getMainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (exception.getType() != ExceptionType.BUSINESS)
                            callback.onSuccess(resource);
                        else
                            callback.onError(exception);
                    }
                });

            }
        });
    }

    @Override
    public void invalidateToken(String client_id, String client_secret, String token, final ResourceRequestCallback<Void> callback) {
        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void getDeviceConfiguration(String udid, ResourceRequestCallback<PR_ADeviceConfiguration> callback) {
        final ResourceRequestCallback<PR_ADeviceConfiguration> fCallback = callback;
        JSONObject result = new JSONObject();

        try {
            JSONArray devices = _databaseHandler.getDevice(udid);
            if (devices.length() > 0) {
                JSONObject device = devices.getJSONObject(0);
                String shopId = device.getString("shop_id");
                result.put("shop_id", shopId);
                JSONArray sellers = _databaseHandler.getSellersByShop(shopId);
                result.put("sellers", sellers);
            }
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };


        Gson gson = new Gson();
        final DeviceConfiguration resource = gson.fromJson(result.toString(), DeviceConfiguration.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                fCallback.onSuccess(resource);
            }
        });
    }

    @Override
    public void replaceUserPassword(String userid, String oldPassword, String newPassword, ResourceRequestCallback<PR_AUser> callback) {
        final ResourceRequestCallback<PR_AUser> fCallback = callback;

        JSONObject result = new JSONObject();
        try {
            JSONArray sellers = _databaseHandler.getSeller(userid);
            if (sellers.length() > 0) {
                JSONObject seller = sellers.getJSONObject(0);
                seller.put("password", newPassword);
                String id = _databaseHandler.updateSeller(seller);
                result.put("id", id);
            }

        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        Gson gson = new Gson();
        final PR_AUser resource = gson.fromJson(result.toString(), PR_AUser.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                fCallback.onSuccess(resource);
            }
        });
    }

    @Override
    public void notifyLostUserPassword(String userid, ResourceRequestCallback<PR_AUser> callback) {
        final ResourceRequestCallback<PR_AUser> fCallback = callback;

        JSONObject result = new JSONObject();

        Gson gson = new Gson();
        final PR_AUser resource = gson.fromJson(result.toString(), PR_AUser.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                fCallback.onSuccess(resource);
            }
        });
    }
}
