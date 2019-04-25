package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 05/04/2017.
 */

public enum ECustomerLoyaltyState {
    ACTIVE("active", R.string.customer_loyalty_active),
    EXPIRED("expired", R.string.customer_loyalty_expired),
    CLOSED("closed", R.string.customer_loyalty_closed);

    String code;
    int labelId;

    private static Map<String, ECustomerLoyaltyState> CODE_2_STATE = new HashMap<>();

    static {
        for (ECustomerLoyaltyState state : ECustomerLoyaltyState.values()) {
            CODE_2_STATE.put(state.code, state);
        }
    }

    ECustomerLoyaltyState(String code, int labelId) {
        this.code = code;
        this.labelId = labelId;
    }

    public static ECustomerLoyaltyState getECustomerLoyaltyState(String code) {
        return CODE_2_STATE.get(code);
    }

    public int getLabelId() {
        return labelId;
    }
}
