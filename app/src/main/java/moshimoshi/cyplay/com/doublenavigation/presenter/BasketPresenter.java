package moshimoshi.cyplay.com.doublenavigation.presenter;


import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketComment;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.BasketItemWithStock;
import moshimoshi.cyplay.com.doublenavigation.view.BasketView;
import moshimoshi.cyplay.com.doublenavigation.view.RemoveBasketItemsView;

/**
 * Created by damien on 27/05/15.
 */
public interface BasketPresenter extends Presenter<BasketView> {

    // Get basket from server
    void getBasket();

    void getSellerBasket();

    Basket getCachedBasket();

    boolean isCustomerBasket();

    // ITEMS IN CART
    void addUpdateOrDeleteBasketItem(BasketItem basketItem);

    void removeBasketItem(BasketItem basketItem);

    void getBasketItemStock(BasketItem basketItem);

    void checkBasketStock(List<BasketItemWithStock> basketItemWithStockList );

    void setRemoveBasketItemsView(RemoveBasketItemsView removeBasketItemsView);

    void initBasketItemToRemove(List<BasketItem> basketItems);

    void removeBasketItems();

    void clearBasket();

    void linkSellerBasketToCustomer(String customerId);

    void updateDeliveryFees(Basket basket);

    void validateCart(String customerId, BasketComment basket);

}