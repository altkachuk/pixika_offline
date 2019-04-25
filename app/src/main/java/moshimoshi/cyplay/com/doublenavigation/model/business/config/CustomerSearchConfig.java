package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

/**
 * Created by romainlebouc on 31/01/2017.
 */
@Parcel
public class CustomerSearchConfig {
    Integer max_results_count;

    CustomerHistoryConfig history;

    public Integer getMax_results_count() {
        return max_results_count != null ? max_results_count : Integer.MAX_VALUE;
    }

    public CustomerHistoryConfig getHistory() {
        return history != null ? history : new CustomerHistoryConfig();
    }
}
