package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;

/**
 * Created by damien on 28/04/16.
 */
public class ProductSelectedSkuChangeEvent {

    private final Sku sku;

    public ProductSelectedSkuChangeEvent(Sku sku) {
        this.sku = sku;
    }

    public Sku getSku() {
        return sku;
    }

}
