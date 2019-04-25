package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

@Parcel
public class Panel {
    Boolean enable;
    String segue;
    String label;

    public Boolean getEnable() {
        return enable;
    }

    public String getSegue() {
        return segue;
    }

    public String getLabel() {
        return label;
    }
}
