package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

/**
 * Created by wentongwang on 24/03/2017.
 */
@Parcel
public class ShippingTime {

    int days;
    int hours;
    int minutes;

    public ShippingTime() {

    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

}
