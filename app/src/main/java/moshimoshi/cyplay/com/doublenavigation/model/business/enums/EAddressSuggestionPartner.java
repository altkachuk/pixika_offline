package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

/**
 * Created by wentongwang on 05/06/2017.
 */

public enum EAddressSuggestionPartner {
    DQE("DQE"),
    GOOGLE_MAP("GOOGLE_MAP");

    private final String code;
    private final static HashMap<String, EAddressSuggestionPartner> CODE_TO_EPARTNER = new HashMap<>();

    static {
        for (EAddressSuggestionPartner ePartner : EAddressSuggestionPartner.values()) {
            CODE_TO_EPARTNER.put(ePartner.code, ePartner);
        }
    }

    EAddressSuggestionPartner(String code) {
        this.code = code;
    }

    public static EAddressSuggestionPartner getEPartnerFromCode(String code) {
        return CODE_TO_EPARTNER.get(code);
    }


    public String getCode() {
        return code;
    }
}
