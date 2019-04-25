package moshimoshi.cyplay.com.doublenavigation.domain.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import ninja.cyplay.com.apilibrary.domain.executor.InteractorExecutor;
import ninja.cyplay.com.apilibrary.domain.executor.MainThreadExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.AbstractInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.SaleInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.PaginatedResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_Sale;

/**
 * Created by andre on 28-Feb-19.
 */

public class OfflineSaleInteractorImpl extends AbstractInteractor implements SaleInteractor {

    private IDatabaseHandler databaseHandler;


    public OfflineSaleInteractorImpl(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        super(interactorExecutor, mainThreadExecutor);
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void getAllSales(final PaginatedResourceRequestCallback<PR_Sale> callback) {
        JSONArray result = new JSONArray();

        try {
            JSONArray sales = databaseHandler.getAllSales();
            result = sales;
        } catch (JSONException e) {
            Log.d("JSONException Error", e.getMessage());
        }

        final int count = result.length();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Sale>>(){}.getType();
        final List<PR_Sale> resource = gson.fromJson(result.toString(), type);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource, count, null, null);
            }
        });
    }

    public void removeSale(String id, final ResourceRequestCallback<PR_Sale> callback) {
        JSONObject result = new JSONObject();
        try {
            databaseHandler.deleteSale(id);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_Sale resource = gson.fromJson(result.toString(), Sale.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }
}
