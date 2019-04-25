package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;

import java.util.Map;

import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventContentData;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;


/**
 * Created by romainlebouc on 23/09/16.
 */
public class SearchActionEventData extends ActionEventData<SearchActionEventData> {

    public SearchActionEventData() {
        this.action = EAction.SEARCH.getCode();
    }

    public SearchActionEventData addSearchQueryParams(String key, String value) {
        this.getContents().add(new ActionEventContentData(key, value));
        return this;
    }

    public SearchActionEventData addSearchQueryParams(Map<String, String> values){
        for (Map.Entry<String,String> entry: values.entrySet()){
            this.getContents().add(new ActionEventContentData(entry.getKey(), entry.getValue()));
        }
        return this;
    }

}
