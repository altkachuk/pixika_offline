package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerProfile;
import moshimoshi.cyplay.com.doublenavigation.view.UpdateCustomerInfoView;

/**
 * Created by damien on 06/05/15.
 */
public interface UpdateCustomerInfoPresenter extends Presenter<UpdateCustomerInfoView> {

    void updateCustomerProfile(CustomerProfile customer);

}