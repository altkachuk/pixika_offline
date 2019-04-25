package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CatalogCategoryViewHolder;

/**
 * Created by damien on 20/04/16.
 */
public class CatalogCategoryAdapter extends ItemAdapter<Category,CatalogCategoryViewHolder>  {

    private List<Category> categories;

    public CatalogCategoryAdapter(Context context, List<Category> categories) {
        super(context);
        this.categories = categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @Override
    public CatalogCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_catalog_product_category, parent, false);
        return new CatalogCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CatalogCategoryViewHolder holder, int position) {
        final Category category = categories.get(position);
        if (category != null) {
            holder.setItem(category);
        }
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

}
