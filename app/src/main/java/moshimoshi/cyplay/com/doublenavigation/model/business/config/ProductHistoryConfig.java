package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by wentongwang on 17/05/2017.
 */
@Parcel
public class ProductHistoryConfig {
    Boolean enable;

    public Boolean getEnable() {
        return enable != null ? enable : false;
    }
}
