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
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerOfferState;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.IOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Offer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ECustomerOfferState;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.OffersPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.OffersByTypeAdapter;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.utils.Constants;

/**
 * Created by wentongwang on 07/03/2017.
 */

public class OffersByTypeFragment extends ResourceBaseFragment<List<IOffer>> implements DisplayEventFragment {

    private final static String RAW_OFFERS_SAVED_STATE = "RAW_OFFERS";

    @Inject
    CustomerContext customerContext;

    @Inject
    OffersPresenter offersPresenter;

    @Inject
    GetCustomerInfoPresenter getCustomerInfoPresenter;

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.offers_recycler_view)
    RecyclerView recyclerView;

    private List<IOffer> rawOffers;

    private OffersByTypeAdapter adapter;

    private Boolean shopOffersOnly = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set presenter's view

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RAW_OFFERS_SAVED_STATE, Parcels.wrap(rawOffers));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.getCachedResource() == null) {
            loadResource();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // get intent params
        initPresentersView();
        if (getActivity().getIntent() != null && getActivity().getIntent().hasExtra(Constants.EXTRA_SHOP_OFFERS_ONLY)) {
            shopOffersOnly = getActivity().getIntent().getBooleanExtra(Constants.EXTRA_SHOP_OFFERS_ONLY, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offres_by_type, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initPresentersView() {
        getCustomerInfoPresenter.setView(new ResourceView<Customer>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<Customer> resourceResponseEvent) {
                if (resourceResponseEvent != null) {
                    Customer customer = resourceResponseEvent.getResource();
                    if (customer != null && customer.getOffers() != null) {
                        List<IOffer> offers = new ArrayList<>();
                        for (CustomerOfferState customerOfferState : customer.getOffers().getItems()) {
                            if (!ECustomerOfferState.USED.equals(customerOfferState.getECustomerOfferState())) {
                                offers.add(customerOfferState.getOffer());
                            }
                        }
                        setResource(offers);
                        // to notify resource Fragment
                        bus.post(new ResourceResponseEvent<>(offers, null, EResourceType.OFFERS));
                    }
                }
            }
        });
        offersPresenter.setView(new ResourceView<List<Offer>>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<List<Offer>> resourceResponseEvent) {
                bus.post(resourceResponseEvent);
            }
        });
    }


    private void initRecyclerView() {
        // Setup expandable feature and RecyclerView
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        expMgr.setDefaultGroupsExpandedState(true);

        adapter = new OffersByTypeAdapter(this.getContext());
        recyclerView.setAdapter(expMgr.createWrappedAdapter(adapter));
        recyclerView.setHasFixedSize(true);
        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(recyclerView);
    }

    @Override
    protected void setResource(List<IOffer> offers) {
        if (offers != null) {
            if (this.rawOffers == null) {
                this.rawOffers = offers;
            }

            adapter.setItems(offers);
        }

    }


    @Subscribe
    @Override
    public void onResourceResponseEvent(ResourceResponseEvent<List<IOffer>> resourceResponseEvent) {
        if (EResourceType.OFFERS == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Subscribe
    @Override
    public void onResourceRequestEvent(ResourceRequestEvent<List<IOffer>> resourceRequestEvent) {
        if (EResourceType.OFFERS == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    @Override
    public List<IOffer> getCachedResource() {
        return rawOffers;
    }

    @Override
    public void loadResource() {
        runOffersRequest();
    }

    private void runOffersRequest() {
        if (shopOffersOnly) {
            // load offers from the shop
            Shop shop = configHelper.getCurrentShop();
            if (shop != null && shop.getId() != null) {
                offersPresenter.getShopOffers(shop.getId());
            }
        } else {
            // load offers from the client
            if (customerContext != null) {
                getCustomerInfoPresenter.getCustomerOffer(customerContext.getCustomerId());
            }
        }
    }

    @Override
    public void logEvent() {
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.CUSTOMER)
                .setAttribute(EResourceAttribute.OFFERS.getCode())
                .setValue(customerContext !=null ? customerContext.getCustomerId() : null)
                .setStatus(EActionStatus.SUCCESS));
    }

}
