package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

@Parcel
public class PR_HomeTilePosition {

    public Float y;
    public Float x;

    public int getY() {
        return y != null ? y.intValue() : 0;
    }

    public int getX() {
        return x != null ? x.intValue() : 0;
    }

}
