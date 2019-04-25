package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;

/**
 * Created by romainlebouc on 12/06/16.
 */
public abstract class ItemViewHolder<Item> extends RecyclerView.ViewHolder {

    protected Context context;

    public ItemViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        PlayRetailApp.get(this.context).inject(this);
        ButterKnife.bind(this, itemView);
    }

    public abstract void setItem(Item item);


}
