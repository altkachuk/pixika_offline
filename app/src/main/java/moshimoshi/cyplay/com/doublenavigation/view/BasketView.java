package moshimoshi.cyplay.com.doublenavigation.view;

import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;


/**
 * Created by romainlebouc on 24/01/2017.
 */

public interface BasketView extends ResourceView<Basket> {
    void onStockResponse(final BasketItem basketItem,ResourceResponseEvent<Product> resourceResponseEvent);
    void onBasketItemsStocksChecked(boolean success);
    void onValidateCartResponse();
}
