package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 10/05/16.
 */
@Parcel
public class HomeEvent {

    int days_before;
    int days_after;

    public int getDays_before() {
        return days_before;
    }

    public void setDays_before(int days_before) {
        this.days_before = days_before;
    }

    public int getDays_after() {
        return days_after;
    }

    public void setDays_after(int days_after) {
        this.days_after = days_after;
    }
}
