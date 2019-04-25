package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AFee;

/**
 * Created by romainlebouc on 30/01/2017.
 */
@Parcel
@ModelResource
public class Fee extends PR_AFee {

    String product_id;
    String sku_id;
    Double value;
    String tag;

    public String getProduct_id() {
        return product_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public Double getValue() {
        return value;
    }

    public String getTag() {
        return tag;
    }
}
