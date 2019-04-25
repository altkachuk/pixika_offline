package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPriceOfferType;

/**
 * Created by romainlebouc on 06/01/2017.
 */
@Parcel
public class ProductSpecialPriceTypeConfig {
    String type;
    String tag;

    public String getType() {
        return type;
    }

    public String getTag() {
        return tag;
    }

    public EPriceOfferType getPriceOfferType(){
        return EPriceOfferType.valueOfCode(type);
    }
}
