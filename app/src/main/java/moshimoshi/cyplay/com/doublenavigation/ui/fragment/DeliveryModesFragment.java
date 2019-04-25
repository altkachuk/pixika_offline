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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.PaymentDeliveryItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketDeliveryItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.DeliveryMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPaymentStep;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPurchaseCollectionMode;
import moshimoshi.cyplay.com.doublenavigation.model.events.PaymentStepEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.DeliveryModesExpandableAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BasketSummaryLayout;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.WSErrorDialog;
import moshimoshi.cyplay.com.doublenavigation.view.BasketView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;


/**
 * Created by wentongwang on 27/03/2017.
 */

public class DeliveryModesFragment extends BaseFragment implements IViewPagerFragment {

    @Inject
    PaymentPresenter paymentPresenter;

    @Inject
    PaymentContext paymentContext;

    @Inject
    BasketPresenter basketPresenter;

    @BindView(R.id.basket_delivery_mode_recyclerview)
    RecyclerView addressRecyclerView;

    @BindView(R.id.loading_view)
    LoadingView loadingView;

    @BindView(R.id.basket_summary)
    BasketSummaryLayout basketSummaryLayout;

    private DeliveryModesExpandableAdapter adapter;
    private boolean displayDeliveryFee=true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery_modes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initPresenter();
        initRecyclerView();
    }

    private void initPresenter() {
        basketPresenter.setView(new BasketView() {
            @Override
            public void onStockResponse(BasketItem basketItem, ResourceResponseEvent<Product> resourceResponseEvent) {

            }

            @Override
            public void onBasketItemsStocksChecked(boolean success) {

            }

            @Override
            public void onResourceViewResponse(ResourceResponseEvent<Basket> resourceResponseEvent) {
                loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
                if (resourceResponseEvent.getResourceException() == null) {
                    updateBasketSummary();
                } else {
                    //show error dialog
                    WSErrorDialog.showWSErrorDialog(getContext(),
                            getActivity().getString(R.string.delivery_update_error) + "\n" + resourceResponseEvent.getResourceException().toString(),
                            new WSErrorDialog.DialogButtonClickListener() {
                                @Override
                                public void retry() {
                                    loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
                                    basketPresenter.updateDeliveryFees(updateBasketDelivery());
                                }

                                @Override
                                public void cancel() {
                                    getActivity().finish();
                                }
                            });
                }

            }

            @Override
            public void onValidateCartResponse() {

            }
        });

        paymentPresenter.setView(new ResourceView<Payment>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<Payment> resourceResponseEvent) {
                loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
                if (resourceResponseEvent.getResourceException() == null) {
                    bus.post(new PaymentStepEvent(EPaymentStep.BASKET_DELIVERY_MODES));

                } else {
                    //show error dialog
                    WSErrorDialog.showWSErrorDialog(getContext(),
                            getActivity().getString(R.string.delivery_update_error) + "\n" + resourceResponseEvent.getResourceException().toString(),
                            new WSErrorDialog.DialogButtonClickListener() {
                                @Override
                                public void retry() {
                                    onClickOk();
                                }

                                @Override
                                public void cancel() {
                                    getActivity().finish();
                                }
                            });

                }
            }
        });
    }


    private void initRecyclerView() {
        // Setup expandable feature and RecyclerView
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        expMgr.setDefaultGroupsExpandedState(true);

        adapter = new DeliveryModesExpandableAdapter();

        adapter.setOnDeliveryModeChangedListener(new DeliveryModesExpandableAdapter.OnDeliveryModeChangedListener() {
            @Override
            public void onDeliveryModeChanged(DeliveryMode deliveryMode) {
                loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
                basketPresenter.updateDeliveryFees(updateBasketDelivery());
            }
        });


        addressRecyclerView.setAdapter(expMgr.createWrappedAdapter(adapter));

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) addressRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(addressRecyclerView);
    }

    private void updateBasketSummary() {
        Basket basket = basketPresenter.getCachedBasket();
        if (basket != null) {
            basketSummaryLayout.setItem(basket, displayDeliveryFee);
        }
    }

    @Override
    public void onFragmentSelected(boolean displayDeliveryFee) {
        this.displayDeliveryFee= displayDeliveryFee;
        if (paymentContext != null) {
            adapter.init(paymentContext.getBasketEPurchaseCollectionModes());
            adapter.fillDeliveryModes(paymentContext.getAvailableDeliveryModes());

            loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
            basketPresenter.updateDeliveryFees(updateBasketDelivery());
        }
    }

    @OnClick(R.id.delivery_mode_ok)
    public void onClickOk() {

        loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        Payment payment = new Payment();
        List<PaymentDeliveryItem> deliveryItems = new ArrayList<>();
        for (EPurchaseCollectionMode ePurchaseCollectionMode : paymentContext.getBasketEPurchaseCollectionModes()) {
            PaymentDeliveryItem paymentDeliveryItem = new PaymentDeliveryItem();
            PurchaseCollection purchaseCollection = new PurchaseCollection();
            purchaseCollection.setType(ePurchaseCollectionMode);
            purchaseCollection.setDeliveryMode(adapter.getSelectedDeliveryModeByType(ePurchaseCollectionMode));
            switch (ePurchaseCollectionMode) {
                case SHOP_NOW:
                    purchaseCollection.setShop_id(configHelper.getCurrentShop().getId());
                    break;
                case SHOP_DELIVERY:
                    purchaseCollection.setShop_id(configHelper.getCurrentShop().getId());

                    purchaseCollection.setBilling_address_id(paymentContext.getMainBillingAddress().getId());
                    break;
                case HOME_DELIVERY:
                    purchaseCollection.setShipping_address_id(paymentContext.getMainShippingAddress().getId());
                    purchaseCollection.setBilling_address_id(paymentContext.getMainBillingAddress().getId());
                    break;

            }
            paymentDeliveryItem.setDelivery(purchaseCollection);
            deliveryItems.add(paymentDeliveryItem);
        }

        payment.setDelivery_items(deliveryItems);
        paymentPresenter.addCustomerPayment(paymentContext.getCustomerId(), payment);
    }


    private Basket updateBasketDelivery() {
        Basket basket = basketPresenter.getCachedBasket();

        List<BasketDeliveryItem> basketDeliveryItems = new ArrayList<>();
        for (EPurchaseCollectionMode ePurchaseCollectionMode : paymentContext.getBasketEPurchaseCollectionModes()) {
            BasketDeliveryItem basketDeliveryItem = new BasketDeliveryItem();
            basketDeliveryItem.getDelivery().setType(ePurchaseCollectionMode.getCode());
            basketDeliveryItem.getDelivery().setDelivery_mode_id(adapter.getSelectedDeliveryModeByType(ePurchaseCollectionMode).getId());
            switch (ePurchaseCollectionMode) {
                case SHOP_NOW:
                case SHOP_DELIVERY:
                    break;
                case HOME_DELIVERY:
                    basketDeliveryItem.getDelivery().setShipping_address_id(paymentContext.getMainShippingAddress().getId());
                    break;
            }
            basketDeliveryItems.add(basketDeliveryItem);
        }

        basket.setDelivery_items(basketDeliveryItems);
        return basket;
    }

}
