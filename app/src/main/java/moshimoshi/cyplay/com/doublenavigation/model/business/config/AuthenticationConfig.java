package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 28/08/16.
 */
@Parcel
public class AuthenticationConfig {
    AuthenticationAutomaticLogOutConfig automatic_log_out;
    Boolean force_change_tmp_password;
    Boolean can_change_password;

    public AuthenticationAutomaticLogOutConfig getAutomatic_log_out() {
        return automatic_log_out;
    }

    public Boolean getForce_change_tmp_password() {
        return force_change_tmp_password != null ? force_change_tmp_password : false;
    }

    public Boolean getCan_change_password() {
        return can_change_password != null ? can_change_password : false;
    }
}
