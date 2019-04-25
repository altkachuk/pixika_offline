package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 26/04/2017.
 */
@Parcel
public class BasketDeliveryItem {

    BasketDelivery delivery;

    public BasketDeliveryItem() {
        if (delivery == null) {
            this.delivery = new BasketDelivery();
        }
    }

    public BasketDelivery getDelivery() {
        return delivery;
    }
}
