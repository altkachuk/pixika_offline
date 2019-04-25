package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 05/12/2016.
 */

public enum EPaymentType {
    CARD_BANK("card_bank",-1,
            R.string.credit_card_payment),
    CHECK("check", -1, -1),
    CASH("cash", -1, -1),
    TICKET_RESTAURANT("ticket_restaurant", -1, -1);

    private final String code;
    private final int drawableId;
    private final int labelId;

    static Map<String, EPaymentType> CODE_2_PAYMENT_TYPE = new HashMap<>();

    static {
        for (EPaymentType ePaymentType : EPaymentType.values()) {
            CODE_2_PAYMENT_TYPE.put(ePaymentType.getCode(), ePaymentType);
        }
    }


    EPaymentType(String code, int drawableId, int labelId) {
        this.code = code;
        this.drawableId = drawableId;
        this.labelId = labelId;
    }

    public String getCode() {
        return code;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getLabelId() {
        return labelId;
    }

    public static EPaymentType getEPaymentTypeFromCode(String code) {
        return CODE_2_PAYMENT_TYPE.get(code);
    }
}
