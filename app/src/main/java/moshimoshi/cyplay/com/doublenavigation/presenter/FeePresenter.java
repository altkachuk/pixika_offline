package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Fee;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by romainlebouc on 30/01/2017.
 */

public interface FeePresenter extends Presenter<ResourceView<List<Fee>>> {
    void getFees(String productId,
                 String skuId,
                 String shopId);
}
