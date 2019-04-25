package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AConfig;

/**
 * Created by romainlebouc on 14/08/16.
 */
@Parcel
@ModelResource
public class Config extends PR_AConfig {

    ConfigFeature feature;
    Shop shop;

    public @NonNull ConfigFeature getFeature() {
        return feature!=null ?feature :  new ConfigFeature();
    }

    public void setFeature(ConfigFeature feature) {
        this.feature = feature;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
