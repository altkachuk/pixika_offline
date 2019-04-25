package moshimoshi.cyplay.com.doublenavigation.ui.activity.abs;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ESearchSuggestionMode;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerSearchPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.EMenuAction;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerPreviewAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.EmailSearchField;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.PhoneSearchField;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.TextSearchField;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.CustomerHistoryFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.RecyclerItemClickListener;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_ORGANIZATION_NAME;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_FIRST_NAME;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_LAST_NAME;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_SEARCH_EMAIL;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_SEARCH_PHONE;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_TEXT_SEARCH;

/**
 * Created by romainlebouc on 20/12/2016.
 */

public abstract class AbstractCustomerSearchActivity extends ResourceBaseActivity<List<Customer>> implements ResourceView<List<Customer>> {

    private CustomerPreviewAdapter customerPreviewAdapter;
    protected List<Customer> customers;

    @Inject
    ConfigHelper configHelper;

    @Inject
    SellerContext sellerContext;

    @Inject
    protected CustomerSearchPresenter customerSearchPresenter;

    @Inject
    protected CustomerContext customerContext;

    @BindView(R.id.customer_search_results)
    protected RecyclerView customersRecyclerView;

    @Nullable
    @BindView(R.id.specify_customer_search)
    TextView specifyCustomerSearch;

    @Nullable
    @BindView(R.id.text_search)
    protected TextSearchField textSearch;

    @Nullable
    @BindView(R.id.zipcode_search)
    protected TextSearchField zipcodeSearch;

    @Nullable
    @BindView(R.id.phone_search)
    protected PhoneSearchField phoneSearch;

    @Nullable
    @BindView(R.id.ean_search)
    protected TextSearchField eanSearch;

    protected RecyclerItemClickListener itemClickListener;

    @Nullable
    @BindView(R.id.customer_history_container)
    protected View customerHistoryContainer;

    protected ESearchSuggestionMode currentMode = null;

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
//        initCustomerHistory();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentMode == null) {
            initCustomerHistory();
        }
    }

    private void initCustomerHistory() {

        if (configHelper.getConfig().getFeature().getCustomerConfig().getSearch().getHistory().getEnable()) {
            if (customerHistoryContainer != null)
                customerHistoryContainer.setVisibility(View.VISIBLE);
            CustomerHistoryFragment customerHistoryFragment = new CustomerHistoryFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.show(customerHistoryFragment);
            fragmentTransaction.replace(R.id.customer_history_container, customerHistoryFragment, CustomerHistoryFragment.CUSTOMER_HISTORY_FRAGMENT_TAG);

            fragmentTransaction.commitAllowingStateLoss();
        } else {
            if (customerHistoryContainer != null)
                customerHistoryContainer.setVisibility(View.GONE);
        }

    }


    @Override
    protected void setResource(List<Customer> customers) {
        int maxResultCount = configHelper.getCustomerConfig().getSearch().getMax_results_count();

        if (specifyCustomerSearch != null) {
            specifyCustomerSearch.setText(this.getString(R.string.customer_search_specify, String.valueOf(maxResultCount)));
            specifyCustomerSearch.setVisibility(customers != null && customers.size() > maxResultCount ? View.VISIBLE : View.GONE);
        }

        if (customers != null && customers.size() > maxResultCount) {
            customers = customers.subList(0, maxResultCount);
        }

        customerPreviewAdapter.setItems(customers);
    }

    @Override
    public List<Customer> getCachedResource() {
        return customers;
    }

    @Override
    public void loadResource() {
        searchCustomer();
    }


    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_SEARCH_CLIENT.getCode();
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    @Override
    public void onResourceViewResponse(ResourceResponseEvent<List<Customer>> resourceResponseEvent) {
        this.customers = resourceResponseEvent.getResource();
        if (EResourceType.CUSTOMERS == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        }
    }


    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------


    protected void saveSearchInfos(Bundle bundle) {
        bundle.putString(EXTRA_TEXT_SEARCH, textSearch.getValue());
    }

    protected void getSearchInfosFromBundle(Bundle bundle) {
        String text = bundle.getString(EXTRA_TEXT_SEARCH, "");
        String firstName = bundle.getString(EXTRA_FIRST_NAME, "");
        String lastName = bundle.getString(EXTRA_LAST_NAME, "");
        String companyName = bundle.getString(EXTRA_ORGANIZATION_NAME, "");
        String phone = bundle.getString(EXTRA_SEARCH_PHONE, "");
        String email = bundle.getString(EXTRA_SEARCH_EMAIL, "");
        if (!TextUtils.isEmpty(text)) {
            textSearch.getValueField().setText(text);
            searchCustomer();
        }
    }

    private class CustomerSearchItemClick implements RecyclerItemClickListener.OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            if (AbstractCustomerSearchActivity.this.customers != null && AbstractCustomerSearchActivity.this.customers.size() > position) {
                Intent intent = new Intent(AbstractCustomerSearchActivity.this, CustomerActivity.class);
                customerContext.setCustomerId(AbstractCustomerSearchActivity.this.customers.get(position).getId());
                startActivity(intent);
            }
        }
    }


    private TextWatcher CLEAR_SEARCH_VISIBILITY_LISTENER = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateDesign() {
        if (textSearch != null) {
            textSearch.initFieldNames(this.getString(R.string.customer_search_text),
                    this.getString(R.string.customer_search_text));
        }

        TextSearchField[] searchFields = {
                textSearch
        };

        for (TextSearchField searchField : searchFields) {
            if (searchField != null) {
                for (Drawable drawable : searchField.getValueField().getCompoundDrawables()) {
                    if (drawable != null) {
                        CompatUtils.setDrawableTint(drawable, ContextCompat.getColor(this, R.color.search_gray));
                    }
                }
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
        itemClickListener = new RecyclerItemClickListener(this, new CustomerSearchItemClick());
        customersRecyclerView.addOnItemTouchListener(itemClickListener);
    }

    private void initPresenters() {
        // Set presenter's view
        customerSearchPresenter.setView(this);
    }


    private void initAdapter() {
        customerPreviewAdapter = new CustomerPreviewAdapter(this, R.layout.cell_customer_search);
    }

    protected abstract void initListeners();

    @Optional
    @OnClick({R.id.reload_button})
    public void onRetrySearchCustomers() {
        searchCustomer();
    }

    protected abstract void searchCustomer();

}
