package moshimoshi.cyplay.com.doublenavigation.utils;

import android.content.Context;
import android.location.Geocoder;

import java.util.Locale;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPlaceUtilTaskType;

/**
 * Created by wentongwang on 29/05/2017.
 */

public class GooglePlaceUtils {

    private Geocoder mGeocoder;

    public GooglePlaceUtils(Context context) {
        mGeocoder = new Geocoder(context, Locale.getDefault());
    }

    public void getCityNameByCoordinates(double lat, double lon) {
        buildTask(EPlaceUtilTaskType.CITY)
                .execute(lat, lon);
    }

    public void getPostalCodeByCoordinates(double lat, double lon) {
        buildTask(EPlaceUtilTaskType.POSTCODE)
                .execute(lat, lon);
    }

    public void getCountryNameByCoordinates(double lat, double lon) {
        buildTask(EPlaceUtilTaskType.COUNTRY)
                .execute(lat, lon);
    }

    private PlaceUtilTask buildTask(EPlaceUtilTaskType taskType) {
        PlaceUtilTask task = new PlaceUtilTask(taskType, mGeocoder);
        task.setCallBack(callBack);
        return task;
    }

    private PlaceUtilTask.CallBack callBack;

    public void setCallBack(PlaceUtilTask.CallBack callBack) {
        this.callBack = callBack;
    }


}
