package moshimoshi.cyplay.com.doublenavigation.view;

import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;

/**
 * Created by wentongwang on 12/05/2017.
 */

public interface ChangeSaleView {
    void showLoading();

    void onRemoveSuccess();
    void onRemoveError(ResourceException e);

    void onAddBasketItemsSuccess();
    void onAddBasketItemsError(ResourceException e);
}
