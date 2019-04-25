package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by romainlebouc on 14/10/16.
 */
@Parcel
public class ProductCatalogConfig {
    List<ProductSearchSortConfig> sort_by;
    String root_category_id;

    public List<ProductSearchSortConfig> getSort_by() {
        return sort_by;
    }

    public String getRoot_category_id() {
        return root_category_id;
    }
}
