package moshimoshi.cyplay.com.doublenavigation.model.events;

/**
 * Created by wentongwang on 29/05/2017.
 */

public class AutoFillPlaceEvent {
    private String placeId;

    public AutoFillPlaceEvent(String placeId){
        this.placeId = placeId;
    }

    public String getPlaceId() {
        return placeId;
    }
}
