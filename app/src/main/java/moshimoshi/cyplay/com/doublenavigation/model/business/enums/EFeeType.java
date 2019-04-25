package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 30/01/2017.
 */

public enum EFeeType {
    SHIPPING("fees_shipping", R.string.extra_shipping_fees),
    PREPARATION("fees_preparation", R.string.extra_preparation_fees);

    private final int labelId;
    private final String tag;

    public final static Map<String, EFeeType> CODE_2_FEETYPE = new HashMap<>();

    static {
        for (EFeeType eFeeType : EFeeType.values()) {
            CODE_2_FEETYPE.put(eFeeType.tag, eFeeType);
        }
    }

    EFeeType(String tag, int labelId) {
        this.tag = tag;
        this.labelId = labelId;
    }

    public int getLabelId() {
        return labelId;
    }

    public String getTag() {
        return tag;
    }

    public static EFeeType valueFromTag(String code) {
        return CODE_2_FEETYPE.get(code);
    }

}
