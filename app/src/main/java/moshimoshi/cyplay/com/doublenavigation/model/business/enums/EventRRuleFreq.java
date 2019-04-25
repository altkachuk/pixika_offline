package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

/**
 * Created by romainlebouc on 28/04/16.
 */
public enum EventRRuleFreq {
    YEARLY("yearly");

    private final String code;

    EventRRuleFreq(String code) {
        this.code = code;

    }


    public final static HashMap<String, EventRRuleFreq> CODE_2_FREQ = new HashMap<>();

    static {
        for (EventRRuleFreq freq : EventRRuleFreq.values()) {
            CODE_2_FREQ.put(freq.code, freq);

        }
    }

    public static EventRRuleFreq valueFromCode(String code) {
        return CODE_2_FREQ.get(code);
    }
}