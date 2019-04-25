package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.view.GetConfigView;

/**
 * Created by damien on 27/04/15.
 */
public interface GetConfigPresenter extends Presenter<GetConfigView> {

    void getConfig(String shopId);

    void onReloadClick();

}
