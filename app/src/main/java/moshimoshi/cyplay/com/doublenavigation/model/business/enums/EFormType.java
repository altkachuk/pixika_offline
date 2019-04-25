package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by romainlebouc on 08/12/2016.
 */

public enum EFormType {
    FORM_CREATE_KEY("customerCreationForm"),
    FORM_UPDATE_KEY("customerUpdateForm"),
    FORM_SHIPPING_ADDRESS_CREATE_KEY("customerShippingAddressCreationForm");


    private final String code;


    private final static Map<String, EFormType> CODE_2_EFORMTYPE_MAP = new HashMap<>();

    static {
        for (EFormType eFormType : EFormType.values()) {
            CODE_2_EFORMTYPE_MAP.put(eFormType.getCode(), eFormType);
        }
    }

    EFormType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public static EFormType getEFormTypeFromCode(String code){
        return CODE_2_EFORMTYPE_MAP.get(code);
    }
}
