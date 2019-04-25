package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EGender;

@Parcel
public class CustomerPreviewDetails {
    public String ctry;
    public String cit;
    public String ad3;
    public String EAN;
    public String civ;
    public String zc;
    public String fn;
    public String name;
    public String gender;

    public String getCtry() {
        return ctry;
    }

    public String getCit() {
        return cit;
    }

    public String getAd3() {
        return ad3;
    }

    public String getEAN() {
        return EAN;
    }

    public String getCiv() {
        return civ;
    }

    public String getZc() {
        return zc;
    }

    public String getFn() {
        return fn;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }


    public EGender getEGender(){
        EGender gender = EGender.valueFromCode(this.getGender());
        if ( gender ==null){
            gender = EGender.UNKNOWN;
        }
        return gender;
    }
}
