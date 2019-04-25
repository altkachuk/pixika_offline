package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by romainlebouc on 07/04/2017.
 */

public enum ECustomerOfferState {

    AVAILABLE(0), IN_BASKET(1), USED(2);


    private final int code;

    private final static Map<Integer, ECustomerOfferState> CODE_2_STATE = new HashMap<>();

    static {
        for (ECustomerOfferState state : ECustomerOfferState.values()) {
            CODE_2_STATE.put(state.code, state);
        }
    }

    ECustomerOfferState(int code) {
        this.code = code;
    }

    public static ECustomerOfferState getECustomerOfferState(Integer code) {
        return CODE_2_STATE.get(code);
    }

}
