package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 06/06/16.
 */
public enum EGender {
    MALE("M", R.drawable.ic_gender_male_black_24dp),
    FEMALE("F",R.drawable.ic_gender_female_black_24dp),
    COUPLE("C",R.drawable.ic_wc_black_24dp),
    UNKNOWN("U", R.drawable.ic_help_outline_black_24dp);

    private final String code;
    private final int iconResource;

    EGender(String code, int iconResource) {
        this.code = code;
        this.iconResource = iconResource;
    }

    public static HashMap<String, EGender> CODE_2_GENDER = new HashMap<>();


    static {
        for (EGender gender : EGender.values()) {
            CODE_2_GENDER.put(gender.code, gender);
        }
    }

    public static EGender valueFromCode(String code) {
        return CODE_2_GENDER.get(code);
    }

    public int getIconResource() {
        return iconResource;
    }
}
