package moshimoshi.cyplay.com.doublenavigation.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentsPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerPaymentsAdapter;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;


/**
 * Created by wentongwang on 20/03/2017.
 */

public class CustomerPaymentsFragment extends ResourceBaseFragment<List<Payment>> implements ResourceView<List<Payment>>, DisplayEventFragment {

    private List<Payment> paymentList;

    @Inject
    CustomerContext customerContext;

    @Inject
    PaymentsPresenter paymentsPresenter;

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.customer_payments_recycler_view)
    RecyclerView paymentsRecyclerView;

    private CustomerPaymentsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set presenter view
        paymentsPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_payments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // fillStocks RecyclerView
        initPaymentsRecyclerView();
        // id products
        loadResource();
    }

    private void initPaymentsRecyclerView() {


        // Setup expandable feature and RecyclerView
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        paymentsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        expMgr.setDefaultGroupsExpandedState(true);

        adapter = new CustomerPaymentsAdapter(this);
        paymentsRecyclerView.setAdapter(expMgr.createWrappedAdapter(adapter));

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) paymentsRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(paymentsRecyclerView);

    }

    @Subscribe
    @Override
    public void onResourceResponseEvent(ResourceResponseEvent<List<Payment>> resourceResponseEvent) {
        if (EResourceType.PAYMENT == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Subscribe
    @Override
    public void onResourceRequestEvent(ResourceRequestEvent<List<Payment>> resourceRequestEvent) {
        if (EResourceType.PAYMENT == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    @Override
    protected void setResource(List<Payment> payments) {
        this.paymentList = payments;
        adapter.setPayments(payments);
    }

    @Override
    public List<Payment> getCachedResource() {
        return paymentList;
    }

    @Override
    public void loadResource() {
        paymentsPresenter.getCustomerPayments(customerContext.getCustomerId(),
                Payment.CUSTOMER_PAYMENT_PREVIEW_PROJECTION);
    }


    @Override
    public void onResourceViewResponse(ResourceResponseEvent<List<Payment>> resourceResponseEvent) {
        resourceResponseEvent.getEResourceType();
        bus.post(resourceResponseEvent);
    }

    @Override
    public void logEvent() {
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.CUSTOMER)
                .setAttribute(EResourceAttribute.PAYMENTS.getCode())
                .setValue(customerContext !=null ? customerContext.getCustomerId() : null)
                .setStatus(EActionStatus.SUCCESS));
    }
}
