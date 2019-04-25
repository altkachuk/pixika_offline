package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;

import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventContentData;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;

/**
 * Created by romainlebouc on 26/09/16.
 */
public class SuggestionActionEventData extends ActionEventData<SuggestionActionEventData> {

    public SuggestionActionEventData() {
        this.action = EAction.SUGGESTION.getCode();
    }

    public SuggestionActionEventData addSuggestionQueryParams(String key, String value) {
        this.getContents().add(new ActionEventContentData(key, value));
        return this;
    }

}
