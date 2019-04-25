package moshimoshi.cyplay.com.doublenavigation.view;

/**
 * Created by damien on 29/04/15.
 */
public interface ProvisionDeviceView extends BaseView {

    void showLoading();

    void onProvisionDeviceSuccess();

    void onProvisionDeviceError();

}