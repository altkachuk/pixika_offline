package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStatus;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerPaymentActivity;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;

import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_CUSTOMER_PAYMENT;

/**
 * Created by wentongwang on 20/03/2017.
 */

public class PaymentsViewHolder extends ItemViewHolder<Payment> {

    @Inject
    ConfigHelper configHelper;

    @BindView(R.id.payment_total)
    TextView total;

    @BindView(R.id.payment_status)
    TextView status;

    @BindView(R.id.payment_date)
    TextView paymentDate;

    final DateFormat dateFormat;
    Payment payment;
    final String fragmentTag;

    public PaymentsViewHolder(View itemView, String fragmentTag) {
        super(itemView);
        this.fragmentTag = fragmentTag;
        dateFormat = DateFormat.getDateTimeInstance(
                DateFormat.DEFAULT,
                DateFormat.DEFAULT,
                context.getResources().getConfiguration().locale);
    }

    @Override
    public void setItem(Payment payment) {
        this.payment = payment;
        EPaymentStatus ePaymentStatus = EPaymentStatus.getEPaymentStatusFromCode(payment.getStatus());
        if (ePaymentStatus != null) {
            status.setText(ePaymentStatus.getLabelId());
        } else {
            status.setText("");
        }

        Date creationDate = payment.getCreation_date();
        if (creationDate != null) {
            paymentDate.setText(dateFormat.format(creationDate));
        } else {
            paymentDate.setText("");
        }

        total.setText(configHelper.formatPrice(payment.getTotal()));

    }

    @OnClick(R.id.customer_payment_item)
    public void onPaymentClick() {

        Intent intent = new Intent(context, CustomerPaymentActivity.class);
        intent.putExtra(EXTRA_CUSTOMER_PAYMENT, Parcels.wrap(payment));
        context.startActivity(intent);

        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if (fragment != null && fragment.isVisible()) {
                fragment.getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
            }
        }

    }

}
