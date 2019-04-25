package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;

/**
 * Created by romainlebouc on 20/01/2017.
 */
@Parcel
public class HomeDelivery extends PurchaseCollection {
    HomeDelivery() {
    }

    public HomeDelivery(Shop shop/*, Mail mail, String shippingAddressId, String name*/) {
        this.type = EPurchaseCollectionMode.HOME_DELIVERY.getCode();
        if (shop != null) {
            this.shop_id = shop.getId();
        }
        //this.shipping_address_id = shippingAddressId;
        //this.address = mail;
        //this.name = name;
    }

}
