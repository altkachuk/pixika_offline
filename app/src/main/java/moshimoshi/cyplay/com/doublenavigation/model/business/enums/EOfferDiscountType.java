package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

/**
 * Created by wentongwang on 12/04/2017.
 */

public enum EOfferDiscountType {
    PERCENT("percent"),
    AMOUNT("amount"),
    UNIT("unit");

    private String code;

    private final static HashMap<String, EOfferDiscountType> CODE_TO_EOfferDiscountType = new HashMap<>();

    static {
        for (EOfferDiscountType eOfferDiscountType : EOfferDiscountType.values()) {
            CODE_TO_EOfferDiscountType.put(eOfferDiscountType.code, eOfferDiscountType);
        }
    }

    EOfferDiscountType(String code) {
        this.code = code;
    }

    public static EOfferDiscountType getEOfferDiscountTypeFromCode(String code) {
        return CODE_TO_EOfferDiscountType.get(code);
    }


    public String getCode() {
        return code;
    }

}
