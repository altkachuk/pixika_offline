package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by damien on 05/05/15.
 */
public interface CustomerSearchPresenter extends Presenter<ResourceView<List<Customer>>> {

    void searchCustomer(String customerName);

    void searchCustomer(String firstName, String lastName, String zipCode);

    void searchCustomer(Map<String ,String> filters);
}
