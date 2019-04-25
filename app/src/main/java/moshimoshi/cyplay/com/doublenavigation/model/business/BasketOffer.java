package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasketOffer;

/**
 * Created by romainlebouc on 08/06/16.
 */
@Parcel
public class BasketOffer extends PR_ABasketOffer {

    String offer_id;

    @ReadOnlyModelField
    Offer offer;

    @ReadOnlyModelField
    Double offer_discount;

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public Offer getOffer() {
        return offer;
    }

    public String getId() {
        return id;
    }
}
