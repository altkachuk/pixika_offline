package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStep;
import moshimoshi.cyplay.com.doublenavigation.model.events.PaymentStepEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;

/**
 * Created by romainlebouc on 03/03/2017.
 */

public class PurchaseCollectionChoiceFragment extends BaseFragment implements IViewPagerFragment{

    @Inject
    protected EventBus bus;

    @Inject
    PaymentContext paymentContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_purchase_collection_choice, container, false);
    }

    @OnClick(R.id.purchase_collection_choice)
    public void onChosePurchaseCollection() {
        bus.post(new PaymentStepEvent(EPaymentStep.BASKET_DELIVERY_CHOICE));
    }

    @Override
    public void onFragmentSelected(boolean displayDeliveryFee) {

    }
}
