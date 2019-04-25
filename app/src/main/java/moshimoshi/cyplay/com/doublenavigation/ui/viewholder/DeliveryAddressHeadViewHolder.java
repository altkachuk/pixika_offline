package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EAddressType;
import moshimoshi.cyplay.com.doublenavigation.model.events.BasketAddressEvent;


import static moshimoshi.cyplay.com.doublenavigation.ui.Constants.TYPE_BILLING_ADDRESS;
import static moshimoshi.cyplay.com.doublenavigation.ui.Constants.TYPE_SHIPPING_ADDRESS;

/**
 * Created by wentongwang on 22/03/2017.
 */

public class DeliveryAddressHeadViewHolder extends ItemViewHolder<String> {

    @Inject
    EventBus bus;

    @BindView(R.id.delivery_address_type)
    TextView addressType;

    public DeliveryAddressHeadViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(String s) {
        switch (s) {
            case TYPE_SHIPPING_ADDRESS:
                addressType.setText(context.getString(R.string.delivery_address));
                break;
            case TYPE_BILLING_ADDRESS:
                addressType.setText(context.getString(R.string.billing_address));
                break;
        }

    }

    @OnClick(R.id.create_new_address)
    public void createNewAddress(){
        bus.post(new BasketAddressEvent(BasketAddressEvent.CREATE_NEW_ADDRESS, EAddressType.EXTRA));
    }
}
