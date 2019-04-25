package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import org.parceler.Parcels;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.PaymentSummaryFragment;

import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_CUSTOMER_PAYMENT;

/**
 * Created by wentongwang on 20/03/2017.
 */

public class CustomerPaymentActivity extends BaseActivity {

    private Payment payment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);
        initData();
    }

    @Override
    protected boolean isLeftCrossClosable() {
        return true;
    }

    private void initData() {
        if (getIntent() != null) {
            payment = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CUSTOMER_PAYMENT));
            initFragment();
        }
    }

    private void initFragment() {
        Bundle date = new Bundle();
        date.putParcelable(EXTRA_CUSTOMER_PAYMENT, Parcels.wrap(payment));

        PaymentSummaryFragment fragment = new PaymentSummaryFragment();
        fragment.setArguments(date);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.payment_summary_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishCustomerPayment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void finishCustomerPayment() {
        supportFinishAfterTransition();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
    }

}
