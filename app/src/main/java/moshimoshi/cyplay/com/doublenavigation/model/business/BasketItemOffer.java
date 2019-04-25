package moshimoshi.cyplay.com.doublenavigation.model.business;


import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EBasketItemOfferStatus;

/**
 * Created by wentongwang on 06/04/2017.
 */
@Parcel
public class BasketItemOffer  implements IOffer{

    Offer offer;
    String basketItemOfferId;
    EBasketItemOfferStatus status;

    public BasketItemOffer() {
    }

    public BasketItemOffer(Offer offer, String basketItemOfferId, EBasketItemOfferStatus status) {
        this.offer = offer;
        this.basketItemOfferId = basketItemOfferId;
        this.status = status;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public String getBasketItemOfferId() {
        return basketItemOfferId;
    }

    public void setBasketItemOfferId(String basketItemOfferId) {
        this.basketItemOfferId = basketItemOfferId;
    }

    public EBasketItemOfferStatus getStatus() {
        return status;
    }
}
