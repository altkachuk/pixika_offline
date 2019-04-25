package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

/**
 * Created by romainlebouc on 02/05/16.
 */
public enum EventMediaType {
    PICTURE("PICTURE"),
    ICON("ICON");

    private final String code;

    EventMediaType(String code) {
        this.code = code;
    }

    public static HashMap<String, EventMediaType> CODE_2_EVENTMEDIATYPE = new HashMap<>();


    static {
        for (EventMediaType eventMediaType : EventMediaType.values()) {
            CODE_2_EVENTMEDIATYPE.put(eventMediaType.code, eventMediaType);

        }
    }

    public static EventMediaType valueFromCode(String code) {
        return CODE_2_EVENTMEDIATYPE.get(code);
    }

}


