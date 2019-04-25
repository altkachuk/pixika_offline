package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import android.content.res.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by romainlebouc on 26/12/2016.
 */

public enum EConfigOrientation {

    LANDSCAPE("landscape"), PORTRAIT("portrait"), ALL("all");

    private final static Map<String, EConfigOrientation> CODE_2_CONFIG_ORIENTATION = new HashMap<>();

    private final String code;

    static {
        for (EConfigOrientation configOrientation : EConfigOrientation.values()) {
            CODE_2_CONFIG_ORIENTATION.put(configOrientation.code, configOrientation);
        }
    }

    EConfigOrientation(String code) {
        this.code = code;
    }

    public static EConfigOrientation getEConfigOrientationFromCode(String code) {
        return CODE_2_CONFIG_ORIENTATION.get(code);
    }

    public static boolean orientationsMatch(EConfigOrientation eConfigOrientation, int deviceOrientation) {
        boolean result = false;
        if (eConfigOrientation == null) {
            result = true;
        } else {
            switch (eConfigOrientation) {
                case LANDSCAPE:
                    result = Configuration.ORIENTATION_LANDSCAPE == deviceOrientation;
                    break;
                case PORTRAIT:
                    result = Configuration.ORIENTATION_PORTRAIT == deviceOrientation;
                    break;
                case ALL:
                    result = true;
                    break;
            }

        }
        return result;


    }

}
