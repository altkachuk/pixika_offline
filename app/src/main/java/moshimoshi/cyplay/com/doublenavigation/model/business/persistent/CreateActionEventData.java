package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;

import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;

/**
 * Created by romainlebouc on 26/09/16.
 */
public class CreateActionEventData extends ActionEventData<CreateActionEventData> {

    public CreateActionEventData() {
        this.action = EAction.CREATE.getCode();
    }

}
