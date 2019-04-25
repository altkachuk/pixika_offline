package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.StockByShopCategory;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

/**
 * Created by romainlebouc on 02/12/2016.
 */

public class StockByShopCategoryViewHolder extends ItemViewHolder<StockByShopCategory> {

    @Inject
    ConfigHelper configHelper;

    @BindView(R.id.shop_category)
    TextView shopCategory;

    @BindView(R.id.stock_indicator)
    ImageView stockIndicator;

    public StockByShopCategoryViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(StockByShopCategory stockByShopCategory) {
        if (stockByShopCategory.getStock() != null && stockByShopCategory.getStock() > 0) {
            stockIndicator.setImageResource(R.drawable.ic_in_stock);
        } else {
            stockIndicator.setImageResource(R.drawable.ic_out_of_stock);
        }

        switch (stockByShopCategory.geteShopCategory()) {
            case CURRENT_SHOP:
                shopCategory.setText(configHelper.getCurrentShop().getName(true));
                break;
            case WEB_SHOP:
            case OTHER_REGULAR_SHOP:
                shopCategory.setText(stockByShopCategory.geteShopCategory().getLabelId());
                break;
        }

    }
}
