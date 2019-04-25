package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by damien on 25/07/16.
 */
@Parcel
public class HomeConfigGrid {

    public List<PR_HomeTile> tiles;
    public List<PR_HomeTile> tiles_landscape;


    public List<PR_HomeTile> getTiles() {
        return tiles;
    }

    public List<PR_HomeTile> getTiles_landscape() {
        return tiles_landscape;
    }
}
