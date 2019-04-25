package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by romainlebouc on 28/09/16.
 */
@Parcel
public class ProductSearchConfig {

    List<ProductSearchSortConfig> sort_by;
    ProductHistoryConfig history;

    public List<ProductSearchSortConfig> getSort_by() {
        return sort_by;
    }

    public ProductHistoryConfig getHistory() {
        return history != null ? history : new ProductHistoryConfig();
    }
}
