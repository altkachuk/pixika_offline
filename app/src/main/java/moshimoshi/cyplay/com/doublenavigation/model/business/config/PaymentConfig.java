package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 08/12/2016.
 */
@Parcel
public class PaymentConfig {

    Boolean customer_basket;
    Boolean seller_basket;

    PurchaseCollectionConfig purchase_collection;
    PurchaseCollectionStoreConfig purchase_collection_to_store;

    PaymentDisplayConfig display;

    public Boolean getCustomer_basket() {
        return customer_basket;
    }

    public Boolean getSeller_basket() {
        return seller_basket;
    }


    public PurchaseCollectionConfig getPurchase_collection() {
        if (purchase_collection == null) {
            purchase_collection =  new PurchaseCollectionConfig();
        }
        return purchase_collection;
    }

    public PurchaseCollectionStoreConfig getPurchase_collection_to_store() {
        if (purchase_collection_to_store == null) {
            purchase_collection_to_store = new PurchaseCollectionStoreConfig();
        }
        return purchase_collection_to_store;
    }

    public PaymentDisplayConfig getDisplay() {
        if (display==null ){
            display= new PaymentDisplayConfig();
        }
        return display;
    }

}
