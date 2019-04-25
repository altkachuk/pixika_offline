package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

@Parcel
public class PR_HomeTileSize {

    public Float h;
    public Float w;

    public int getH() {
        return h != null ? h.intValue() : 0;
    }

    public int getW() {
        return w != null ? w.intValue() : 0;
    }

}