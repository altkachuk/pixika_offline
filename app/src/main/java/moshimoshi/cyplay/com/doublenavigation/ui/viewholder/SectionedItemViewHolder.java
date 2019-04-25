package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;

/**
 * Created by romainlebouc on 09/12/2016.
 */

public abstract class SectionedItemViewHolder <Item> extends RecyclerView.ViewHolder{

    public SectionedItemViewHolder(View itemView) {
        super(itemView);
        if ( itemView != null){
            PlayRetailApp.get(this.itemView.getContext()).inject(this);
        }
    }

    public abstract void setItem(Item item, int relativePosition);

}