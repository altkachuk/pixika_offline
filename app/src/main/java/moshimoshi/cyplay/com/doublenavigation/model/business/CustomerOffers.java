package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomerOffers;

@Parcel
public class CustomerOffers extends PR_ACustomerOffers {

    List<CustomerOfferState> items;

    public List<CustomerOfferState> getItems() {
        if (items == null)
            items = new ArrayList<>();
        return items;
    }

}

