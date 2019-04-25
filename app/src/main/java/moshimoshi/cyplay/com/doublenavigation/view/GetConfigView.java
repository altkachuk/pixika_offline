package moshimoshi.cyplay.com.doublenavigation.view;

public interface GetConfigView extends BaseView {

    void showLoading();

    void onConfigSuccess();

    void goToDeviceRegistration();

    void appNotSupported(String appVersion);

}
