package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.Date;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AWishlistItem;

/**
 * Created by romainlebouc on 03/06/16.
 */
@Parcel
@ModelResource
public class WishlistItem extends PR_AWishlistItem implements ProductItem {

    @ReadOnlyModelField
    Date added_date;

    @ReadOnlyModelField
    Product product;

    String product_id;
    String sku_id;
    String shop_id;

    public WishlistItem() {

    }

    public WishlistItem(Product product, Sku sku, Shop shop) {
        this.setProduct(product);
        this.setShop(shop);
        this.setSku(sku);
    }


    public Product getProduct() {
        return product;
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public String getSkuId() {
        return sku_id;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.product_id = product.getId();
        }
    }

    public void setShop(Shop shop) {
        if (shop != null) {
            this.shop_id = shop.getId();
        }
    }

    public void setSku(Sku sku){
        if ( sku !=null){
            this.sku_id = sku.getId();
        }
    }
}
