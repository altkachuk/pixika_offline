package moshimoshi.cyplay.com.doublenavigation.domain.interactor;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import moshimoshi.cyplay.com.doublenavigation.model.business.WishlistItem;
import ninja.cyplay.com.apilibrary.domain.executor.InteractorExecutor;
import ninja.cyplay.com.apilibrary.domain.executor.MainThreadExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.AbstractInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.WishlistInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AWishlistItem;

/**
 * Created by andre on 20-Sep-18.
 */

public class OfflineWishlistInteractorImpl extends AbstractInteractor implements WishlistInteractor {

    private IDatabaseHandler _databaseHandler;


    public OfflineWishlistInteractorImpl(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        super(interactorExecutor, mainThreadExecutor);
        _databaseHandler = databaseHandler;
    }

    @Override
    public void addProductWishlist(String customerId, PR_AWishlistItem wishlistItem, String shopId, final ResourceRequestCallback<PR_AWishlistItem> callback) {
        JSONObject result = new JSONObject();

        try {
            Gson gson = new Gson();
            JSONObject item = new JSONObject(gson.toJson(wishlistItem));
            String id = _databaseHandler.addWishlist(customerId, item, shopId);
            result.put("id", id);

            _databaseHandler.updateCustomerUpdate(customerId, true);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        Gson gson = new Gson();
        final PR_AWishlistItem resource = gson.fromJson(result.toString(), WishlistItem.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void deleteProductWishlist(String customerId, String wishlistitemId, final ResourceRequestCallback<PR_AWishlistItem> callback) {
        JSONObject result = new JSONObject();
        try {
            _databaseHandler.deleteWishlist(wishlistitemId);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_AWishlistItem resource = gson.fromJson(result.toString(), WishlistItem.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }
}
