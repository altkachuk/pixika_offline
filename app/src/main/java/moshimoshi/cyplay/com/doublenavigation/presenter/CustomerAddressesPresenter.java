package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.Address;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by romainlebouc on 08/12/2016.
 */

public interface CustomerAddressesPresenter extends Presenter<ResourceView<Address>> {

    void createAddress(String customerId, Address address) ;

    void updateAddress(String customerId, Address address) ;

}
