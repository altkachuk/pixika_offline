package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 07/12/2016.
 */
@Parcel
public class ProductAutoCompleteConfig {

    ProductAutoCompleteItemConfig products;
    ProductAutoCompleteItemConfig families;
    ProductAutoCompleteItemConfig brands;

    public ProductAutoCompleteItemConfig getProducts() {
        return products;
    }

    public ProductAutoCompleteItemConfig getFamilies() {
        return families;
    }

    public ProductAutoCompleteItemConfig getBrands() {
        return brands;
    }
}
