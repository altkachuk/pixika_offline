package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABarCodeInfo;

/**
 * Created by romainlebouc on 19/08/16.
 */
@Parcel
@ModelResource
public class BarCodeInfo extends PR_ABarCodeInfo{

    String eanc;
    String product_id;
    String customer_id;

    public String getEanc() {
        return eanc;
    }

    public void setEanc(String eanc) {
        this.eanc = eanc;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
