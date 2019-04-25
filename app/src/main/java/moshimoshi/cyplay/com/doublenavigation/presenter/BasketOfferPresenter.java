package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by romainlebouc on 06/04/2017.
 */

public interface BasketOfferPresenter extends Presenter<ResourceView<BasketOffer>> {

    void activeCustomerOffer(String offer_id);

    void inActiveCustomerOffer(String offer_id);
}
