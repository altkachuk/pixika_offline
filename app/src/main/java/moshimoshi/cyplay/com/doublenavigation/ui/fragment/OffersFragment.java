package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerOfferState;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.IOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Offer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.OfferFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.OfferFilterValue;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.meta.ResourceSorting;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.OffersPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FullScreenFilteringActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.EmptyItemAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ResourceSortSpinnerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.EmptyViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.IEmptyOrItemViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.ShopOfferViewHolder;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;
import ninja.cyplay.com.apilibrary.utils.Constants;

/**
 * Created by romainlebouc on 02/09/16.
 */
public class OffersFragment extends ResourceBaseFragment<List<IOffer>> implements DisplayEventFragment {

    private final static String RAW_OFFERS_SAVED_STATE = "RAW_OFFERS";

    @Inject
    CustomerContext customerContext;

    @Inject
    OffersPresenter offersPresenter;

    @Inject
    GetCustomerInfoPresenter getCustomerInfoPresenter;

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.shop_offers_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.resource_sort_filter_view)
    View sortFilterView;

    @BindView(R.id.resource_sort_spinner)
    Spinner offerSortSpinner;

    @BindView(R.id.filter_button_text)
    TextView filterButtonText;

    private List<IOffer> rawOffers;
    private List<IOffer> filteredOffers;

    private List<OfferFilter> availableFilters;
    private List<OfferFilter> activeFilters = new ArrayList<>();

    private EmptyItemAdapter<IOffer, IEmptyOrItemViewHolder<IOffer>> offersAdapter = new EmptyItemAdapter<IOffer, IEmptyOrItemViewHolder<IOffer>>() {
        @Override
        public int getItemToDisplayCount() {
            int columnCount = OffersFragment.this.getContext().getResources().getInteger(R.integer.offers_columns_count);
            return ((this.getItems() != null ? this.getItems().size() : 0) / columnCount) * columnCount;
        }

        @Override
        public IEmptyOrItemViewHolder<IOffer> onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_POSTION) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_grid_shop_offer, parent, false);
                return new ShopOfferViewHolder(v);
            } else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_thumbnail_empty, parent, false);
                return new EmptyViewHolder(v);
            }

        }
    };

    private Boolean shopOffersOnly = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set presenter's view
        if (savedInstanceState != null) {
            rawOffers = Parcels.unwrap(savedInstanceState.getParcelable(RAW_OFFERS_SAVED_STATE));
        }

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
        return inflater.inflate(R.layout.fragment_shop_offers, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        offersAdapter.setItems(new ArrayList<IOffer>());
        initRecyclerView();
        initSortSpinner();
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
                            offers.add(customerOfferState.getOffer());
                        }
                        setResource(offers);
                        // to notify resource Fragment
                        bus.post(new ResourceResponseEvent<>(customer.getOffers().getItems(), null, EResourceType.OFFERS));
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

    private void applySort() {
        ResourceFieldSorting resourceFieldSorting = (ResourceFieldSorting) offerSortSpinner.getSelectedItem();
        if (resourceFieldSorting.getComparator() != null && filteredOffers != null) {
            Collections.sort(filteredOffers, resourceFieldSorting.getComparator());
        }
        this.offersAdapter.setItems(filteredOffers);
    }

    private void initSortSpinner() {
        // Create adapter
        final ResourceSortSpinnerAdapter spinnerArrayAdapter = new ResourceSortSpinnerAdapter<>(getContext(),
                ResourceSorting.OFFER_SORTING.getFields());
        // Set adapter
        offerSortSpinner.setAdapter(spinnerArrayAdapter);
        offerSortSpinner.setSelection(0, false);
        offerSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmptyItemAdapter<Offer, ShopOfferViewHolder> adapter = (EmptyItemAdapter<Offer, ShopOfferViewHolder>) recyclerView.getAdapter();
                applySort();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.offers_columns_count));
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(offersAdapter);
        // Add 8dp spacing between items
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getResources().getInteger(R.integer.offers_columns_count), spacing, true));
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void setResource(List<IOffer> offers) {
        if (this.rawOffers == null) {
            this.rawOffers = offers;
            this.filteredOffers = offers;
        }

        sortFilterView.setVisibility(filteredOffers != null && !filteredOffers.isEmpty() ? View.VISIBLE : View.GONE);
        applySort();

        availableFilters = getOfferFilters();
    }

    public List<OfferFilter> getOfferFilters() {
        List<OfferFilter> offerFilters = new ArrayList<>();

        List<OfferFilterValue> offerFilterValues = new ArrayList<>();

        for (EOfferType type : EOfferType.values()) {
            int count = Offer.getOfferCountByType(filteredOffers, type);
            if (count > 0) {
                OfferFilterValue offerFilterValue = new OfferFilterValue();
                offerFilterValue.setKey(type.getCode());
                offerFilterValue.setLabel(this.getString(EOfferType.getEOfferTypeFromCode(type.getCode()).getLabelId()));
                offerFilterValue.setCount(count);
                offerFilterValues.add(offerFilterValue);
            }
        }

        if (!offerFilterValues.isEmpty()) {
            OfferFilter offerFilter = new OfferFilter();
            offerFilter.setKey("type");
            offerFilter.setLabel(this.getString(R.string.offer_type));
            offerFilter.setLevel(ResourceFilter.TYPE_LEVEL_ATTRIBUTE);
            offerFilter.setValues(offerFilterValues);
            offerFilters.add(offerFilter);
        }

        return offerFilters;
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

    @OnClick(R.id.filter_button_text_container)
    public void onFiltersClick() {
        Intent intent = new Intent(getContext(), FullScreenFilteringActivity.class);

        intent.putExtra(IntentConstants.EXTRA_RESOURCES_FILTERS, Parcels.wrap(availableFilters));
        intent.putExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS, Parcels.wrap(activeFilters));
        intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_COUNT, filteredOffers != null ? filteredOffers.size() : 0);
        intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_RESOURCE, Offer.API_RESOURCE_NAME);
        intent.putExtra(IntentConstants.EXTRA_FILTER_MODE, IntentConstants.SINGLE_CHOICE_FILTER);

        startActivityForResult(intent, IntentConstants.REQUEST_ITEMS_FILTERS);
        getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == IntentConstants.REQUEST_ITEMS_FILTERS) {
            // Make sure the request was successful
            if (resultCode == IntentConstants.RESULT_ITEMS_FILTERS) {
                // The user picked a contact.
                activeFilters = Parcels.unwrap(data.getParcelableExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS));
                filteredOffers = OfferFilter.filterResources(rawOffers, (List<ResourceFilter<ResourceFilter, ResourceFilterValue>>) (List<?>) activeFilters);

                int activeFilterSize = activeFilters != null ? activeFilters.size() : 0;
                String buttonText;
                if (activeFilterSize == 0) {
                    buttonText = this.getString(R.string.filter);
                } else {
                    buttonText = this.getResources().getString(R.string.filter_count, activeFilterSize);
                }
                filterButtonText.setText(buttonText);

                applySort();
                // The Intent's data Uri identifies which contact was selected.
                // Do something with the contact here (bigger example below)
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
