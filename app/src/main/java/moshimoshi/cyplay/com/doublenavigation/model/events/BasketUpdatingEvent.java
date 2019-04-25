package moshimoshi.cyplay.com.doublenavigation.model.events;


import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;

/**
 * Created by romainlebouc on 22/11/16.
 */
public class BasketUpdatingEvent {

    private final BasketItem basketItem;

    public BasketUpdatingEvent(BasketItem basketItem) {
        this.basketItem = basketItem;
    }

    public BasketItem getBasketItem() {
        return basketItem;
    }
}
