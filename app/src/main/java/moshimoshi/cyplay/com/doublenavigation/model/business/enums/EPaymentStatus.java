package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 05/12/2016.
 */

public enum EPaymentStatus {

    PAYMENT_STARTED("started", R.string.payment_started, R.color.Gray, false),
    PAYMENT_PENDING("pending", R.string.payment_pending, R.color.Gray, false),
    PAYMENT_CANCELED("canceled", R.string.payment_canceled, R.color.error_red, false),
    PAYMENT_FAILED("failed", R.string.payment_failed, R.color.error_red, false),
    PAYMENT_PARTIAL_PAID("partial_paid", R.string.payment_partial_paid, R.color.warning_yellow, false),
    PAYMENT_PAID("paid", R.string.payment_paid, R.color.success_green, true),
    PAYMENT_SUCCEEDED("succeeded", R.string.payment_succeeded, R.color.success_green, true);

    private final String code;
    private final int labelId;
    private final int colorId;
    private final boolean receiptSharable;

    static Map<String, EPaymentStatus> CODE_2_PAYMENT_STATUS = new HashMap<>();

    static {
        for (EPaymentStatus ePaymentStatus : EPaymentStatus.values()) {
            CODE_2_PAYMENT_STATUS.put(ePaymentStatus.getCode(), ePaymentStatus);
        }
    }

    EPaymentStatus(String code, int labelId, int colorId, boolean receiptSharable) {
        this.code = code;
        this.labelId = labelId;
        this.colorId = colorId;
        this.receiptSharable = receiptSharable;
    }

    public String getCode() {
        return code;
    }

    public static EPaymentStatus getEPaymentStatusFromCode(String code) {
        return CODE_2_PAYMENT_STATUS.get(code);
    }

    public int getLabelId() {
        return labelId;
    }

    public int getColorId() {
        return colorId;
    }

    public boolean isReceiptSharable() {
        return receiptSharable;
    }

}
