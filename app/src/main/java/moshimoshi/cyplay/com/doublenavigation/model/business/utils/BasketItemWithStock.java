package moshimoshi.cyplay.com.doublenavigation.model.business.utils;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;

/**
 * Created by romainlebouc on 26/01/2017.
 */

@Parcel
public class BasketItemWithStock {
    BasketItem basketItem;
    ThreeSixtyStocks threeSixtyStocks;

    BasketItemWithStock() {

    }

    public BasketItemWithStock(BasketItem basketItem) {
        this.basketItem = basketItem;
    }

    public void setThreeSixtyStocks(ThreeSixtyStocks threeSixtyStocks) {
        this.threeSixtyStocks = threeSixtyStocks;
    }

    public BasketItem getBasketItem() {
        return basketItem;
    }

    public ThreeSixtyStocks getThreeSixtyStocks() {
        return threeSixtyStocks;
    }
}