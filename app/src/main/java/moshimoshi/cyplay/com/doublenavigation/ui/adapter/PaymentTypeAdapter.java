package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentType;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.PaymentTypeViewHolder;

/**
 * Created by romainlebouc on 25/11/2016.
 */

public class PaymentTypeAdapter extends ItemAdapter<EPaymentType, PaymentTypeViewHolder> {

    public PaymentTypeAdapter(Context context) {
        super(context);
    }

    @Override
    public PaymentTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_payment_type, parent, false);
        return new PaymentTypeViewHolder(v);
    }

}
