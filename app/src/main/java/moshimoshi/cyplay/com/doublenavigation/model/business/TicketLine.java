package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by romainlebouc on 13/05/16.
 */
@Parcel
public class TicketLine implements ProductItem {

    Product product;
    String sku_id;
    Double unit_price;
    Double qty;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public String getSkuId() {
        return sku_id;
    }
}
