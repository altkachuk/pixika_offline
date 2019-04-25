package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.IOffer;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.OfferItemCore;

/**
 * Created by wentongwang on 07/03/2017.
 */

public class OffersViewHolder extends ItemViewHolder<IOffer> {

    @Inject
    EventBus bus;

    @BindView(R.id.offer_item_core)
    OfferItemCore offerItemCore;

    @Nullable
    @BindView(R.id.customer_offer_switch_btn)
    SwitchCompat customerOfferSwitchBtn;

    public OffersViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(IOffer iOffer) {
        offerItemCore.fillOffer(iOffer.getOffer());
        if (customerOfferSwitchBtn != null) {
            customerOfferSwitchBtn.setVisibility(View.GONE);
        }
    }

}
