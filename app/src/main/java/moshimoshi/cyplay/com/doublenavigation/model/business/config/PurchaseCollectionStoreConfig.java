package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by wentongwang on 16/03/2017.
 */
@Parcel
public class PurchaseCollectionStoreConfig {

    StoreTypeConfig shop;
    StoreTypeConfig home;

    public StoreTypeConfig getShop() {
        if ( shop == null){
            return new StoreTypeConfig();
        }
        return shop;
    }

    public StoreTypeConfig getHome() {
        if ( home == null){
            return new StoreTypeConfig();
        }
        return home;
    }
}
