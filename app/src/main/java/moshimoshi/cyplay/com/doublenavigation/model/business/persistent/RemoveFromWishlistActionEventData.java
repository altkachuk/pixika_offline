package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;

import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;

/**
 * Created by romainlebouc on 10/10/16.
 */
public class RemoveFromWishlistActionEventData extends ActionEventData<RemoveFromWishlistActionEventData> {

    public final static String WISHLIST_ATTRIBUTE = "wishlist";

    public RemoveFromWishlistActionEventData(String value) {
        this.resource = EResource.CUSTOMER.getCode();
        this.attribute = WISHLIST_ATTRIBUTE;
        this.action = EAction.DELETE.getCode();
        this.value  = value;
    }
}
