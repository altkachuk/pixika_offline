package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 03/10/16.
 */
public enum EPhoneType {
    LANDLINE(R.string.form_landline_phone),
    MOBILE(R.string.form_mobile_phone),
    PROFESSIONAL(R.string.form_professional_phone);

    private int labelResourceId;

    EPhoneType(int labelResourceId) {
        this.labelResourceId = labelResourceId;
    }

    public int getLabelResourceId() {
        return labelResourceId;
    }
}
