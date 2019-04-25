package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

/**
 * Created by romainlebouc on 21/09/16.
 */
public enum  EChannel {
    EMAIL("email");

    private final String code;
    private final static HashMap<String, EChannel> CODE_TO_ECHANNEL = new HashMap<>();

    static {
        for (EChannel eChannel : EChannel.values()) {
            CODE_TO_ECHANNEL.put(eChannel.code, eChannel);
        }
    }

    EChannel(String code) {
        this.code = code;
    }

    public static EChannel getEChannelFromCode(String code) {
        return CODE_TO_ECHANNEL.get(code);
    }


    public String getCode() {
        return code;
    }
}
