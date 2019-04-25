package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 05/12/2016.
 */

public enum ETransactionStatus {
    TRANSACTION_PENDING("pending", R.string.transaction_pending),
    TRANSACTION_PAID("paid", R.string.transaction_paid),
    TRANSACTION_FAILED("failed", R.string.transaction_failed),
    TRANSACTION_CANCELED("canceled", R.string.transaction_canceled);


    private final String code;
    private final int labelId;


    static Map<String, ETransactionStatus> CODE_2_TRANSACTION_STATUS = new HashMap<>();

    static {
        for (ETransactionStatus eTransactionStatus : ETransactionStatus.values()) {
            CODE_2_TRANSACTION_STATUS.put(eTransactionStatus.getCode(), eTransactionStatus);
        }
    }

    ETransactionStatus(String code, int labelId) {
        this.code = code;
        this.labelId = labelId;
    }

    public String getCode() {
        return code;
    }

    public int getLabelId() {
        return labelId;
    }


    public static ETransactionStatus getETransactionStatusFromCode(String code) {
        return CODE_2_TRANSACTION_STATUS.get(code);
    }
}
