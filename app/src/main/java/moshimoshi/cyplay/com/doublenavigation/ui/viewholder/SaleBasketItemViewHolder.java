package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketItemCore;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

/**
 * Created by damien on 20/04/16.
 */
public class SaleBasketItemViewHolder extends ItemViewHolder<BasketItem> {

    @BindView(R.id.basket_item_core)
    BasketItemCore basketItemCore;

    @BindView(R.id.increase_sku_quantity)
    ImageView increaseSkuQuantity;

    public SaleBasketItemViewHolder(View view) {
        super(view);
    }

    @Override
    public void setItem(BasketItem basketItem) {
        basketItemCore.setBasketItem(basketItem);
    }

}