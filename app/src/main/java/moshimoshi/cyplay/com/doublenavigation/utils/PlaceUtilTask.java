package moshimoshi.cyplay.com.doublenavigation.utils;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPlaceUtilTaskType;

/**
 * Created by wentongwang on 01/06/2017.
 */

public class PlaceUtilTask extends AsyncTask<Double, String, String> {

    private EPlaceUtilTaskType type;

    private CallBack callBack;

    private Geocoder mGeocoder;

    public PlaceUtilTask(EPlaceUtilTaskType taskType, Geocoder geocoder) {
        this.type = taskType;
        this.mGeocoder = geocoder;
    }

    @Override
    protected String doInBackground(Double... params) {
        if (params.length < 2) {
            return null;
        }

        double lat = params[0];
        double lon = params[1];
        String result = StringUtils.EMPTY_STRING;

        List<Address> addresses;
        try {
            addresses = mGeocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (addresses != null && addresses.size() > 0) {

            switch (type) {
                case CITY:
                    result = addresses.get(0).getLocality();
                    break;
                case COUNTRY:
                    result = addresses.get(0).getCountryCode();
                    break;
                case POSTCODE:
                    result = addresses.get(0).getPostalCode();
                    break;
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (callBack != null) {
            callBack.onResult(result);
        }
    }

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    public interface CallBack {
        void onResult(String result);
    }
}
