package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

@Parcel
public class Shortcut {
    String segue;
    String img;
    String label;

    public String getSegue() {
        return segue;
    }

    public String getImg() {
        return img;
    }

    public String getLabel() {
        return label;
    }
}