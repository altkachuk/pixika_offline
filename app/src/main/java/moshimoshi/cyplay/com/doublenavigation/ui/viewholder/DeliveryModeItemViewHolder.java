package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.DeliveryMode;

/**
 * Created by wentongwang on 27/03/2017.
 */

public class DeliveryModeItemViewHolder extends ItemViewHolder<DeliveryMode> {

    @BindView(R.id.delivery_mode_shop_name)
    TextView tvName;

    @BindView(R.id.delivery_mode_description)
    TextView tvDescription;

    @BindView(R.id.check_button)
    RadioButton checkBtn;

    @BindView(R.id.delivery_mode_shipping_time)
    TextView tvShippingTime;

    @BindView(R.id.shipping_time_container)
    View shippingTimeContainer;

    @BindView(R.id.main_content_container)
    View mainContainer;

    public DeliveryModeItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(DeliveryMode deliveryMode) {
        tvName.setText(deliveryMode.getName());
        tvDescription.setText(deliveryMode.getDescription());
        if (deliveryMode.getShippingFormatTime(context).isEmpty()) {
            shippingTimeContainer.setVisibility(View.INVISIBLE);
        }
        tvShippingTime.setText(deliveryMode.getShippingFormatTime(context));
    }

    public void setOnCheckedListener(View.OnClickListener listener) {
        checkBtn.setOnClickListener(listener);
        mainContainer.setOnClickListener(listener);
    }

    public void setCheckBtn(boolean checked) {
        checkBtn.setChecked(checked);
    }
}
