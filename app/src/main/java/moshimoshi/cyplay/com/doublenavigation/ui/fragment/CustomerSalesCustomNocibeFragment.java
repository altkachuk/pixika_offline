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
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.TicketFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.TicketFilterValue;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.meta.ResourceSorting;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetSalesHistoryPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FullScreenFilteringActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductItemThumbnailAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ResourceSortSpinnerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by damien on 14/09/16.
 */
public class CustomerSalesCustomNocibeFragment extends ResourceBaseFragment<List<Ticket>> implements ResourceView<List<Ticket>>, DisplayEventFragment {

    private final static String RAW_TICKETS_SAVED_STATE = "RAW_TICKETS";

    @Inject
    CustomerContext customerContext;

    @Inject
    GetSalesHistoryPresenter salesHistoryPresenter;

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.customer_sales_history_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.resource_sort_spinner)
    Spinner salesSortSpinner;

    @BindView(R.id.resource_sort_filter_view)
    View sortFilterView;

    @BindView(R.id.filter_button_text)
    TextView filterButtonText;

    private ProductItemThumbnailAdapter adapter;

    private List<Ticket> rawTickets;
    private List<Ticket> filteredTickets;

    private List<TicketFilter> availableFilters;
    private List<TicketFilter> activeFilters = new ArrayList<>();

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set presenter's view
        salesHistoryPresenter.setView(this);
        // adapter fillStocks
        adapter = new ProductItemThumbnailAdapter(getContext(), this.getResources().getInteger(R.integer.product_search_columns_count));
        if (savedInstanceState != null) {
            rawTickets = Parcels.unwrap(savedInstanceState.getParcelable(RAW_TICKETS_SAVED_STATE));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RAW_TICKETS_SAVED_STATE, Parcels.wrap(rawTickets));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_sales_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // fillStocks Recycler view
        initRecyclerView();
        // fillStocks Sort
        initSorting();
        // Load resources
        if (this.getCachedResource() == null) {
            loadResource();
        }
        // Show sort view
        sortFilterView.setVisibility(View.VISIBLE);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    //filtering sales history

    private void initSorting() {
        // Create adapter
        final ResourceSortSpinnerAdapter spinnerArrayAdapter = new ResourceSortSpinnerAdapter<>(getContext(),
                ResourceSorting.TICKET_SORTING.getFields());
        salesSortSpinner.setAdapter(spinnerArrayAdapter);
        salesSortSpinner.setSelection(0, false);
        salesSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applySort(getCachedResource());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void applySort(List<Ticket> tickets) {
        ResourceFieldSorting resourceFieldSorting = (ResourceFieldSorting) salesSortSpinner.getSelectedItem();
        if (resourceFieldSorting.getComparator() != null && tickets != null) {
            Collections.sort(tickets, resourceFieldSorting.getComparator());
            customerContext.setSales(tickets);
        }
        adapter.setItems((List<ProductItem>) (List<?>) tickets);
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.wishlist_columns_count));
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        // Add 8dp spacing between items
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getResources().getInteger(R.integer.wishlist_columns_count), spacing, true));
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void runSalesHistoryRequest() {
        String customerId = customerContext.hasToRequestForCustomerSales();
        if (customerId != null) {
            salesHistoryPresenter.getCustomerSalesHistory(customerId);
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void onResourceViewResponse(ResourceResponseEvent<List<Ticket>> resourceResponseEvent) {
        customerContext.setSales(resourceResponseEvent.getResource());
        bus.post(resourceResponseEvent);
    }


    @Override
    protected void setResource(List<Ticket> tickets) {
        if (this.rawTickets == null) {
            this.rawTickets = tickets;
            this.filteredTickets = tickets;
        }
        applySort(filteredTickets);
        sortFilterView.setVisibility(filteredTickets != null && !filteredTickets.isEmpty() ? View.VISIBLE : View.GONE);
        availableFilters = getTicketFilters();
    }


    @Override
    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<List<Ticket>> resourceResponseEvent) {
        if (EResourceType.TICKETS == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Override
    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent<List<Ticket>> resourceRequestEvent) {
        if (EResourceType.TICKETS == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    @Override
    public List<Ticket> getCachedResource() {
        return customerContext.getSales();
    }

    @Override
    public void loadResource() {
        runSalesHistoryRequest();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.reload_button)
    public void onRetrySearchCustomers() {
        runSalesHistoryRequest();
    }

    public List<TicketFilter> getTicketFilters() {
        List<TicketFilter> ticketFilters = new ArrayList<>();

        List<TicketFilterValue> ticketFilterValues = new ArrayList<>();

        for (EShopType type : EShopType.values()) {
            int count = Ticket.getTicketCountByChannel(filteredTickets, type.getCode());
            if (count > 0) {
                TicketFilterValue ticketFilterValue = new TicketFilterValue();
                ticketFilterValue.setKey(type.getCode());
                ticketFilterValue.setLabel(this.getString(EShopType.getEShopTypeFromCode(type.getCode()).getLabelId()));
                ticketFilterValue.setCount(count);
                ticketFilterValues.add(ticketFilterValue);
            }
        }

        if (!ticketFilterValues.isEmpty()) {
            TicketFilter ticketFilter = new TicketFilter();
            ticketFilter.setKey("channel");
            ticketFilter.setLabel(this.getString(R.string.shop_type));
            ticketFilter.setLevel(ResourceFilter.TYPE_LEVEL_ATTRIBUTE);
            ticketFilter.setValues(ticketFilterValues);
            ticketFilters.add(ticketFilter);
        }

        return ticketFilters;
    }

    @OnClick(R.id.filter_button_text_container)
    public void onFiltersClick() {
        Intent intent = new Intent(getContext(), FullScreenFilteringActivity.class);

        intent.putExtra(IntentConstants.EXTRA_RESOURCES_FILTERS, Parcels.wrap(availableFilters));
        intent.putExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS, Parcels.wrap(activeFilters));
        intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_COUNT, filteredTickets != null ? filteredTickets.size() : 0);
        intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_RESOURCE, Product.API_RESOURCE_NAME);
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
                filteredTickets = TicketFilter.filterResources(rawTickets, (List<ResourceFilter<ResourceFilter, ResourceFilterValue>>) (List<?>) activeFilters);

                int activeFilterSize = activeFilters != null ? activeFilters.size() : 0;
                String buttonText;
                if (activeFilterSize == 0) {
                    buttonText = this.getString(R.string.filter);
                } else {
                    buttonText = this.getResources().getString(R.string.filter_count, activeFilterSize);
                }
                filterButtonText.setText(buttonText);
                applySort(filteredTickets);
                // The Intent's data Uri identifies which contact was selected.
                // Do something with the contact here (bigger example below)
            }
        }
    }

    @Override
    public void logEvent() {
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.CUSTOMER)
                .setAttribute(EResourceAttribute.SALES.getCode())
                .setValue(customerContext !=null ? customerContext.getCustomerId() : null)
                .setStatus(EActionStatus.SUCCESS));
    }


}