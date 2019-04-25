package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import android.graphics.Color;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 07/09/16.
 */
@Parcel
public class RfmIndicator {

    String rfm;
    String color;

    public String getRfm() {
        return rfm;
    }

    public Integer getColor(){
        try{
            return Color.parseColor(color);
        }catch (Exception e){
            return null;
        }
    }

}
