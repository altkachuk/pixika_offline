package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;

/**
 * Created by romainlebouc on 29/04/2017.
 */

public class SellerLoadedEvent extends LimitedConsumptionEvent {

    private final Seller seller;

    public SellerLoadedEvent(Seller seller) {
        super(1);
        this.seller = seller;
    }

    public Seller getSeller() {
        return seller;
    }
}
