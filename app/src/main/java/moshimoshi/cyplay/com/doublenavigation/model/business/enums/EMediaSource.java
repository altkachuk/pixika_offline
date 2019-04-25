package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

/**
 * Created by romainlebouc on 06/09/16.
 */
public enum EMediaSource {
    SOURCE("source"),
    HASH("hash");

    private final String code;
    private final static HashMap<String, EMediaSource> CODE_TO_EMEDIASOURCE = new HashMap<>();

    static {
        for (EMediaSource eMediaSource : EMediaSource.values()) {
            CODE_TO_EMEDIASOURCE.put(eMediaSource.code, eMediaSource);
        }
    }

    EMediaSource(String code) {
        this.code = code;
    }

    public static EMediaSource getEMediaSourceFromCode(String code) {
        return CODE_TO_EMEDIASOURCE.get(code);
    }
}
