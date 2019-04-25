package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by damien on 06/05/15.
 */
public interface GetCustomerInfoPresenter extends Presenter<ResourceView<Customer>> {

    void getCustomerInfo(String id);

    void getCustomerFromEan(String ean);

    void getCustomerOffer(String id);

}