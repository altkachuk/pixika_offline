package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import moshimoshi.cyplay.com.doublenavigation.R;

/**
 * Created by romainlebouc on 12/01/2017.
 */

public enum  EAddToBasketStatus {
    OUT_OF_STOCK(R.string.add_to_basket_out_of_stock_in_shop  ),
    IN_STOCK(R.string.add_item_to_basket);

    private final  int labelId;

    EAddToBasketStatus(int labelId) {
        this.labelId = labelId;
    }

    public int getLabelId() {
        return labelId;
    }
}
