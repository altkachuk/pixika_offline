package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ProductItemGridThumbnailViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.EmptyViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.IEmptyOrItemViewHolder;

/**
 * Created by damien on 10/06/16.
 */
public class ProductItemThumbnailAdapter extends EmptyItemAdapter<ProductItem, IEmptyOrItemViewHolder<ProductItem>> implements SelectableItemAdapter<ProductItem> {

    private Context context;

    private int itemsToDisplayCount = 0;
    private final List<ProductFilter> activeFilters;

    public ProductItemThumbnailAdapter(Context ctx, int itemsToDisplayCount ) {
        this.context = ctx;
        this.itemsToDisplayCount = itemsToDisplayCount;
        this.activeFilters = null;
    }

    public ProductItemThumbnailAdapter(Context ctx,
                                       int itemsToDisplayCount,
                                       List<ProductFilter> activeFilters ) {
        this.context = ctx;
        this.itemsToDisplayCount = itemsToDisplayCount;
        this.activeFilters = activeFilters;
    }
    private boolean selectionMode = false;

    private List<ProductItem> selectedProductItems = new ArrayList<>();

    @Override
    public IEmptyOrItemViewHolder<ProductItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_POSTION) {
            View v = LayoutInflater.from(parent.getContext()).inflate(ProductItemGridThumbnailViewHolder.LAYOUT, parent, false);
            return new ProductItemGridThumbnailViewHolder(v, context, this,this.activeFilters);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_thumbnail_empty, parent, false);
            return new EmptyViewHolder(v);
        }
    }

    @Override
    public int getItemToDisplayCount() {
        return items != null ? (items.size() < itemsToDisplayCount ? itemsToDisplayCount : items.size()) : itemsToDisplayCount;
    }

    @Override
    public boolean isSelectionMode() {
        return selectionMode;
    }

    @Override
    public void notifySelectedItemsChange() {

    }

    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
    }

    public List<ProductItem> getSelectedItems(){
        return selectedProductItems;
    }

}
