package moshimoshi.cyplay.com.doublenavigation.model.business;

import java.io.Serializable;

public class CartItem implements Serializable {

    public Product product;
    public Sku sku; // size

    public CartItem(Product product, Sku sku) {
        this.product = product;
        this.sku = sku;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof CartItem)) return false;
        CartItem otherItem = (CartItem) other;
        if (this.product == null) return false;
        if (otherItem.product == null) return false;
        boolean ret = this.product.equals(otherItem.product);
        if (ret)
            ret = this.sku.equals(otherItem.sku);
        return ret;
    }

}
