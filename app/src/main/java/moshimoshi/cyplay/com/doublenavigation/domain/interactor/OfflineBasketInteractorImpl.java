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
import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketComment;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import ninja.cyplay.com.apilibrary.domain.executor.InteractorExecutor;
import ninja.cyplay.com.apilibrary.domain.executor.MainThreadExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.AbstractInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.BasketInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasket;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasketComment;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasketItem;

/**
 * Created by andre on 16-Aug-18.
 */

public class OfflineBasketInteractorImpl extends AbstractInteractor implements BasketInteractor {

    private IDatabaseHandler _databaseHandler;


    public OfflineBasketInteractorImpl(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        super(interactorExecutor, mainThreadExecutor);
        _databaseHandler = databaseHandler;
    }

    @Override
    public void getSellerCart(String sellerUserName, final ResourceRequestCallback<PR_ABasket> callback) {
        JSONObject result = new JSONObject();

        try {
            JSONArray sellers = _databaseHandler.getSeller(sellerUserName);
            if (sellers.length() > 0) {
                JSONObject seller = sellers.getJSONObject(0);


                JSONArray items = _databaseHandler.getSellerBasketItemsBySeller(sellerUserName);
                JSONObject basket = _databaseHandler.createBasket(items);
                basket.put("items", items);

                result = basket;
            }
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasket resource = gson.fromJson(result.toString(), Basket.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void getCustomerCart(String customerId, final ResourceRequestCallback<PR_ABasket> callback) {
        JSONObject result = new JSONObject();

        try {
            JSONArray customers = _databaseHandler.getCustomer(customerId);
            if (customers.length() > 0) {
                JSONObject customer = customers.getJSONObject(0);


                JSONArray items = _databaseHandler.getCustomerBasketItemsByCustomer(customerId);
                JSONObject basket = _databaseHandler.createBasket(items);
                basket.put("items", items);

                result = basket;
            }
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasket resource = gson.fromJson(result.toString(), Basket.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void addBasketItemToCustomerBasket(String customerId, PR_ABasketItem basketItem, final ResourceRequestCallback<PR_ABasketItem> callback) {
        JSONObject result = new JSONObject();
        try {
            Gson gson = new Gson();
            JSONObject item = new JSONObject(gson.toJson(basketItem));
            String id = _databaseHandler.addCustomerBasketItem(customerId, item);
            result.put("id", id);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasketItem resource = gson.fromJson(result.toString(), BasketItem.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public <Item extends PR_ABasketItem> void addBasketItemsToCustomerBasket(String customerId, List<Item> basketItems, final ResourceRequestCallback<List<PR_ABasketItem>> callback) {
        JSONArray result = new JSONArray();
        try {
            for (Item basketItem : basketItems) {
                Gson gson = new Gson();
                JSONObject item = new JSONObject(gson.toJson(basketItem));
                if (item.has("id")) {
                    item.remove("id");
                }
                String id = _databaseHandler.addCustomerBasketItem(customerId, item);
                result.put(item);
            }
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<BasketItem>>(){}.getType();
        final List<PR_ABasketItem> resource = gson.fromJson(result.toString(), type);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void updateBasketItemInCustomerBasket(String customerId, PR_ABasketItem basketItem, final ResourceRequestCallback<PR_ABasketItem> callback) {
        JSONObject result = new JSONObject();
        try {
            Gson gson = new Gson();
            JSONObject item = new JSONObject(gson.toJson(basketItem));
            String id = _databaseHandler.updateCustomerBasketItem(item);
            result.put("id", id);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasketItem resource = gson.fromJson(result.toString(), BasketItem.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void removeBasketItemFromCustomerBasket(String customerId, PR_ABasketItem basketItem, final ResourceRequestCallback<PR_ABasketItem> callback) {
        JSONObject result = new JSONObject();
        try {
            _databaseHandler.deleteCustomerBasketItem(basketItem.getId());
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasketItem resource = gson.fromJson(result.toString(), BasketItem.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void removeBasketItemsFromCustomerBasket(String customerId, List<String> basketItemIds, final ResourceRequestCallback<List<PR_ABasketItem>> callback) {
        JSONArray result = new JSONArray();
        try {
            _databaseHandler.deleteCustomerBasketItems(basketItemIds);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<BasketItem>>(){}.getType();
        final List<PR_ABasketItem> resource = gson.fromJson(result.toString(), type);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void clearBasket(String customerId, final ResourceRequestCallback<PR_ABasket> callback) {
        JSONObject result = new JSONObject();
        try {
            _databaseHandler.clearCustomerBasketItemsBySeller(customerId);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasket resource = gson.fromJson(result.toString(), Basket.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void updateBasketDeliveryFees(String customerId, PR_ABasket basket, ResourceRequestCallback<PR_ABasket> callback) {

    }

    @Override
    public void validateCart(String customerId, PR_ABasketComment basketComment, final ResourceRequestCallback<Void> callback) {
        JSONObject result = new JSONObject();
        try {
            JSONObject purchase = new JSONObject();
            JSONArray customers = _databaseHandler.getCustomer(customerId);
            if (customers.length() > 0) {
                JSONObject customer = customers.getJSONObject(0);
                if (customer.has("basket"))
                    customer.remove("basket");
                purchase.put("customer", customer);
            }

            JSONArray items = _databaseHandler.getCustomerBasketItemsByCustomer(customerId);
            JSONObject basket = _databaseHandler.createBasket(items);

            purchase.put("basket", basket);
            purchase.put("comment", ((BasketComment)basketComment).getComment());
            String id = _databaseHandler.addSale(purchase);

            _databaseHandler.clearCustomerBasketItemsBySeller(customerId);

        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final Void resource = gson.fromJson(result.toString(), Void.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void addBasketItemToSellerBasket(String sellerId, PR_ABasketItem basketItem, final ResourceRequestCallback<PR_ABasketItem> callback) {
        JSONObject result = new JSONObject();
        try {
            Gson gson = new Gson();
            JSONObject item = new JSONObject(gson.toJson(basketItem));
            String id = _databaseHandler.addSellerBasketItem(sellerId, item);
            result.put("id", id);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasketItem resource = gson.fromJson(result.toString(), BasketItem.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void updateBasketItemInSellerBasket(String sellerId, PR_ABasketItem basketItem, final ResourceRequestCallback<PR_ABasketItem> callback) {
        JSONObject result = new JSONObject();
        try {
            Gson gson = new Gson();
            JSONObject item = new JSONObject(gson.toJson(basketItem));
            String id = _databaseHandler.updateSellerBasketItem(item);
            result.put("id", id);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasketItem resource = gson.fromJson(result.toString(), BasketItem.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void removeBasketItemFromSellerBasket(String sellerId, PR_ABasketItem basketItem, final ResourceRequestCallback<PR_ABasketItem> callback) {
        JSONObject result = new JSONObject();
        try {
            _databaseHandler.deleteSellerBasketItem(basketItem.getId());
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasketItem resource = gson.fromJson(result.toString(), BasketItem.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void removeBasketItemsFromSellerBasket(String customerId, List<String> basketItemIds, final ResourceRequestCallback<List<PR_ABasketItem>> callback) {
        JSONArray result = new JSONArray();
        try {
            _databaseHandler.deleteSellerBasketItems(basketItemIds);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<BasketItem>>(){}.getType();
        final List<PR_ABasketItem> resource = gson.fromJson(result.toString(), type);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void clearSellerBasket(String sellerId, final ResourceRequestCallback<PR_ABasket> callback) {
        JSONObject result = new JSONObject();
        try {
            _databaseHandler.clearSellerBasketItemsBySeller(sellerId);
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasket resource = gson.fromJson(result.toString(), Basket.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }

    @Override
    public void linkSellerBasketToCustomer(String customerId, String sellerId, List<String> project, final ResourceRequestCallback<PR_ABasket> callback) {
        JSONObject result = new JSONObject();

        try {
            JSONArray sellerItems = _databaseHandler.getSellerBasketItemsBySeller(sellerId);

            for (int i = 0; i < sellerItems.length(); i++) {
                JSONObject item = sellerItems.getJSONObject(i);
                item.remove("id");
                _databaseHandler.addCustomerBasketItem(customerId, item);
            }

            _databaseHandler.clearSellerBasketItemsBySeller(sellerId);

            JSONArray customers = _databaseHandler.getCustomer(customerId);
            if (customers.length() > 0) {
                JSONObject customer = customers.getJSONObject(0);

                JSONArray items = _databaseHandler.getCustomerBasketItemsByCustomer(customerId);
                JSONObject basket = _databaseHandler.createBasket(items);
                basket.put("items", items);

                result = basket;
            }
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        Gson gson = new Gson();
        final PR_ABasket resource = gson.fromJson(result.toString(), Basket.class);

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resource);
            }
        });
    }
}
