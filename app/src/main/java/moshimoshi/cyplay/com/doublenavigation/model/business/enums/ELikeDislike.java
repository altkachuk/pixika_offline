package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by romainlebouc on 04/01/2017.
 */

public enum ELikeDislike {
    LIKE(1, true,2),
    DISLIKE(-1, true, 2),
    NONE( 0, false,2);

    private final int rating;
    private final boolean active;
    private final int neutralStatePosition;

    public static Map<Integer, ELikeDislike> RATING_2_ELIKE_DISLIKE = new HashMap<>();

    static {
        for ( ELikeDislike eLikeDislike : ELikeDislike.values()){
            RATING_2_ELIKE_DISLIKE.put(eLikeDislike.rating, eLikeDislike);
        }
    }

    ELikeDislike( int rating, boolean active,  int neutralStatePosition) {
        this.rating = rating;
        this.active = active;
        this.neutralStatePosition = neutralStatePosition;
    }

    public int getRating() {
        return rating;
    }

    public boolean isActive() {
        return active;
    }

    public ELikeDislike getNeutralState(){
        return ELikeDislike.values()[neutralStatePosition];
    }

    public ELikeDislike getELikeDislike( ELikeDislike opposite) {
        if ( !isActive()){
            return this;
        }else{
            if ( opposite.isActive()){
                return getNeutralState();
            }else{
                return this;
            }
        }
    }

    public static ELikeDislike valueOfELikeDislikeForRating(int rating){
        ELikeDislike result = RATING_2_ELIKE_DISLIKE.get(rating);
        if ( result ==null){
            result = NONE;
        }
        return result;
    }

}
