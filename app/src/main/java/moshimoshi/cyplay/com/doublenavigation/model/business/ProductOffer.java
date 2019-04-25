package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferDiscountType;

/**
 * Created by wentongwang on 12/04/2017.
 */
@Parcel
public class ProductOffer {
    String offer_id;
    String discount_type;
    String discount_value;

    public String getOffer_id() {
        return offer_id;
    }

    public EOfferDiscountType getDiscount_type() {
        return EOfferDiscountType.getEOfferDiscountTypeFromCode(discount_type);
    }

    public String getDiscount_value() {
        return discount_value;
    }
}
