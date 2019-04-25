package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

/**
 * Created by wentongwang on 26/04/2017.
 */

public enum EValuesSource {
    SHOPS("shops");

    private final String code;
    private final static HashMap<String, EValuesSource> CODE_TO_EValuesSource = new HashMap<>();

    static {
        for (EValuesSource eValuesSource : EValuesSource.values()) {
            CODE_TO_EValuesSource.put(eValuesSource.code, eValuesSource);
        }
    }

    EValuesSource(String code) {
        this.code = code;
    }

    public static EValuesSource getEValuesSourceCode(String code) {
        return CODE_TO_EValuesSource.get(code);
    }


    public String getCode() {
        return code;
    }
}
