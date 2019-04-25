package moshimoshi.cyplay.com.doublenavigation.view;

import moshimoshi.cyplay.com.doublenavigation.model.business.WishlistItem;

/**
 * Created by damien on 15/05/15.
 */
public interface OpinionView extends BaseView {

    void onAddToWishlistSuccess(WishlistItem wishlistItem);

    void onDeleteFromWishlistSuccess();

}
