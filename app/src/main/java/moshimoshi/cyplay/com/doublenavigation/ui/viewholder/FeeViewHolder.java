package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Fee;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EFeeType;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;


/**
 * Created by romainlebouc on 30/01/2017.
 */

public class FeeViewHolder extends ItemViewHolder<Fee> {

    @Inject
    ConfigHelper configHelper;

    @BindView(R.id.fee_name)
    TextView feeName;

    @BindView(R.id.fee_value)
    TextView feeValue;

    protected Fee fee;

    public FeeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(Fee fee) {
        this.fee = fee;
        EFeeType eFeeType = EFeeType.valueFromTag(fee.getTag());
        if (eFeeType != null) {
            feeName.setText(eFeeType.getLabelId());
        } else {
            feeName.setText(fee.getTag());
        }

        feeValue.setText(configHelper.formatPrice(fee.getValue()));
    }
}
