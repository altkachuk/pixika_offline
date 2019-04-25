package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;


import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventContentData;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;

/**
 * Created by romainlebouc on 22/09/16.
 */
public class LogInActionEventData extends ActionEventData<LogInActionEventData> {

    public LogInActionEventData() {
        this.action = EAction.LOGIN.getCode();

    }

    public LogInActionEventData setSeller(Seller seller) {
        this.resource = EResource.SELLER.getCode();
        this.value = seller.getId();
        this.getContents().add(new ActionEventContentData("seller_id", seller != null ? seller.getId() : null));
        return this;
    }


}