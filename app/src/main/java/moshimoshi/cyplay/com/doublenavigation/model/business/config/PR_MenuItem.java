package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EConfigOrientation;

/**
 * Created by damien on 11/03/16.
 */
@Parcel
public class PR_MenuItem {

    public String label;
    public String tag;
    public String icon;
    public Boolean visibility = true;
    public String orientation;
    public PR_ItemSeparator separator;

    public String getLabel() {
        return label;
    }

    public String getTag() {
        return tag;
    }

    public String getIcon() {
        return icon;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public PR_ItemSeparator getSeparator() {
        return separator;
    }

    public EConfigOrientation getEConfigOrientation(){
        return EConfigOrientation.getEConfigOrientationFromCode(orientation);
    }

}
