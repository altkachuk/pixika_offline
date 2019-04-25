package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CatalogCategoryViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.SaleViewHolder;

/**
 * Created by damien on 20/04/16.
 */
public class SaleAdapter extends ItemAdapter<Sale,SaleViewHolder>  {

    private List<Sale> sales;

    public SaleAdapter(Context context, List<Sale> sales) {
        super(context);
        this.sales = sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
        notifyDataSetChanged();
    }

    @Override
    public SaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_sale, parent, false);
        return new SaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SaleViewHolder holder, int position) {
        final Sale sale = sales.get(position);
        if (sale != null) {
            holder.setItem(sale);
        }
    }

    @Override
    public int getItemCount() {
        return sales != null ? sales.size() : 0;
    }

}
