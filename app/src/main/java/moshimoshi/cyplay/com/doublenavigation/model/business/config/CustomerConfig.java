package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by romainlebouc on 07/09/16.
 */
@Parcel
public class CustomerConfig {

    List<RfmIndicator> rfm_indicators;
    CustomerFidelityCardConfig fidelity_card;
    CustomerWishlistConfig wishlist;
    Boolean editable = true;
    CustomerSearchConfig search;
    CustomerSalesHistoryConfig sales_history;

    public List<RfmIndicator> getRfm_indicators() {
        return rfm_indicators;
    }

    public CustomerFidelityCardConfig getFidelity_card() {
        return fidelity_card;
    }

    public CustomerWishlistConfig getWishlist() {
        return wishlist;
    }

    public Boolean getEditable() {
        return editable;
    }

    @NonNull
    public CustomerSearchConfig getSearch() {
        return search != null ? search : new CustomerSearchConfig();
    }

    @NonNull
    public CustomerSalesHistoryConfig getSales_history() {
        return sales_history != null ? sales_history : new CustomerSalesHistoryConfig();
    }
}
