package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasket;


/**
 * Created by romainlebouc on 08/06/16.
 */
@Parcel
@ModelResource
public class Basket extends PR_ABasket {
    public transient final static List<String> BASKET_PROJECTION = Arrays.asList(
            "id",
            "items",
            "basket_offers",
            "creation_date",
            "update_date",
            "total",
            "total_discount",
            "items_total_amount",
            "delivery_fees_amount",
            "delivery_items");

    @ReadOnlyModelField
    List<BasketItem> items;
    @ReadOnlyModelField
    List<BasketOffer> basket_offers;
    @ReadOnlyModelField
    String creation_date;
    @ReadOnlyModelField
    String update_date;
    @ReadOnlyModelField
    Double total;
    @ReadOnlyModelField
    Double total_discount;
    @ReadOnlyModelField
    Double items_total_amount;

    @ReadOnlyModelField
    Double delivery_fees_amount;

    List<BasketDeliveryItem> delivery_items;

    public Basket() {
        this.items = new ArrayList<>();
        this.basket_offers = new ArrayList<>();
    }

    public List<BasketOffer> getBasket_offers() {
        return basket_offers;
    }

    public void setBasket_offers(List<BasketOffer> basket_offers) {
        this.basket_offers = basket_offers;
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public Double getTotal() {
        return total;
    }

    public void setItems(List<BasketItem> items) {
        this.items = items;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotal_discount() {
        return total_discount;
    }

    public Double getItems_total_amount() {
        return items_total_amount;
    }

    public Double getDelivery_fees_amount() {
        return delivery_fees_amount;
    }

    public void setDelivery_items(List<BasketDeliveryItem> delivery_items) {
        this.delivery_items = delivery_items;
    }

    public int getBasketItemsCount() {
        int result = 0;
        if (items != null) {
            for (BasketItem basketItem : items) {
                result += basketItem.getQty();
            }
        }
        return result;
    }

    public BasketItem getBasketItem(Product product, Sku sku) {
        BasketItem result = null;

        for (BasketItem basketItem : getItems()) {
            if (product.getId().equals(basketItem.getProduct_id()) && sku.getId().equals(basketItem.getSku_id())) {
                result = basketItem;
            }
        }
        return result;
    }

    public BasketItem getBasketItem(String basketItemId) {
        BasketItem result = null;
        if (items != null && basketItemId != null) {
            for (BasketItem basketItem : items) {
                if (basketItemId.equals(basketItem.getId())) {
                    result = basketItem;
                    break;
                }
            }

        }
        return result;
    }


    public Set<EPurchaseCollectionMode> getBasketEPurchaseCollectionModes() {
        Set<EPurchaseCollectionMode> result = new HashSet<>();
        if (this != null && this.getItems() != null) {
            for (BasketItem basketItem : this.getItems()) {
                result.add(basketItem.getPurchaseCollection().getEPurchaseCollectionMode());
            }
        }
        return result;
    }

    public boolean hasDeliveries() {
        Set<EPurchaseCollectionMode> ePurchaseCollectionModes = getBasketEPurchaseCollectionModes();
        return ePurchaseCollectionModes.contains(EPurchaseCollectionMode.HOME_DELIVERY) || ePurchaseCollectionModes.contains(EPurchaseCollectionMode.SHOP_DELIVERY);
    }

    public boolean hasHomeDelivery() {
        Set<EPurchaseCollectionMode> ePurchaseCollectionModes = getBasketEPurchaseCollectionModes();
        return ePurchaseCollectionModes.contains(EPurchaseCollectionMode.HOME_DELIVERY);
    }

    public String getBasketItemId(Offer offer) {
        String id = offer.getId();
        for (BasketOffer basketOffer : this.getBasket_offers()) {
            if (id.equals(basketOffer.getOffer_id())) {
                return basketOffer.getId();
            }
        }
        return null;
    }

}
