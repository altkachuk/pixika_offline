package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by romainlebouc on 21/09/16.
 */
@Parcel
public class CustomerWishlistConfig {

    List<CustomerWishlistSharingMode> sharing_modes;

    public List<CustomerWishlistSharingMode> getSharing_modes() {
        return sharing_modes;
    }
}
