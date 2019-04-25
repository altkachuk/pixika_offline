package moshimoshi.cyplay.com.doublenavigation.view;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;

/**
 * Created by damien on 29/04/15.
 */
public interface GetShopsView extends BaseView {

    void showLoading();

    void onShopsSuccess(List<Shop> PRShops);

    void onShopsError();

}