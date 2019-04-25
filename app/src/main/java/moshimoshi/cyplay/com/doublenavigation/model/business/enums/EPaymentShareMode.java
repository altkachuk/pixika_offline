package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wentongwang on 29/03/2017.
 */

public enum EPaymentShareMode {

    PAYMENT_SHARE_CUSTOMER_EMAIL("customer_email"),
    PAYMENT_SHARE_SHOP_EMAIL("shop_email");

    private static Map<String, EPaymentShareMode> STATUS_2_SHARE_MODE = new HashMap<>();
    static {
        for ( EPaymentShareMode shareMode : EPaymentShareMode.values()){
            STATUS_2_SHARE_MODE.put(shareMode.getTag(), shareMode);
        }
    }
    public static EPaymentShareMode valueOfEPaymentShareMode(String tag){
        return STATUS_2_SHARE_MODE.get(tag);
    }

    private final String tag;

    EPaymentShareMode(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
