package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 25/04/2017.
 */
@Parcel
public class ProductFeesConfig {
    Boolean exists;

    public Boolean getExists() {
        if (exists == null) {
            exists = Boolean.TRUE;
        }
        return exists;
    }
}
