package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferDiscountType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferType;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AOffer;

@Parcel
@ModelResource
public class Offer extends PR_AOffer implements ShopsItem, IOffer {


    public final static List<String> OFFER_PROJECTION = Arrays.asList(
            "name",
            "from_date",
            "to_date",
            "shop_ids",
            "shops",
            "type",
            "description",
            "discount_type",
            "discount_value");

    String name;
    Date from_date;
    Date to_date;
    List<String> shop_ids;
    List<Shop> shops;
    String type;
    String description;
    String discount_type;
    Double discount_value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFrom_date() {
        return from_date;
    }

    public Date getTo_date() {
        return to_date;
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    public EOfferType getType() {
        return EOfferType.getEOfferTypeFromCode(this.type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public EOfferDiscountType getDiscountType() {
        return EOfferDiscountType.getEOfferDiscountTypeFromCode(discount_type);
    }

    public Double getDiscount_value() {
        return discount_value;
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    public static int getOfferCountByType(List<IOffer> offerList, EOfferType type) {
        int count = 0;
        if (offerList != null && type != null) {
            for (int i = 0; i < offerList.size(); i++)
                if (offerList.get(i) != null && type.equals(offerList.get(i).getOffer().getType())) {
                    count++;
                }
        }
        return count;
    }

    @Override
    public Offer getOffer() {
        return this;
    }

    @Override
    public String getBasketItemOfferId() {
        return null;
    }

}