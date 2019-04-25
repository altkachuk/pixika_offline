package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerSearchPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerPreviewAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.business.EScanFilters;
import ninja.cyplay.com.apilibrary.utils.ActivityHelper;
import ninja.cyplay.com.apilibrary.utils.Constants;

/**
 * Created by damien on 02/05/16.
 */
public class CustomerSearchActivity extends ResourceBaseActivity<List<Customer>> implements ResourceView<List<Customer>> {

    @Inject
    CustomerContext customerContext;

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerSearchPresenter customerSearchPresenter;

    @BindView(R.id.customer_search)
    EditText customerSearch;

    @BindView(R.id.customer_search_results)
    RecyclerView customersRecyclerView;

    @BindView(R.id.customer_search_button)
    View customerSearchButton;

    @BindView(R.id.customer_search_error)
    TextView customerSearchError;

    @BindView(R.id.customer_search_error_container)
    View customerSearchErrorContainer;

    protected List<Customer> customers;

    CustomerPreviewAdapter customerPreviewAdapter;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search);
        updateDesign();
        initPresenters();
        initAdapter();
        initPager();
        initListeners();
    }

    @Override
    protected void setResource(List<Customer> customers) {
        customerPreviewAdapter.setItems(customers);
    }

    @Override
    public void onResourceViewResponse(ResourceResponseEvent<List<Customer>> resourceResponseEvent) {
        this.customers = resourceResponseEvent.getResource();
        if (EResourceType.CUSTOMERS == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Override
    public List<Customer> getCachedResource() {
        return customers;
    }

    @Override
    public void loadResource() {
        searchCustomer();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_SEARCH_CLIENT.getCode();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void updateDesign() {
        EditText[] editTexts = {customerSearch};
        for (EditText editText : editTexts) {
            for (Drawable drawable : editText.getCompoundDrawables()) {

                if (drawable != null) {
                    CompatUtils.setDrawableTint(drawable, ContextCompat.getColor(this, R.color.search_gray));
                }

            }
        }
        CompatUtils.setDrawableTint(customerSearchButton.getBackground(), ContextCompat.getColor(this, R.color.colorAccent));
        customerSearchError.setText(this.getResources().getQuantityString(R.plurals.customer_search_fields_count_warning, 2, 2));
    }

    private void initPresenters() {
        // Set presenter's view
        customerSearchPresenter.setView(this);
    }

    private void initAdapter() {
        customerPreviewAdapter = new CustomerPreviewAdapter(this, R.layout.cell_customer_search);
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    private class CustomerSearchItemClick implements RecyclerItemClickListener.OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            if (CustomerSearchActivity.this.customers != null && CustomerSearchActivity.this.customers.size() > position) {
                Intent intent = new Intent(CustomerSearchActivity.this, CustomerActivity.class);
                customerContext.setCustomerId(CustomerSearchActivity.this.customers.get(position).getId());
                startActivity(intent);
            }
        }
    }

    protected void initPager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // optimization
        customersRecyclerView.setHasFixedSize(true);
        // set layout
        customersRecyclerView.setLayoutManager(layoutManager);
        customersRecyclerView.setAdapter(customerPreviewAdapter);

        // add onclick listener
        customersRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new CustomerSearchItemClick()));
    }

    private void initListeners() {
        // ic_filter_check fields when focus is lost
        customerSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //this if condition is true when edittext lost focus...
                if (!hasFocus)
                    doCheck(customerSearch, 3, getString(R.string.client_search_first_name), false);
            }
        });
        // Search on keyboard's icon click
        customerSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchCustomer();
                    return true;
                }
                return false;
            }
        });
    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public void showLoading() {

    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    private boolean isSearching = false;

    private void searchCustomer() {
        customerPreviewAdapter.setItems(new ArrayList<Customer>());
        if (validateForm()) {
            ActivityHelper.hideSoftKeyboard(this);
            super.onResourceRequest(new ResourceRequestEvent<List<Customer>>(EResourceType.CUSTOMERS));
            customerSearchPresenter.searchCustomer(customerSearch.getText().toString());
        }
    }

    private boolean validateForm() {

        boolean result = true;

        int countFilledField = 0;
        countFilledField += customerSearch.getText().toString().length() > 0 ? 1 : 0;

        // False ==> we do not accept two only two filled fields but could come back one day...
        if (false && countFilledField < 2) {
            customerSearchErrorContainer.setVisibility(View.VISIBLE);
            result = false;
        } else {
            customerSearchErrorContainer.setVisibility(View.GONE);

            result = doCheck(customerSearch, 3, getString(R.string.client_search_first_name), false) && result;
        }


        return result;
    }

    private boolean doCheck(EditText editText, int minimum, String error, boolean acceptEmpty) {
        Boolean ret = editText.getText().toString().length() >= minimum;
        if (acceptEmpty) {
            ret = ret || editText.getText().toString().isEmpty();
        }
        editText.setError(ret ? null : getString(R.string.client_search_generic_error, error, minimum));
        return ret;
    }

    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------

    @OnClick({R.id.reload_button, R.id.customer_search_button})
    public void onRetrySearchCustomers() {
        searchCustomer();
    }

    @OnClick(R.id.customer_search_clear)
    public void onClearCustomerSearch() {
        this.customerSearch.setText(StringUtils.EMPTY_STRING);
    }

}
