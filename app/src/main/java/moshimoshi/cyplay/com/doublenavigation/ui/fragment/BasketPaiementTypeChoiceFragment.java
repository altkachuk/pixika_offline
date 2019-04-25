package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStep;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentType;
import moshimoshi.cyplay.com.doublenavigation.model.events.PaymentStepEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.PaymentTypeAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.PaymentSummaryLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;

/**
 * Created by romainlebouc on 24/11/2016.
 */

public class BasketPaiementTypeChoiceFragment extends BaseFragment implements IViewPagerFragment{

    @Inject
    EventBus bus;

    @Inject
    PaymentContext paymentContext;

    @BindView(R.id.payment_type_choice_recyclerview)
    RecyclerView paymentTypeChoiceRecyclerView;

    @BindView(R.id.basket_summary)
    PaymentSummaryLayout basketSummary;

    @BindView(R.id.payment_progress_bar)
    ProgressBar paymentProgressBar;

    // TODO :  set in configuration
    public final static List<EPaymentType> PAYMENT_TYPE_LIST = new ArrayList<>();

    static {
        PAYMENT_TYPE_LIST.add(EPaymentType.CARD_BANK);
    }

    private PaymentTypeAdapter paymentTypeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_paiement_type_choice, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        paymentTypeAdapter = new PaymentTypeAdapter(this.getContext());
        initRecyclerView();
        paymentTypeAdapter.setItems(PAYMENT_TYPE_LIST);
        //paymentTransactionPresenter.setView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bus != null) {
            bus.register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bus != null) {
            bus.unregister(this);
        }
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        // set layout
        paymentTypeChoiceRecyclerView.setLayoutManager(layoutManager);
        // optimization
        paymentTypeChoiceRecyclerView.setHasFixedSize(true);
        // Add Decorator spacing between items
        paymentTypeChoiceRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        // Link view with Adapter
        paymentTypeChoiceRecyclerView.setAdapter(paymentTypeAdapter);

        paymentTypeChoiceRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                EPaymentType paymentType = PAYMENT_TYPE_LIST.get(position);


                switch (paymentType) {
                    case CARD_BANK:

                        BasketPaiementTypeChoiceFragment.this.bus.post(new PaymentStepEvent(EPaymentStep.BASKET_PAYMENT));


                        break;
                    default:
                }

            }
        }));


    }

    @Override
    public void onFragmentSelected(boolean displayDeliveryFee) {

    }
}
