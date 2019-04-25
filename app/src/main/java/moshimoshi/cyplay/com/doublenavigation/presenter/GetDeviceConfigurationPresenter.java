package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import moshimoshi.cyplay.com.doublenavigation.view.GetSellersView;

/**
 * Created by damien on 28/04/15.
 */
public interface GetDeviceConfigurationPresenter extends Presenter<GetSellersView> {

    void getDeviceConfiguration();

    void onDeviceConfiguration(List<Seller> PRSellers);

}
