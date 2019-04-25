package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;

import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;

/**
 * Created by romainlebouc on 10/04/2017.
 */

public class ValidateCartActionEvent extends ActionEventData<ValidateCartActionEvent> {

    public final static String PAYMENT_ATTRIBUTE = "payments";

    public ValidateCartActionEvent() {
        this.resource = EResource.CUSTOMER.getCode();
        this.attribute = PAYMENT_ATTRIBUTE;
        this.action = EAction.CREATE.getCode();
    }

}
