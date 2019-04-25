package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by wentongwang on 17/05/2017.
 */
@Parcel
public class ProductThumbnailConfig {
    Boolean display_price;

    public Boolean getDisplay_price() {
        return display_price != null ? display_price : false;
    }
}
