package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by damien on 09/11/16.
 */
@Parcel
public class ProductSpecSelector implements Serializable {

    String spec_id;

    public String getSpec_id() {
        return spec_id;
    }

    public void setSpec_id(String spec_id) {
        this.spec_id = spec_id;
    }


}
