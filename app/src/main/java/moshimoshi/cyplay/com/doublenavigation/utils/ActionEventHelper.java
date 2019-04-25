package moshimoshi.cyplay.com.doublenavigation.utils;

import android.content.Context;
import android.util.Log;

import java.util.Date;

import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;
import ninja.cyplay.com.apilibrary.utils.LogUtils;
import ninja.cyplay.com.apilibrary.utils.RealmHelper;

/**
 * Created by romainlebouc on 22/09/16.
 */
public class ActionEventHelper {


    private final ConfigHelper configHelper;
    private final SellerContext sellerContext;
    private final CustomerContext customerContext;
    private final Context context;

    public ActionEventHelper(Context context, SellerContext sellerContext, CustomerContext customerContext, ConfigHelper configHelper) {
        ((PlayRetailApp) context.getApplicationContext()).inject(this);
        this.sellerContext = sellerContext;
        this.customerContext = customerContext;
        this.context = context;
        this.configHelper = configHelper;
    }

    public void log(ActionEventData actionEventData) {
        try {
            actionEventData.setSeller_id(sellerContext.getSeller() != null ? sellerContext.getSeller().getId() : null);
            actionEventData.setShop_id(configHelper.getCurrentShop() != null ?
                    configHelper.getCurrentShop().getId() : null);
            actionEventData.setCustomer_id(customerContext.getCustomer() != null ? customerContext.getCustomer().getId() : null);
            actionEventData.setDate(new Date());
            RealmHelper.saveObjectToRealm(context, actionEventData.build());
        } catch (Exception e) {
            Log.e(LogUtils.generateTag(this), e.toString());
        }
    }

}
