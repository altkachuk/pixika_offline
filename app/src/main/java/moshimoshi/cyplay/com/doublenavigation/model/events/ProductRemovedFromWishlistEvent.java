package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;

/**
 * Created by romainlebouc on 05/08/16.
 */
public class ProductRemovedFromWishlistEvent {

    private final Product product;

    public ProductRemovedFromWishlistEvent(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
