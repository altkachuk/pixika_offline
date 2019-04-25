package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;

/**
 * Created by romainlebouc on 16/05/16.
 */
public class ProductLoadedEvent {

    private final Product product;

    public ProductLoadedEvent(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
