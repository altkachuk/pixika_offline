package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.view.ProvisionDeviceView;

/**
 * Created by damien on 29/04/15.
 */
public interface ProvisionDevicePresenter extends Presenter<ProvisionDeviceView> {

    void provisionDevice(String storeId);

    void updateStore(String storeId);

}
