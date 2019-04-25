package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by romainlebouc on 06/01/2017.
 */

public enum EPriceOfferType {
    NORMAL("N"), DISCOUNT("P");
    String code;

    private final static Map<String, EPriceOfferType> CODE_2_EPRICEOFFER = new HashMap<>();

    static {
        for ( EPriceOfferType ePriceOfferType : EPriceOfferType.values()){
            CODE_2_EPRICEOFFER.put(ePriceOfferType.getCode(), ePriceOfferType);
        }
    }
    public String getCode() {
        return code;
    }

    EPriceOfferType(String code) {
        this.code = code;
    }

    public static EPriceOfferType valueOfCode(String code) {
        return CODE_2_EPRICEOFFER.get(code);
    }
}
