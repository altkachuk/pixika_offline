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
import android.widget.AdapterView;
import android.widget.Spinner;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.greenrobot.eventbus.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.TicketFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.TicketFilterValue;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetSalesHistoryPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FullScreenFilteringActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerTicketsAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ResourceSortSpinnerAdapter;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilter;
import ninja.cyplay.com.apilibrary.models.filters.ResourceFilterValue;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by damien on 13/04/16.
 */
public class CustomerSalesHistoryFragment extends ResourceBaseFragment<List<Ticket>> implements ResourceView<List<Ticket>> , DisplayEventFragment{

    @Inject
    CustomerContext customerContext;

    @Inject
    GetSalesHistoryPresenter salesHistoryPresenter;

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.customer_sales_history_recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.resource_sort_spinner)
    Spinner salesSortSpinner;

    @Nullable
    @BindView(R.id.resource_sort_filter_view)
    View sortFilterView;

    private CustomerTicketsAdapter adapter;

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
        // Set presenter's view
        salesHistoryPresenter.setView(this);
        // fillStocks Adapter
        adapter = new CustomerTicketsAdapter(this.getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_sales_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.getCachedResource() == null) {
            loadResource();
        }
        adapter.setItems(new ArrayList<Ticket>());
        initRecyclerView();
        initSorting();
        if (sortFilterView != null) {
            sortFilterView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void applySort(List<Ticket> tickets) {
        ResourceFieldSorting resourceFieldSorting;
        if (salesSortSpinner != null) {
            resourceFieldSorting = (ResourceFieldSorting) salesSortSpinner.getSelectedItem();
        } else {
            resourceFieldSorting = configHelper.getCustomerSalesHistorySorting().get(0);
        }
        if (resourceFieldSorting != null && resourceFieldSorting.getComparator() != null && tickets != null) {
            Collections.sort(tickets, resourceFieldSorting.getComparator());
            customerContext.setSales(tickets);
        }
        adapter.setItems(tickets);

    }

    private void initSorting() {
        if (salesSortSpinner != null) {
            // Create adapter
            final ResourceSortSpinnerAdapter spinnerArrayAdapter = new ResourceSortSpinnerAdapter<>(this.getContext(),
                    configHelper.getCustomerSalesHistorySorting());
            salesSortSpinner.setAdapter(spinnerArrayAdapter);
            salesSortSpinner.setSelection(0, false);
            salesSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    applySort(adapter.getItems());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }

    private void initRecyclerView() {
        // Setup expandable feature and RecyclerView
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        expMgr.setDefaultGroupsExpandedState(true);

        adapter = new CustomerTicketsAdapter(this.getContext());
        recyclerView.setAdapter(expMgr.createWrappedAdapter(adapter));

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(recyclerView);
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

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @Optional
    @OnClick(R.id.reload_button)
    public void onRetrySearchCustomers() {
        runSalesHistoryRequest();
    }

    @Override
    protected void setResource(List<Ticket> tickets) {
        if (this.rawTickets == null) {
            this.rawTickets = tickets;
            this.filteredTickets = tickets;
        }
        applySort(filteredTickets);
        if (sortFilterView != null) {
            sortFilterView.setVisibility(filteredTickets != null && !filteredTickets.isEmpty() ? View.VISIBLE : View.GONE);
        }
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

    @Optional
    @OnClick(R.id.filter_button_text_container)
    public void onFiltersClick() {
        Intent intent = new Intent(getContext(), FullScreenFilteringActivity.class);

        intent.putExtra(IntentConstants.EXTRA_RESOURCES_FILTERS, Parcels.wrap(availableFilters));
        intent.putExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS, Parcels.wrap(activeFilters));
        intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_COUNT, filteredTickets != null ? filteredTickets.size() : 0);
        intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_RESOURCE, Ticket.API_RESOURCE_NAME);
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

