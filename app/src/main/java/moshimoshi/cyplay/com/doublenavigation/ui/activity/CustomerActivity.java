package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.history.CustomerHistory;
import moshimoshi.cyplay.com.doublenavigation.model.events.CustomerLoadedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.HistoryContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ActivityTab;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ViewPagerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.DisplayEventPagerListener;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import moshimoshi.cyplay.com.doublenavigation.view.BasketView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomer;

/**
 * Created by romainlebouc on 28/07/16.
 */
public class CustomerActivity extends MenuBaseActivity implements ResourceView<Customer>, ResourceActivity<Customer> {

    @Inject
    GetCustomerInfoPresenter getCustomerInfoPresenter;

    @Inject
    BasketPresenter basketPresenter;

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    @Inject
    HistoryContext historyContext;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.customer_tabs)
    TabLayout customerTabLayout;

    @Nullable
    @BindView(R.id.customer_pager_port)
    ViewPager portCustomerViewPager;

    @Nullable
    @BindView(R.id.customer_pager_land)
    ViewPager landCustomerViewPager;

    ViewPager customerViewPager;

    private Customer customer;
    private ViewPagerAdapter adapter;

    private int portCurrentTab;
    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        customerViewPager = portCustomerViewPager != null ? portCustomerViewPager : landCustomerViewPager;
        getCustomerInfoPresenter.setView(this);
        loadResource();
        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        initViewPager();
        customerContext.setAliveCustomerActivityUp(true);
        basketPresenter.setView(new BasketView() {
            @Override
            public void onStockResponse(final BasketItem basketItem, ResourceResponseEvent<Product> resourceResponseEvent) {

            }

            @Override
            public void onBasketItemsStocksChecked(boolean success) {

            }

            @Override
            public void onResourceViewResponse(ResourceResponseEvent<Basket> resourceResponseEvent) {
                bus.post(new CustomerLoadedEvent(customer, false));
            }

            @Override
            public void onValidateCartResponse() {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentConstants.EXTRA_FORM_EDIT_RESULT) {
            if (resultCode == RESULT_OK) {
                SnackBarHelper.buildSnackbar(coordinatorLayout, this.getString(R.string.customer_update_success), Snackbar.LENGTH_INDEFINITE, "OK");
                String customerId = this.customerContext.getCustomer().getId();
                this.customer = null;
                runGetCustomerRequest(customerId);
            }
        } else {
            // Necessary for fragment possible override
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.customer = customerContext.getCustomer();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        switch (this.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                //portrait to landscape
                //(when rotate from portrait, in this function the orientation is already changed in landscape)
                if (portCustomerViewPager != null)
                    outState.putInt(IntentConstants.EXTRA_PRODUCT_CURRENT_TAB,
                            portCustomerViewPager.getCurrentItem());

                break;
            case Configuration.ORIENTATION_PORTRAIT:
                //landscape to portrait
                if (landCustomerViewPager != null)
                    outState.putInt(IntentConstants.EXTRA_PRODUCT_CURRENT_TAB,
                            portCurrentTab == 0 ? portCurrentTab : landCustomerViewPager.getCurrentItem() + 1);
                break;
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int currentTab = savedInstanceState.getInt(IntentConstants.EXTRA_PRODUCT_CURRENT_TAB);

        switch (this.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                if (landCustomerViewPager != null) {
                    portCurrentTab = currentTab;
                    if (currentTab > 0)
                        currentTab--;
                    landCustomerViewPager.setCurrentItem(currentTab);
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                if (portCustomerViewPager != null)
                    portCustomerViewPager.setCurrentItem(currentTab);
                break;
        }

    }


    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_CUSTOMER_IDENTIFIED.getCode();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void runGetCustomerRequest(String customerId) {
        bus.post(new ResourceRequestEvent<PR_ACustomer>(EResourceType.CUSTOMER));
        getCustomerInfoPresenter.getCustomerInfo(customerId);
    }

    private void initViewPager() {
        for (ActivityTab customerActivityTab : configHelper.getCustomerActivityTabs(this)) {
            try {
                adapter.addFragment((Fragment) customerActivityTab.getFragmentClass().newInstance(),
                        customerActivityTab.getTitle(this));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // fillStocks viewPager
        customerViewPager.setAdapter(adapter);
        customerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (CustomerActivity.this.customerSpinner != null) {
                    CustomerActivity.this.customerSpinner.setSelection(CUSTOMER_SPINNER_META_DATA_COUNT + position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        customerViewPager.addOnPageChangeListener(new DisplayEventPagerListener(adapter));
        // fillStocks tabLayout
        customerTabLayout.setupWithViewPager(customerViewPager);

        int position = getIntent().getIntExtra(IntentConstants.EXTRA_CUSTOMER_TAB, -1);
        goToPage(position, false);
    }

    public void goToPage(int position, boolean smoothScroll) {
        if (position >= 0 && position < configHelper.getCustomerActivityTabs(this).size()) {
            customerViewPager.setCurrentItem(position, true);
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public Customer getResource() {
        return customer;
    }

    @Override
    public void loadResource() {
        // Run request
        String customerId = customerContext.hasToRequestForCustomer();
        if (customerId != null) {
            runGetCustomerRequest(customerId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customerContext.setAliveCustomerActivityUp(false);
    }

    @Override
    public void onResourceViewResponse(ResourceResponseEvent<Customer> resourceResponseEvent) {
        if (EResourceType.CUSTOMER == resourceResponseEvent.getEResourceType()) {
            bus.post(resourceResponseEvent);
            this.customer = resourceResponseEvent.getResource();
            historyContext.addCustomerHistory(new CustomerHistory(sellerContext.getSeller().getId(), this.customer, new Date()));
            if (sellerContext.getBasket() != null && sellerContext.getBasket().getBasketItemsCount() > 0) {
                basketPresenter.linkSellerBasketToCustomer(customer.getId());
            } else {
                bus.post(new CustomerLoadedEvent(customer));
            }
        }
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    @Override
    protected void displayCatalogItem() {
        catalogMenu.setVisible(true);
    }

    @Override
    protected void updateBasketVisibility() {
        if (!basketPresenter.isCustomerBasket()) {
            basketMenu.setVisible(false);
        } else {
            super.updateBasketVisibility();
        }
    }
}
