package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;

/**
 * Created by romainlebouc on 20/01/2017.
 */
@Parcel
public class ShopDelivery extends PurchaseCollection {

    ShopDelivery() {

    }

    public ShopDelivery(Shop shop) {

        this.type = EPurchaseCollectionMode.SHOP_DELIVERY.getCode();

        if (shop != null) {
            this.shop_id = shop.getId();
            this.address = shop.getMail();
            this.name = shop.getName(true);
        }
    }
}
