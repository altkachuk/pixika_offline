package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;

import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;

/**
 * Created by romainlebouc on 06/04/2017.
 */

public class AddToBasketActionEventData extends ActionEventData<AddToBasketActionEventData> {

    public final static String BASKET_ATTRIBUTE = "basket";
    public final static String ITEM_SUB_ATTRIBUTE = "items";

    public AddToBasketActionEventData(String value) {
        this.resource = EResource.CUSTOMER.getCode();
        this.attribute = BASKET_ATTRIBUTE;
        this.sub_attribute = ITEM_SUB_ATTRIBUTE;
        this.action = EAction.CREATE.getCode();
        this.value = value;
    }

}
