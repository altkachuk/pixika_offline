package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.BasketItemWithStock;


/**
 * Created by romainlebouc on 08/06/16.
 */
public abstract class IBasketCellViewHolder extends RecyclerView.ViewHolder {

    protected EventBus bus;

    public abstract void setBasketItemWithStock(BasketItemWithStock basketItemWithStock);

    public abstract void setBasketOffer(BasketOffer basketOffer);

    public IBasketCellViewHolder(View itemView) {
        super(itemView);
    }

    public void register() {
        if (bus != null) {
            bus.register(this);
        }
    }

    public void unregister() {
        if (bus != null) {
            bus.unregister(this);
        }
    }

}
