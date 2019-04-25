package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ECustomerOfferState;

/**
 * Created by romainlebouc on 08/06/16.
 */
@Parcel
public class CustomerOfferState implements IOffer {

    String offer_id;
    Integer status;
    String used_date;
    Offer offer;

    public String getOffer_id() {
        return offer_id;
    }

    public ECustomerOfferState getECustomerOfferState() {
        return ECustomerOfferState.getECustomerOfferState(status);
    }

    public String getUsed_date() {
        return used_date;
    }

    public Offer getOffer() {
        return offer;
    }

    @Override
    public String getBasketItemOfferId() {
        return null;
    }
}
