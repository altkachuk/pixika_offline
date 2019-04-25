package moshimoshi.cyplay.com.doublenavigation.model;

import org.parceler.Parcel;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;
import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;

/**
 * Created by romainlebouc on 14/03/2017.
 */

@Parcel
public class PaymentDeliveryItem {

    @ReadOnlyModelField
    String id;

    @ReadOnlyModelField
    List<CustomerPaymentItem> items;

    PurchaseCollection delivery;

    public List<CustomerPaymentItem> getItems() {
        return items;
    }

    public void setItems(List<CustomerPaymentItem> items) {
        this.items = items;
    }

    public PurchaseCollection getDelivery() {
        return delivery;
    }

    public void setDelivery(PurchaseCollection delivery) {
        this.delivery = delivery;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentDeliveryItem that = (PaymentDeliveryItem) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
