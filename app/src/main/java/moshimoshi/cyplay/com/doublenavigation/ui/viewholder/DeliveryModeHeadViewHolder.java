package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;

/**
 * Created by wentongwang on 27/03/2017.
 */

public class DeliveryModeHeadViewHolder extends ItemViewHolder<String> {

    @BindView(R.id.type)
    TextView tvType;

    public DeliveryModeHeadViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(String s) {
        tvType.setText(EPurchaseCollectionMode.getEDeliveryDestinationTypeFromCode(s).getLabelId());
    }
}
