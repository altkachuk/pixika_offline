package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

/**
 * Created by wentongwang on 26/06/2017.
 */

public enum EFeedbackType {
    BUG("BUG"),
    SUGGESTION("FR"),
    PASSWORD_FORGET("LOST_PASSWORD");

    private final String code;
    private final static HashMap<String, EFeedbackType> CODE_TO_EFeedbackType = new HashMap<>();

    static {
        for (EFeedbackType eFeedbackType : EFeedbackType.values()) {
            CODE_TO_EFeedbackType.put(eFeedbackType.code, eFeedbackType);
        }
    }

    EFeedbackType(String code) {
        this.code = code;
    }

    public static EFeedbackType getFeedBackTypeByCode(String code) {
        return CODE_TO_EFeedbackType.get(code);
    }


    public String getCode() {
        return code;
    }

}
