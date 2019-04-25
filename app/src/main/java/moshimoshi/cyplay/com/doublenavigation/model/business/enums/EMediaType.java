package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

/**
 * Created by romainlebouc on 02/05/16.
 */
public enum EMediaType {
    PICTURE(new String[]{"image", "PICTURE"}),
    PICTURE_PREVIEW(new String[]{"image", "PICTURE_PREVIEW"}),
    PICTURE_SELECTOR(new String[]{"SELECTOR_PICTURE"}),
    VIDEO(new String[]{"video"}),
    ICON(new String[]{"ICON"});

    private final String[] codes;

    EMediaType(String[] codes) {
        this.codes = codes;
    }

    public static HashMap<String, EMediaType> CODE_2_MEDIATYPE = new HashMap<>();

    static {
        for (EMediaType eventEMediaType : EMediaType.values()) {
            for ( String code: eventEMediaType.codes){
                CODE_2_MEDIATYPE.put(code, eventEMediaType);
            }
        }
    }

    public static EMediaType valueFromCode(String code) {
        return CODE_2_MEDIATYPE.get(code);
    }

}


