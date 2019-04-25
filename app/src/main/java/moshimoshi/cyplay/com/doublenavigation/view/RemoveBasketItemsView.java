package moshimoshi.cyplay.com.doublenavigation.view;

import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;

/**
 * Created by wentongwang on 12/05/2017.
 */

public interface RemoveBasketItemsView {
    void onRemoveSuccess();
    void onRemoveBasketItemsError(ResourceException e);
    void onClearError(ResourceException e);
}
