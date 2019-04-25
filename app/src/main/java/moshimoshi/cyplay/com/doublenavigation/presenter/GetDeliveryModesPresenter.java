package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.DeliveryMode;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by wentongwang on 24/03/2017.
 */

public interface GetDeliveryModesPresenter extends Presenter<ResourceView<List<DeliveryMode>>>{

    void getDeliveryModes(String status);
}
