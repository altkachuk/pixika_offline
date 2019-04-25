package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 11/05/2017.
 */
@Parcel
public class ProductDefaultSpecificationConfig {

    ProductDefaultSpecificationValuesConfig source;
    ProductDefaultSpecificationValuesConfig target;


    public
    @NonNull
    ProductDefaultSpecificationValuesConfig getSource() {
        return source == null ? new ProductDefaultSpecificationValuesConfig() : source;
    }

    public
    @NonNull
    ProductDefaultSpecificationValuesConfig getTarget() {
        return target == null ? new ProductDefaultSpecificationValuesConfig() : target;
    }
}
