package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by romainlebouc on 01/02/2017.
 */
@Parcel
public class CustomerSalesHistoryConfig {
    List<ProductSearchSortConfig> sort_by;

    public List<ProductSearchSortConfig> getSort_by() {
        return sort_by;
    }
}
