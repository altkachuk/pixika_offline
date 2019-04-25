package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferDiscountType;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasketItem;

/**
 * Created by romainlebouc on 08/06/16.
 */
@Parcel
@ModelResource
public class BasketItem extends PR_ABasketItem implements Cloneable{

    String product_id;
    String sku_id;

    Integer qty;

    @ReadOnlyModelField
    Double unit_price;

    @ReadOnlyModelField
    Product product;

    PurchaseCollection delivery;

    @ReadOnlyModelField
    List<BasketOffer> item_offers;

    @ReadOnlyModelField
    Double total;

    @ReadOnlyModelField
    Double total_discount;

    @ReadOnlyModelField
    Double item_total_amount;


    BasketItem() {
    }

    public BasketItem(Product product, Sku sku, Shop shop, PurchaseCollection purchaseCollection) {
        this.product = product;
        if (product != null) {
            product_id = product.getId();
        }

        if (sku != null) {
            sku_id = sku.getId();
        }

        this.delivery = purchaseCollection;
        if (shop != null) {
            delivery.setShop_id(shop.getId());
            if (sku != null) {
                Ska ska = sku.getSkaForShop(shop.getId());
                this.unit_price = ska.getPrice();
            }
        }

    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PurchaseCollection getPurchaseCollection() {
        return delivery;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setTotal_discount(Double total_discount) {
        this.total_discount = total_discount;
    }

    public void setItem_total_amount(Double item_total_amount) {
        this.item_total_amount = item_total_amount;
    }

    public void setPurchaseCollection(PurchaseCollection purchaseCollection) {
        this.delivery = purchaseCollection;
    }

    public List<BasketOffer> getItem_offers() {
        if (this.item_offers == null) {
            this.item_offers = new ArrayList<>();
        }
        return this.item_offers;
    }

    public BasketOffer getOfferByDiscountType(EOfferDiscountType type){
        if (this.item_offers == null) {
            return null;
        }
        for (BasketOffer offer : this.item_offers) {
            if (type.equals(offer.getOffer().getDiscountType())) {
                return offer;
            }
        }
        return null;
    }

    public Double getItem_total_amount() {
        return item_total_amount;
    }

    public Double getTotal_discount() {
        return total_discount;
    }

    public Double getPriceTotal() {
        return total != null ? total : unit_price;
    }

    public Ska getSka() {
        Ska result = null;
        if (getProduct() != null && getSku_id() != null) {
            Sku sku = getProduct().getSku(getSku_id());
            if (sku != null && getPurchaseCollection().getShop_id() != null) {
                result = sku.getSkaForShop(delivery.getShop_id());
            }
        }
        return result;
    }

    public Sku getSku() {
        Sku result = null;
        if (getProduct() != null && getSku_id() != null) {
            result = getProduct().getSku(getSku_id());
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasketItem that = (BasketItem) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
