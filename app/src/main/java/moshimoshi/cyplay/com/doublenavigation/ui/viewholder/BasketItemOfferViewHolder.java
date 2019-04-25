package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItemOffer;
import moshimoshi.cyplay.com.doublenavigation.model.events.UpdateCustomerOfferStatusEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.OfferItemCore;

/**
 * Created by wentongwang on 07/03/2017.
 */

public class BasketItemOfferViewHolder extends ItemViewHolder<BasketItemOffer> {

    @Inject
    EventBus bus;

    @BindView(R.id.offer_item_core)
    OfferItemCore offerItemCore;

    @Nullable
    @BindView(R.id.customer_offer_switch_btn)
    SwitchCompat customerOfferSwitchBtn;

    @Nullable
    @BindView(R.id.customer_offer_auto_selected_icon)
    ImageView automaticIcon;

    public BasketItemOfferViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(final BasketItemOffer iOffer) {
        offerItemCore.fillOffer(iOffer.getOffer());
        offerItemCore.hideOfferName();
        switch (iOffer.getStatus()) {
            case CHOSEN:
                automaticIcon.setVisibility(View.GONE);
                customerOfferSwitchBtn.setVisibility(View.VISIBLE);
                if (customerOfferSwitchBtn != null)
                    customerOfferSwitchBtn.setChecked(true);
                break;
            case AVAILABLE:
                automaticIcon.setVisibility(View.GONE);
                customerOfferSwitchBtn.setVisibility(View.VISIBLE);
                if (customerOfferSwitchBtn != null)
                    customerOfferSwitchBtn.setChecked(false);
                break;
            case AUTOMATIC:
                customerOfferSwitchBtn.setVisibility(View.GONE);
                automaticIcon.setVisibility(View.VISIBLE);

        }

        if (customerOfferSwitchBtn != null) {
            customerOfferSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    UpdateCustomerOfferStatusEvent event = new UpdateCustomerOfferStatusEvent(b);
                    event.setId(iOffer.getBasketItemOfferId());
                    event.setOffer_id(iOffer.getOffer().getId());
                    bus.post(event);
                }
            });
        }
    }

}
