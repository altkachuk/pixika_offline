package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.SaleBasketItemViewHolder;

/**
 * Created by romainlebouc on 27/01/2017.
 */

public class BasketItemAdapter extends ItemAdapter<BasketItem, SaleBasketItemViewHolder> {

    private List<BasketItem> basketItems;

    public BasketItemAdapter(Context context, List<BasketItem> basketItems) {
        super(context);
        this.basketItems = basketItems;
    }

    public void setBasketItems(List<BasketItem> basketItems) {
        this.basketItems = basketItems;
        notifyDataSetChanged();
    }

    @Override
    public SaleBasketItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_sale_basket_item, parent, false);
        return new SaleBasketItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SaleBasketItemViewHolder holder, int position) {
        final BasketItem basketItem = basketItems.get(position);
        if (basketItem != null) {
            holder.setItem(basketItem);
        }
    }

    @Override
    public int getItemCount() {
        return basketItems != null ? basketItems.size() : 0;
    }
}
