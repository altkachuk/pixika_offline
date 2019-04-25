package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;


import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EChannel;
import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventContentData;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;


/**
 * Created by romainlebouc on 23/09/16.
 */
public class ShareActionEventData extends ActionEventData<ShareActionEventData> {
    public ShareActionEventData() {
        this.action = EAction.SHARE.getCode();
    }

    public ShareActionEventData setChannel(EChannel channel){
        this.getContents().add(new ActionEventContentData("channel",
                channel.getCode()));
        return this;
    }

    public ShareActionEventData setShareIds(String key, List<String> ids){
        if ( ids !=null){
            for (String id : ids){
                this.getContents().add(new ActionEventContentData(key, id) );
            }
        }
        return this;
    }

}
