package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 25/04/2017.
 */
@Parcel
public class PaymentDisplayConfig {

    Boolean keep_screen_on;

    public Boolean getKeep_screen_on() {
        if (keep_screen_on == null) {
            keep_screen_on = Boolean.TRUE;
        }
        return keep_screen_on;
    }


}
