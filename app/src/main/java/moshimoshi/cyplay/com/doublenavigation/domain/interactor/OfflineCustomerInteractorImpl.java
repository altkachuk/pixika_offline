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
import java.util.Map;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import atproj.cyplay.com.dblibrary.util.TimeUtil;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import ninja.cyplay.com.apilibrary.domain.executor.InteractorExecutor;
import ninja.cyplay.com.apilibrary.domain.executor.MainThreadExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.AbstractInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.PaginatedResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomer;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ATicket;

/**
 * Created by andre on 20-Aug-18.
 */

public class OfflineCustomerInteractorImpl extends AbstractInteractor implements CustomerInteractor {

    private IDatabaseHandler _databaseHandler;

    public OfflineCustomerInteractorImpl(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        super(interactorExecutor, mainThreadExecutor);
        _databaseHandler = databaseHandler;
    }

    @Override
    public void completionFromName(String cname, PaginatedResourceRequestCallback<String> callback) {

    }

    @Override
    public void searchFromName(String cname, PaginatedResourceRequestCallback<PR_ACustomer> callback) {

    }

    @Override
    public void searchFromFirstNameLastNameZipCode(String firstName, String lastName, String zipCode, PaginatedResourceRequestCallback<PR_ACustomer> callback) {

    }

    @Override
    public void search(Map<String, String> parameters, final PaginatedResourceRequestCallback<PR_ACustomer> callback) {
        JSONArray result = new JSONArray();

        try {
            String text = parameters.get("text_search");
            JSONArray customers = _databaseHandler.searchCustomers(text);
            result = customers;
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        final int count = result.length();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Customer>>(){}.getType();
        final List<PR_ACustomer> resource = gson.fromJson(result.toString(), type);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource, count, null, null);
            }
        });
    }

    @Override
    public void getUpdatedCustomers(final PaginatedResourceRequestCallback<PR_ACustomer> callback) {
        JSONArray result = new JSONArray();
        try {
            JSONArray customers = _databaseHandler.getUpdatedCustomers();
            result = customers;
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        final int count = result.length();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Customer>>(){}.getType();
        final List<PR_ACustomer> resource = gson.fromJson(result.toString(), type);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource, count, null, null);
            }
        });
    }

    @Override
    public void getCustomerInfo(String id, List<String> projection, final ResourceRequestCallback<PR_ACustomer> callback) {
        JSONObject result = new JSONObject();

        try {
            JSONArray customers = _databaseHandler.getCustomer(id);
            if (customers.length() > 0)
                result = customers.getJSONObject(0);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        Gson gson = new Gson();
        final PR_ACustomer resource = gson.fromJson(result.toString(), Customer.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void getCustomerInfoFromEAN(String ean, ResourceRequestCallback<PR_ACustomer> callback) {

    }

    @Override
    public void updateCustomerInfo(String id, PR_ACustomer customer, final ResourceRequestCallback<PR_ACustomer> callback) {
        JSONObject result = new JSONObject();
        try {
            Gson gson = new Gson();
            JSONObject item = new JSONObject(gson.toJson(customer));
            item.put("id", id);
            id = _databaseHandler.updateCustomer(item);
            result = item;
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ACustomer resource = gson.fromJson(result.toString(), Customer.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void createCustomerInfo(PR_ACustomer customer, final ResourceRequestCallback<PR_ACustomer> callback) {
        JSONObject result = new JSONObject();
        try {
            Gson gson = new Gson();
            JSONObject item = new JSONObject(gson.toJson(customer));
            String id = TimeUtil.generateUid();
            item.put("id", id);
            item.put("source", "app");
            id = _databaseHandler.addCustomer(item, true);
            result.put("id", id);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ACustomer resource = gson.fromJson(result.toString(), Customer.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void getCustomerSalesHistory(String customerId, PaginatedResourceRequestCallback<PR_ATicket> callback) {

    }
}
