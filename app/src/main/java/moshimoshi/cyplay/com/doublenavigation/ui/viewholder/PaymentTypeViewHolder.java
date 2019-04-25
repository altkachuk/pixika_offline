package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentType;

/**
 * Created by romainlebouc on 25/11/2016.
 */

public class PaymentTypeViewHolder  extends ItemViewHolder<EPaymentType>{

    @BindView(R.id.payment_type_icon)
    ImageView icon;

    @BindView(R.id.payment_type_label)
    TextView label;


    private EPaymentType paymentType;

    public PaymentTypeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setItem(EPaymentType paymentType) {
        this.paymentType = paymentType;
        if ( paymentType.getDrawableId() >0){
            this.icon.setImageResource( paymentType.getDrawableId());
        }
        this.label.setText(paymentType.getLabelId());
    }




}
