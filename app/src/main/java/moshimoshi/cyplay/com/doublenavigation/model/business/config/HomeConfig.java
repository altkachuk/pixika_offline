package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by damien on 25/07/16.
 */
@Parcel
public class HomeConfig {

    public HomeConfigGrid grid;

    public HomeConfigGrid getGrid() {
        return grid!=null ? grid : new HomeConfigGrid();
    }

}
