package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EDeliveryAddressPattern;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStep;
import moshimoshi.cyplay.com.doublenavigation.model.events.BasketAddressEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.PaymentStepEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.AddressFormActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.DeliveryAddressAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketSummaryLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.NextButtonEnableHandler;
import ninja.cyplay.com.apilibrary.utils.Constants;

import static android.app.Activity.RESULT_OK;
import static moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode.HOME_DELIVERY;
import static moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode.SHOP_DELIVERY;


/**
 * Created by wentongwang on 22/03/2017.
 */

public class DeliveryAddressFragment extends BaseFragment implements IViewPagerFragment, NextButtonEnableHandler {

    @Inject
    PaymentContext paymentContext;

    @Inject
    CustomerContext customerContext;

    @BindView(R.id.basket_delivery_address_recyclerview)
    RecyclerView addressRecyclerView;

    @BindView(R.id.delivery_address_ok)
    TextView nextBtn;

    @BindView(R.id.basket_summary)
    BasketSummaryLayout basketSummary;

    private DeliveryAddressAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery_address, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();

    }

    private void initRecyclerView() {
        // Setup expandable feature and RecyclerView
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        expMgr.setDefaultGroupsExpandedState(true);

        if (paymentContext.getBasketEPurchaseCollectionModes().contains(SHOP_DELIVERY) &&
                !paymentContext.getBasketEPurchaseCollectionModes().contains(HOME_DELIVERY)) {
            //if only have shop delivery, just show the billing address
            adapter = new DeliveryAddressAdapter(this, EDeliveryAddressPattern.BILLING);
        } else {
            adapter = new DeliveryAddressAdapter(this, EDeliveryAddressPattern.SHIPPING_AND_BILLING);
        }

        addressRecyclerView.setAdapter(expMgr.createWrappedAdapter(adapter));

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) addressRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(addressRecyclerView);
    }


    @Override
    public void onFragmentSelected(boolean displayDeliveryFee) {
        basketSummary.setItem(customerContext.getBasket(), displayDeliveryFee);
        updateAddress();
    }

    private void updateAddress() {
        adapter.fillDeliveryAddress(customerContext.getCustomer());
    }


    @Subscribe
    public void onBasketDeliveryAddressEvent(BasketAddressEvent event) {

        switch (event.getType()) {
            case BasketAddressEvent.CREATE_NEW_ADDRESS:
                Intent createAddressIntent = new Intent(getContext(), AddressFormActivity.class);
                createAddressIntent.putExtra(IntentConstants.EXTRA_FORM_EDIT_MODE, Constants.FORM_SHIPPING_ADDRESS_KEY);
                if (event.geteAddressType() != null) {
                    createAddressIntent.putExtra(IntentConstants.EXTRA_FORM_ADDRESS_TYPE, event.geteAddressType().ordinal());
                }
                startActivityForResult(createAddressIntent, IntentConstants.EXTRA_FORM_EDIT_RESULT);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
                break;
            case BasketAddressEvent.UPDATE_ADDRESS:
                Intent updateAddressIntent = new Intent(getContext(), AddressFormActivity.class);
                updateAddressIntent.putExtra(IntentConstants.EXTRA_SHIPPING_ADDRESS, Parcels.wrap(event.getAddress()));
                updateAddressIntent.putExtra(IntentConstants.EXTRA_FORM_EDIT_MODE, Constants.FORM_SHIPPING_ADDRESS_KEY);
                if (event.geteAddressType() != null) {
                    updateAddressIntent.putExtra(IntentConstants.EXTRA_FORM_ADDRESS_TYPE, event.geteAddressType().ordinal());
                }
                startActivityForResult(updateAddressIntent, IntentConstants.EXTRA_FORM_EDIT_RESULT);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentConstants.EXTRA_FORM_EDIT_RESULT) {
            if (resultCode == RESULT_OK) {
                updateAddress();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected boolean isBusActivated() {
        return true;
    }

    @OnClick(R.id.delivery_address_ok)
    public void onClickOk() {

        paymentContext.setMainShippingAddress(adapter.getSelectedShippingAddress());
        paymentContext.setMainBillingAddress(adapter.getSelectedBillingAddress());

        bus.post(new PaymentStepEvent(EPaymentStep.BASKET_DELIVERY_CHOICE));
    }

    @Override
    public void setEnable(boolean enable) {
        nextBtn.setEnabled(enable);
    }

}
