package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;

import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;


/**
 * Created by fabienbouvet on 13/06/17.
 */
public class GetActionEventData extends ActionEventData<GetActionEventData> {

    public GetActionEventData() {
        this.action = EAction.GET.getCode();
    }

}
