package moshimoshi.cyplay.com.doublenavigation.model.events;

import ninja.cyplay.com.apilibrary.models.abstractmodels.DQEResult;

/**
 * Created by wentongwang on 08/06/2017.
 */

public class DQEAutoFillPlaceEvent {
    private DQEResult placeResult;

    public DQEAutoFillPlaceEvent(DQEResult placeResult) {
        this.placeResult = placeResult;
    }

    public DQEResult getPlaceResult() {
        return placeResult;
    }
}
