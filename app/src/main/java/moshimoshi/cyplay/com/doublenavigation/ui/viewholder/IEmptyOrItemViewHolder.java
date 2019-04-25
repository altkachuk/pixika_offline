package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;

/**
 * Created by romainlebouc on 10/06/16.
 */
public abstract class IEmptyOrItemViewHolder<Item> extends ItemViewHolder<Item> {

    public IEmptyOrItemViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void setItem(Item item);

}
