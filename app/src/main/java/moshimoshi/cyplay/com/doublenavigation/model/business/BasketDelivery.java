package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;

/**
 * Created by romainlebouc on 26/04/2017.
 */
@Parcel
public class BasketDelivery {

    String type;
    String delivery_mode_id;
    String shipping_address_id;

    public EPurchaseCollectionMode getType() {
        return EPurchaseCollectionMode.getEDeliveryDestinationTypeFromCode(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDelivery_mode_id() {
        return delivery_mode_id;
    }

    public void setDelivery_mode_id(String delivery_mode_id) {
        this.delivery_mode_id = delivery_mode_id;
    }

    public String getShipping_address_id() {
        return shipping_address_id;
    }

    public void setShipping_address_id(String shipping_address_id) {
        this.shipping_address_id = shipping_address_id;
    }
}
