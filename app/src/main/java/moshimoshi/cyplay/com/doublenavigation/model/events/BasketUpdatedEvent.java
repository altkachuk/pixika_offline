package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;

/**
 * Created by romainlebouc on 13/12/2016.
 */

public class BasketUpdatedEvent {


    public enum EBasketPersonType {
        SELLER, CUSTOMER
    }

    private final Basket basket;
    private EBasketPersonType eBasketPersonType;

    public BasketUpdatedEvent(Basket basket, EBasketPersonType eBasketPersonType) {
        this.basket = basket;
        this.eBasketPersonType = eBasketPersonType;
    }

    public Basket getBasket() {
        return basket;
    }

    public EBasketPersonType geteBasketPersonType() {
        return eBasketPersonType;
    }
}
