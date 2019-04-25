package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by wentongwang on 10/03/2017.
 */
@Parcel
public class TopBarScan {

    public Boolean customer;
    public Boolean product;

    public Boolean getCustomer() {
        return customer != null ? customer : true;
    }

    public void setCustomer(Boolean customer) {
        this.customer = customer;
    }

    public Boolean getProduct() {
        return product != null ? product : true;
    }

    public void setProduct(Boolean product) {
        this.product = product;
    }
}
