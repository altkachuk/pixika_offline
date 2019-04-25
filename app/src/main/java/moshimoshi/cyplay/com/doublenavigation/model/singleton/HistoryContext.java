package moshimoshi.cyplay.com.doublenavigation.model.singleton;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.history.CustomerHistory;
import moshimoshi.cyplay.com.doublenavigation.model.business.history.ProductHistory;

/**
 * Created by romainlebouc on 12/05/2017.
 */

public class HistoryContext {

    private final static int HISTORY_GIRD_VIEW_LINE_NUMBER = 3;
    private Context context;
    private int historySize;

    // Seller -> List of product history
    private final Map<String, List<ProductHistory>> historyPerSeller = new HashMap<>();
    private final Map<String, List<CustomerHistory>> customerHistoryPerSeller = new HashMap<>();

    public HistoryContext(Context context) {
        this.context = context;
        this.historySize = context.getResources().getInteger(R.integer.catalog_columns_count) * HISTORY_GIRD_VIEW_LINE_NUMBER;
    }

    public void addProductHistory(ProductHistory productHistory) {

        List<ProductHistory> productHistories = historyPerSeller.get(productHistory.getSellerId());
        if (productHistories == null) {
            productHistories = new ArrayList<>();
            historyPerSeller.put(productHistory.getSellerId(), productHistories);
        }

        Collections.sort(productHistories);

        if (productHistories.contains(productHistory)) {
            productHistories.remove(productHistory);
        }
        if (productHistories.size() == historySize) {
            if (!productHistories.contains(productHistory)) {
                productHistories.remove(historySize - 1);
            }
        }
        productHistories.add(productHistory);
    }

    public List<ProductHistory> getProductHistory(String sellerId) {
        return historyPerSeller.get(sellerId);
    }

    public void addCustomerHistory(CustomerHistory customerHistory) {

        List<CustomerHistory> customerHistories = customerHistoryPerSeller.get(customerHistory.getSellerId());
        if (customerHistories == null) {
            customerHistories = new ArrayList<>();
            customerHistoryPerSeller.put(customerHistory.getSellerId(), customerHistories);
        }

        Collections.sort(customerHistories);

        if (customerHistories.contains(customerHistory)) {
            customerHistories.remove(customerHistory);
        }
        if (customerHistories.size() == historySize) {
            if (!customerHistories.contains(customerHistory)) {
                customerHistories.remove(historySize - 1);
            }
        }
        customerHistories.add(customerHistory);
    }

    public List<CustomerHistory> getCustomerHistory(String sellerId) {
        return customerHistoryPerSeller.get(sellerId);
    }

    public int getHistorySize(){
        return historySize;
    }

}
