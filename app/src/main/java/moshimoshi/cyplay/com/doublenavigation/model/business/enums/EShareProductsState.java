package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 24/08/16.
 */
public enum EShareProductsState {
    PRODUCT_TO_SELECT(R.string.share_wishlist_select_products),
    PRODUCTS_SELECTED(R.plurals.share_wishlist_send_products);

    private final int labelResourceId;

    EShareProductsState(int labelResourceId) {
        this.labelResourceId = labelResourceId;
    }



}
