package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;

/**
 * Created by romainlebouc on 05/12/2016.
 */
@Parcel
public class CustomerPaymentItem {

    String product_id;

    @ReadOnlyModelField
    Product product;
    String sku_id;
    Double unit_price;
    Integer qty;

    @ReadOnlyModelField
    Double total_discount;

    @ReadOnlyModelField
    Double item_total_amount;

    @ReadOnlyModelField
    Double total;

    public String getProduct_id() {
        return product_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public Double getTotal_discount() {
        return total_discount;
    }

    public Double getItem_total_amount() {
        return item_total_amount;
    }

    public Double getTotal() {
        return total;
    }
}
