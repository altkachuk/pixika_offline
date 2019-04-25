package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import moshimoshi.cyplay.com.doublenavigation.model.PaymentDeliveryItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_APayment;


/**
 * Created by romainlebouc on 05/12/2016.
 */
@Parcel
@ModelResource
public class Payment extends PR_APayment {

    public transient final static List<String> CUSTOMER_PAYMENT_PREVIEW_PROJECTION = Arrays.asList(
            "id",
            "creation_date",
            "status",
            "seller_id",
            "shop_id",
            "total",
            "items_total_amount",
            "delivery_fees_amount",
            "transactions");

    @ReadOnlyModelField
    String id;

    @ReadOnlyModelField
    Date updated_date;

    @ReadOnlyModelField
    Date creation_date;

    String status;

    String shop_id;
    @ReadOnlyModelField
    Shop shop;

    String seller_id;
    @ReadOnlyModelField
    Seller seller;

    @ReadOnlyModelField
    Double items_total_amount;

    @ReadOnlyModelField
    Double items_total_discount;

    @ReadOnlyModelField
    Double delivery_fees_amount;

    @ReadOnlyModelField
    Double total;

    String currency_iso4217;

    List<CustomerPaymentTransaction> transactions;
    List<PaymentDeliveryItem> delivery_items;

    public Payment() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public Date getUpdated_date() {
        return updated_date;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public String getStatus() {
        return status;
    }

    public EPaymentStatus getEPaymentStatus() {
        return EPaymentStatus.getEPaymentStatusFromCode(status);
    }

    public Double getItems_total_amount() {
        return items_total_amount;
    }

    public Double getDelivery_fees_amount() {
        return delivery_fees_amount;
    }

    public Double getItems_total_discount() {
        return items_total_discount;
    }

    public Double getTotal() {
        return total;
    }

    public String getCurrency_iso4217() {
        return currency_iso4217;
    }

    public String getShop_id() {
        return shop_id;
    }

    public List<CustomerPaymentTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<CustomerPaymentTransaction> transactions) {
        this.transactions = transactions;
    }

    public void setEPaymentStatus(EPaymentStatus ePaymentStatus) {
        if (ePaymentStatus != null) {
            this.status = ePaymentStatus.getCode();
        }
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setCurrency_iso4217(String currency_iso4217) {
        this.currency_iso4217 = currency_iso4217;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public List<PaymentDeliveryItem> getDelivery_items() {
        return delivery_items;
    }

    public void setDelivery_items(List<PaymentDeliveryItem> delivery_items) {
        this.delivery_items = delivery_items;
    }

    public int getTotalItemCount() {
        int result = 0;
        if (delivery_items != null) {
            for (PaymentDeliveryItem deliveryItem : delivery_items) {
                if (deliveryItem.getItems() != null) {
                    for (CustomerPaymentItem item : deliveryItem.getItems()) {
                        result += item.getQty();
                    }
                }
            }
        }
        return result;
    }

    public Set<EPurchaseCollectionMode> getPaymentEPurchaseCollectionModes() {
        Set<EPurchaseCollectionMode> result = new HashSet<>();
        if (this != null && this.getDelivery_items() != null) {
            for (PaymentDeliveryItem paymentDeliveryItem : this.getDelivery_items()) {
                result.add(paymentDeliveryItem.getDelivery().getEPurchaseCollectionMode());
            }
        }
        return result;
    }

    public boolean hasDeliveries() {
        Set<EPurchaseCollectionMode> ePurchaseCollectionModes = getPaymentEPurchaseCollectionModes();
        return ePurchaseCollectionModes.contains(EPurchaseCollectionMode.HOME_DELIVERY) || ePurchaseCollectionModes.contains(EPurchaseCollectionMode.SHOP_DELIVERY);
    }
}
