package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.IEmptyOrItemViewHolder;


/**
 * Created by romainlebouc on 10/06/16.
 */
public abstract class EmptyItemAdapter<Item, T extends IEmptyOrItemViewHolder> extends RecyclerView.Adapter<T> {

    protected List<Item> items;

    public static int ITEM_POSTION = 0;
    public static int EMPTY_POSITION = 1;

    public abstract int getItemToDisplayCount();

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items){
        this.items = items;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (getItemToDisplayCount() < 0 ){
            return getItems() != null ? getItems().size() : 0;
        }else{
            return getItems() != null ? Math.max(getItems().size(), getItemToDisplayCount()) : 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItems() == null || position >= getItems().size()) {
            return EMPTY_POSITION;
        } else {
            return ITEM_POSTION;
        }
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        if ( getItems() != null && position< getItems().size() ){
            Item item = getItems().get(position);
            holder.setItem(item);
        }else{
            holder.setItem(null);
        }

    }


}
