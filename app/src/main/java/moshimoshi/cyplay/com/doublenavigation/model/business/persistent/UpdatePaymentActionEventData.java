package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;

import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;

/**
 * Created by romainlebouc on 06/04/2017.
 */

public class UpdatePaymentActionEventData extends ActionEventData<UpdatePaymentActionEventData> {


    public final static String PAYMENT_ATTRIBUTE = "payments";

    public UpdatePaymentActionEventData(String value) {
        this.resource = EResource.CUSTOMER.getCode();
        this.attribute = PAYMENT_ATTRIBUTE;
        this.action = EAction.UPDATE.getCode();
        this.value = value;
    }

}
