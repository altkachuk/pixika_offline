package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ADeviceConfiguration;

/**
 * Created by romainlebouc on 14/08/16.
 */
@Parcel
public class DeviceConfiguration extends PR_ADeviceConfiguration{

    List<Seller> sellers;
    String shop_id;

    public List<Seller> getSellers() {
        return sellers;
    }

    public void setSellers(List<Seller> sellers) {
        this.sellers = sellers;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }
}
