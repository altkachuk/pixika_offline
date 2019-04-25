package moshimoshi.cyplay.com.doublenavigation.presenter;


import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerProfile;
import moshimoshi.cyplay.com.doublenavigation.view.CreateCustomerView;

/**
 * Created by damien on 20/01/16.
 */
public interface CreateCustomerInfoPresenter extends Presenter<CreateCustomerView> {

    void createCustomerProfile(CustomerProfile customer);

}
