package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by wentongwang on 18/05/2017.
 */
@Parcel
public class CustomerHistoryConfig {
    Boolean enable;

    public Boolean getEnable() {
        return enable != null ? enable : false;
    }
}
