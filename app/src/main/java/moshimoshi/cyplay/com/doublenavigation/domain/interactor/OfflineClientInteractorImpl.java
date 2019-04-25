package moshimoshi.cyplay.com.doublenavigation.domain.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.Config;
import ninja.cyplay.com.apilibrary.domain.executor.InteractorExecutor;
import ninja.cyplay.com.apilibrary.domain.executor.MainThreadExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.AbstractInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ClientInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.PaginatedResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.requests.GetConfigRequest;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.responses.BaseResponse;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AConfig;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ADevice;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AShop;

/**
 * Created by andre on 13-Aug-18.
 */

public class OfflineClientInteractorImpl extends AbstractInteractor implements ClientInteractor {

    private IDatabaseHandler _databaseHandler;


    public OfflineClientInteractorImpl(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        super(interactorExecutor, mainThreadExecutor);
        _databaseHandler = databaseHandler;
    }

    public void getConfig(String udid, String app_version, String shopId, final ResourceRequestCallback<PR_AConfig> callback) {
        final ResourceRequestCallback<PR_AConfig> getConfigCallback = callback;

        boolean exists = false;

        try {
            JSONArray devices = _databaseHandler.getDevice(udid);
            exists = devices.length() > 0;
        }  catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        if (!exists) {
            final BaseException e = new BaseException();
            e.setType(ExceptionType.BUSINESS);
            e.setResponse(new BaseResponse("11", "11"));
            getMainThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    getConfigCallback.onError(e);
                }
            });
            return;
        }


        JSONObject result = new JSONObject();
        try {
            JSONArray configurations = _databaseHandler.getAllConfigurations();
            JSONArray shops = _databaseHandler.getAllShops();

            if (configurations.length() > 0) {
                result = configurations.getJSONObject(0);
            }
            if (shops.length() > 0) {
                result.put("shop", shops.getJSONObject(0));
            }
        } catch (JSONException e) {};

        Gson gson = new Gson();
        final Config resource = gson.fromJson(result.toString(), Config.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                getConfigCallback.onSuccess(resource);
            }
        });
    }

    public void getShops(String udid,
                  boolean all,
                  List<String> fields,
                  List<String> sortBys,
                  HashMap<String,String> filters,
                  final PaginatedResourceRequestCallback<PR_AShop> callback) {
        final PaginatedResourceRequestCallback<PR_AShop> getConfigCallback = callback;

        JSONArray result = new JSONArray();
        try {
            result = _databaseHandler.getAllShops();
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        final int count = result.length();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Shop>>(){}.getType();
        final List<PR_AShop> resource = gson.fromJson(result.toString(), type);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                getConfigCallback.onSuccess(resource, count, null, null);
            }
        });
    }

    public void provisionDevice(String udid, String shopId, final ResourceRequestCallback<PR_ADevice> callback) {
        final ResourceRequestCallback<PR_ADevice> getConfigCallback = callback;

        JSONObject result = new JSONObject();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
            String date = df.format(Calendar.getInstance().getTime());

            JSONObject device = new JSONObject();
            device.put("id", udid);
            device.put("shop_id", shopId);
            device.put("language", "en");
            device.put("date_updated", date);

            String id = _databaseHandler.addDevice(device);
            result.put("id", id);

        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        Gson gson = new Gson();
        final PR_ADevice resource = gson.fromJson(result.toString(), PR_ADevice.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                getConfigCallback.onSuccess(resource);
            }
        });
    }

    public void updateDeviceStore(String udid, String shopId, final ResourceRequestCallback<PR_ADevice> callback) {
        final ResourceRequestCallback<PR_ADevice> getConfigCallback = callback;

        JSONObject result = new JSONObject();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
            String date = df.format(Calendar.getInstance().getTime());

            JSONArray devices = _databaseHandler.getDevice(udid);
            if (devices.length() > 0) {
                JSONObject device = devices.getJSONObject(0);
                device.put("shop_id", shopId);
                String id = _databaseHandler.updateDevice(device);
                result.put("id", id);
                device.put("date_updated", date);
            }

        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        Gson gson = new Gson();
        final PR_ADevice resource = gson.fromJson(result.toString(), PR_ADevice.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                getConfigCallback.onSuccess(resource);
            }
        });
    }
}
