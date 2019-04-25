package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.models.abstractmodels.DQEResult;

/**
 * Created by wentongwang on 08/06/2017.
 */

public interface DQEAddressPresenter extends Presenter<ResourceView<List<DQEResult>>>{

    void setBaseUrl(String baseUrl);

    void setLicence(String licence);

    void getAddress(String query, String countryCode);
}
