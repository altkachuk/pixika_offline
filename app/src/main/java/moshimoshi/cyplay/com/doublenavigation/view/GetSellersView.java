package moshimoshi.cyplay.com.doublenavigation.view;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;

/**
 * Created by damien on 28/04/15.
 */
public interface GetSellersView extends BaseView {

    void onSellersSuccess(List<Seller> sellers);

}
