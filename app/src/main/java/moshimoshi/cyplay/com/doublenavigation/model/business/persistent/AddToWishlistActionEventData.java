package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;

import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;


/**
 * Created by romainlebouc on 10/10/16.
 */
public class AddToWishlistActionEventData extends ActionEventData<AddToWishlistActionEventData> {
    public final static String WISHLIST_ATTRIBUTE = "wishlist";

    public AddToWishlistActionEventData(String value) {
        this.resource = EResource.CUSTOMER.getCode();
        this.attribute = WISHLIST_ATTRIBUTE;
        this.action = EAction.PUT.getCode();
        this.value=value;
    }
}
