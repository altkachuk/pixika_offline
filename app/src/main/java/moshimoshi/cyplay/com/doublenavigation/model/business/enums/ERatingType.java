package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by romainlebouc on 04/01/2017.
 */

public enum ERatingType {
    STARS("stars"), LIKE("like"), STARS10("stars10");

    public String tag;

    private static Map<String, ERatingType> TAG_2_RATING_TYPE = new HashMap<>();
    static {
        for ( ERatingType eRatingType : ERatingType.values()){
            TAG_2_RATING_TYPE.put(eRatingType.tag, eRatingType);
        }
    }

    ERatingType(String tag) {
        this.tag = tag;
    }

    public static ERatingType valueOfERatingTypeForTag(String tag){
        return TAG_2_RATING_TYPE.get(tag);
    }


}
