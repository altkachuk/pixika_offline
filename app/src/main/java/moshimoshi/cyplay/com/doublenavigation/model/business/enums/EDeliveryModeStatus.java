package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wentongwang on 24/03/2017.
 */

public enum EDeliveryModeStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private static Map<String, EDeliveryModeStatus> STATUS_2_DELIVERY_TYPE = new HashMap<>();
    static {
        for ( EDeliveryModeStatus deliveryMode : EDeliveryModeStatus.values()){
            STATUS_2_DELIVERY_TYPE.put(deliveryMode.getStatus(), deliveryMode);
        }
    }
    public static EDeliveryModeStatus valueOfEDeliveryModeForStatus(String status){
        return STATUS_2_DELIVERY_TYPE.get(status);
    }

    private final String status;

    EDeliveryModeStatus(String active) {
        this.status = active;
    }

    public String getStatus() {
        return status;
    }
}
