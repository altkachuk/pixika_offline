package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.StockByShopCategory;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.StockByShopCategoryViewHolder;

/**
 * Created by romainlebouc on 02/12/2016.
 */

public class StockByShopCategoryAdapter extends ItemAdapter<StockByShopCategory, StockByShopCategoryViewHolder> {


    public StockByShopCategoryAdapter(Context context) {
        super(context);
    }

    @Override
    public StockByShopCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_stock_by_shop_category, parent, false);
        return new StockByShopCategoryViewHolder(v);
    }

}
