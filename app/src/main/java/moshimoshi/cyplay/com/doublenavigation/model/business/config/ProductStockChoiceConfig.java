package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;


/**
 * Created by romainlebouc on 25/01/2017.
 */
@Parcel
public class ProductStockChoiceConfig {

    String purchase_collection_mode;
    Integer extra;
    Integer max;
    Integer min_real_stock;

    public ProductStockChoiceConfig() {

    }

    public ProductStockChoiceConfig(EPurchaseCollectionMode ePurchaseCollectionMode) {
        this.purchase_collection_mode = ePurchaseCollectionMode != null ? ePurchaseCollectionMode.getCode() : null;
    }

    public Integer getExtra() {
        return extra != null ? extra : 0;
    }

    public Integer getMax() {
        return max != null ? max : Integer.MAX_VALUE;
    }

    public Integer getMin_real_stock() {
        return min_real_stock;
    }

    public EPurchaseCollectionMode getEPurchaseCollectionMode() {
        return EPurchaseCollectionMode.getEDeliveryDestinationTypeFromCode(this.purchase_collection_mode);
    }
}
