package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaSource;

/**
 * Created by romainlebouc on 06/09/16.
 */
@Parcel
public class ProductMediasConfig {

    String picture;

    public String getPicture() {
        return picture;
    }

    public EMediaSource getPictureSource(){
        return EMediaSource.getEMediaSourceFromCode(picture);
    }


    public void setPicture(String picture) {
        this.picture = picture;
    }
}
