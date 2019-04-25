package moshimoshi.cyplay.com.doublenavigation.domain.interactor;

import android.content.Context;
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
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSuggestion;
import ninja.cyplay.com.apilibrary.domain.executor.InteractorExecutor;
import ninja.cyplay.com.apilibrary.domain.executor.MainThreadExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.AbstractInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.FilteredPaginatedResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProduct;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductFilter;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductSuggestion;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by andre on 17-Aug-18.
 */

public class OfflineProductInteractorImpl extends AbstractInteractor implements ProductInteractor {

    private IDatabaseHandler _databaseHandler;
    private Context _context;

    public OfflineProductInteractorImpl(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler, Context context) {
        super(interactorExecutor, mainThreadExecutor);
        _databaseHandler = databaseHandler;
        _context = context;
    }

    @Override
    public void suggestProducts(String cname, int productsSuggestionLimit, final ResourceRequestCallback<PR_AProductSuggestion> callback) {
        JSONObject result = new JSONObject();

        try {
            JSONArray products = _databaseHandler.searchProducts(cname);
            JSONArray searchProducts = new JSONArray();
            for (int i = 0; i < products.length() && i < productsSuggestionLimit; i++) {
                searchProducts.put(products.getJSONObject(i));
            }
            result.put("products", searchProducts);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        Gson gson = new Gson();
        final PR_AProductSuggestion resource = gson.fromJson(result.toString(), ProductSuggestion.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void searchByName(String cname, int offset, int limit, List<String> projection, ResourceFieldSorting resourceFieldSorting, List<ResourceFilter<ResourceFilter, ResourceFilterValue>> resourceFilters, final FilteredPaginatedResourceRequestCallback<PR_AProduct, PR_AProductFilter> callback) {
        JSONArray result = new JSONArray();

        try {
            JSONArray products = _databaseHandler.searchProducts(cname);
            for (int i = 0; i < products.length() && i < limit; i++) {
                result.put(products.getJSONObject(i));
            }
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        final int count = result.length();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Product>>(){}.getType();
        final List<PR_AProduct> resource = gson.fromJson(result.toString(), type);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource, count, null, null, null);
            }
        });
    }

    @Override
    public void getProductsFromIds(List<String> rps, List<String> projection, FilteredPaginatedResourceRequestCallback<PR_AProduct, PR_AProductFilter> callback) {

    }

    @Override
    public void getProductsFromSkuIds(List<String> skuIds, List<String> projection, FilteredPaginatedResourceRequestCallback<PR_AProduct, PR_AProductFilter> callback) {

    }

    @Override
    public void getProductsForAssortment(String assortment, List<String> projection, ResourceFieldSorting resourceFieldSortings, FilteredPaginatedResourceRequestCallback<PR_AProduct, PR_AProductFilter> callback) {

    }

    @Override
    public void getProductsForBrand(String brand, int offset, int limit, List<String> projection, ResourceFieldSorting resourceFieldSortings, List<ResourceFilter<ResourceFilter, ResourceFilterValue>> resourceFilters, FilteredPaginatedResourceRequestCallback<PR_AProduct, PR_AProductFilter> callback) {

    }

    @Override
    public void getProductsForFamily(String familyId, int offset, int limit, List<String> projection, ResourceFieldSorting resourceFieldSortings, List<ResourceFilter<ResourceFilter, ResourceFilterValue>> resourceFilters, final FilteredPaginatedResourceRequestCallback<PR_AProduct, PR_AProductFilter> callback) {
        JSONArray result = new JSONArray();

        try {
            result = _databaseHandler.getProductsByFamily(familyId, projection);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        final int count = result.length();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Product>>(){}.getType();
        final List<PR_AProduct> resource = gson.fromJson(result.toString(), type);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource, count, null, null, null);
            }
        });
    }

    @Override
    public void getProductFromBarCode(String ean, ResourceRequestCallback<PR_AProduct> callback) {

    }

    @Override
    public void getProduct(String id, List<String> project, final ResourceRequestCallback<PR_AProduct> callback) {
        JSONObject result = new JSONObject();

        try {
            JSONArray products = _databaseHandler.getProduct(id);
            if (products.length() > 0)
                result = products.getJSONObject(0);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        Gson gson = new Gson();
        final PR_AProduct resource = gson.fromJson(result.toString(), Product.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void getProductSku(String productId, String skuId, List<String> project, final ResourceRequestCallback<PR_AProduct> callback) {
        JSONObject result = new JSONObject();

        try {
            JSONArray products = _databaseHandler.getProductBySku(productId, skuId);
            if (products.length() > 0)
                result = products.getJSONObject(0);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        Gson gson = new Gson();
        final PR_AProduct resource = gson.fromJson(result.toString(), Product.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }
}
