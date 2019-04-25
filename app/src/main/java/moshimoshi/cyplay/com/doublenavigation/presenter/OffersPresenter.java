package moshimoshi.cyplay.com.doublenavigation.presenter;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Offer;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by romainlebouc on 02/09/16.
 */
public interface OffersPresenter extends Presenter<ResourceView<List<Offer>>>{

    void getShopOffers(String shopId);
}
