package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 28/11/2016.
 */

public enum EPurchaseCollectionMode {
    SHOP_NOW("shop_now", R.string.delivery_mode_shop_delivery),
    SHOP_DELIVERY("shop_delivery", R.string.delivery_mode_shop_collect),
    HOME_DELIVERY("home_delivery", R.string.delivery_mode_delivery);

    private final String code;
    private final int labelId;

    private final static Map<String, EPurchaseCollectionMode> CODE_2_DELIVERYDESTINATIONTYPE = new HashMap<>();

    static {
        for (EPurchaseCollectionMode ePurchaseCollectionMode : EPurchaseCollectionMode.values()) {
            CODE_2_DELIVERYDESTINATIONTYPE.put(ePurchaseCollectionMode.getCode(), ePurchaseCollectionMode);
        }
    }

    EPurchaseCollectionMode(String code, int labelId) {
        this.code = code;
        this.labelId = labelId;
    }

    public String getCode() {
        return code;
    }

    public static EPurchaseCollectionMode getEDeliveryDestinationTypeFromCode(String code) {
        return CODE_2_DELIVERYDESTINATIONTYPE.get(code);
    }

    public int getLabelId() {
        return labelId;
    }
}
