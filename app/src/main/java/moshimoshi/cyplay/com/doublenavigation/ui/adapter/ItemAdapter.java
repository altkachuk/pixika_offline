package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ItemViewHolder;

/**
 * Created by romainlebouc on 12/06/16.
 */
public abstract class ItemAdapter<Item, T extends ItemViewHolder<Item>> extends RecyclerView.Adapter<T> {

    protected List<Item> items;
    protected Context context;

    public ItemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        Item item = items.get(position);
        if (item != null) {
            holder.setItem(item);

        }
    }

    // -------------------------------------------------------------------------------------------
    //                             Manipulate data with animation
    // -------------------------------------------------------------------------------------------

    public void setItems(List<Item> items){
        this.items = items;
        this.notifyDataSetChanged();
    }

    public void addRangeItems(List<Item> rangeItems) {
        if (items == null)
            items = new ArrayList<>();
        if (rangeItems != null) {
            int pos = items.size();
            items.addAll(rangeItems);
            notifyItemRangeInserted(pos, items.size());
        }
    }

    public void clearItems() {
        if (items != null && items.size() > 0) {
            int oldsize = items.size();
            items.clear();
            notifyItemRangeRemoved(0, oldsize);
        }
    }

    public List<Item> getItems() {
        return items;
    }
}
