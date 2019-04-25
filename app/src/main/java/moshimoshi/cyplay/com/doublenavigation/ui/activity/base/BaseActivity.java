package moshimoshi.cyplay.com.doublenavigation.ui.activity.base;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.EFeature;
import moshimoshi.cyplay.com.doublenavigation.model.events.BasketUpdatedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.CustomerLoadedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.FakeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.SellerLoadedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.AuthenticationPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.BasketActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CatalogActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerFormActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.CustomerSearchActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ProductsSearchActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ScanActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerDashboardActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ActivityTab;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ESearchMode;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.CustomerSpinnerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.BadgeDrawable;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.DesignUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.FeatureColor;
import ninja.cyplay.com.apilibrary.domain.interactor.SellerInteractor;
import ninja.cyplay.com.apilibrary.models.business.EScanFilters;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.Constants;

public abstract class BaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Inject
    BasketPresenter basketPresenter;

    @Inject
    protected AuthenticationPresenter authenticationPresenter;

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Inject
    CustomerContext customerContext;

    @Inject
    SellerContext sellerContext;

    @Inject
    public SellerInteractor sellerInteractor;

    @Inject
    public APIValue apiValue;

    @Inject
    protected EventBus bus;

    @Inject
    protected ConfigHelper configHelper;

    protected boolean connectedActivity = true;

    protected Spinner customerSpinner;

    protected MenuItem catalogMenu;
    protected MenuItem customerMenu;
    protected MenuItem basketMenu;

    private boolean customerSpinnerInitialized = false;
    public static final int CUSTOMER_SPINNER_META_DATA_COUNT = 1;


    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // lock orientation if phone or tablet etc
        this.setOrientation();
        injectDependencies();

        if (bus != null) {
            bus.register(this);
        }

        // Views are irrevelant if not logged in
        if (sellerContext != null && sellerContext.isSellerIdentified()) {
            Answers.getInstance().logContentView(
                    new ContentViewEvent()
                            .putContentId(this.getClass().getSimpleName())
                            .putContentName(this.getClass().getSimpleName())
                            .putContentType("Activity"));
        }

    }

    @SuppressWarnings("WrongConstant")
    protected void setOrientation() {
        //this.setRequestedOrientation(this.getResources().getInteger(R.integer.screen_orientation));
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (connectedActivity && apiValue != null && apiValue.getOAuth2Credentials() == null) {
            ninja.cyplay.com.apilibrary.utils.ActivityHelper.goToRestartActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlayRetailApp.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlayRetailApp.activityPaused();
    }

    @Override
    protected void onDestroy() {
        if (bus != null) {
            bus.unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        injectViews();
        getActionBarToolbar();
        updateStatusBarColor();
        if (isLeftCrossClosable()) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
            }
        }
    }


    protected boolean isLeftCrossClosable() {
        return false;
    }

    protected boolean menuItemsVisible() {
        return true;
    }


    protected Toolbar getActionBarToolbar() {
        if (toolbar != null) {
            // Depending on which version of Android you are on the Toolbar or the ActionBar may be
            // active so the a11y description is set here.
            DesignUtils.setBackgroundColor(toolbar, Color.WHITE);
            setSupportActionBar(toolbar);
        }
        return toolbar;
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    public void injectDependencies() {
        PlayRetailApp.get(this).inject(this);
    }

    public void injectViews() {
        ButterKnife.bind(this);
    }

    private void updateStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(FeatureColor.getColorHex(this, FeatureColor.PRIMARY_DARK, configHelper)));
        }
    }

    private void recreateNavigationTo(Intent intent) {
        TaskStackBuilder builder = TaskStackBuilder.create(BaseActivity.this);
        builder.addNextIntentWithParentStack(intent);
        builder.startActivities();
        overridePendingTransition(0, 0);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.custom_base_activity, menu);

        catalogMenu = menu.findItem(R.id.action_catalog);
        displayCatalogItem();

        customerMenu = menu.findItem(R.id.action_customer);
        if (customerMenu != null) {
            customerMenu.setVisible(customerContext != null && customerContext.isCustomerIdentified());
            customerSpinner = (Spinner) (((FrameLayout) menu.findItem(R.id.action_customer).getActionView()).getChildAt(0));
            // Customer
            initCustomerSpinner();
        }

        basketMenu = menu.findItem(R.id.action_basket);
        displayBasketItemCount();

        setMenuItemsVisibility(menu);

        // Set the color of the action bar items
        applyTintColorToMenuItems(menu, new int[]{R.id.action_scan,
                R.id.action_search,
                R.id.action_basket});


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_catalog:
                openCatalog();
                break;
            case R.id.action_search:
                searchIconAction(item);
                break;
            case R.id.action_scan:
                scanIconAction(item);
                break;
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
            case R.id.action_basket:
                openBasket();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void searchIconAction(MenuItem item) {
        if (configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getSearch().getCustomer() &&
                configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getSearch().getProduct()) {
            //all true
            createSearchPopupMenu(item);
        } else if (configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getSearch().getCustomer() &&
                !configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getSearch().getProduct()) {
            //just customer is true
            Intent intent = new Intent(BaseActivity.this, CustomerSearchActivity.class);
            recreateNavigationTo(intent);
        } else if (!configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getSearch().getCustomer() &&
                configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getSearch().getProduct()) {
            //just product is true
            Intent intent = new Intent(BaseActivity.this, ProductsSearchActivity.class);
            intent.putExtra(IntentConstants.EXTRA_SEARCH_MODE, ESearchMode.TEXT);
            recreateNavigationTo(intent);
        }
    }

    private void scanIconAction(MenuItem item) {
        if (configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getScan().getCustomer() &&
                configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getScan().getProduct()) {
            //all true
            createScanPopupMenu(item);
        } else if (configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getScan().getCustomer() &&
                !configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getScan().getProduct()) {
            //just customer is true
            Intent intent = new Intent(BaseActivity.this, ScanActivity.class);
            intent.putExtra(Constants.EXTRA_SCAN_FILTER, EScanFilters.SCAN_CUSTOMER);
            recreateNavigationTo(intent);
        } else if (!configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getScan().getCustomer() &&
                configHelper.getConfig().getFeature().getDisplayConfig().getTopBar().getScan().getProduct()) {
            //just product is true
            Intent intent = new Intent(BaseActivity.this, ScanActivity.class);
            intent.putExtra(Constants.EXTRA_SCAN_FILTER, EScanFilters.SCAN_PROD);
            recreateNavigationTo(intent);
        }
    }

    public void openCatalog() {
        Intent intent = new Intent(BaseActivity.this, CatalogActivity.class);
        intent.putExtra(IntentConstants.EXTRA_SEARCH_MODE, ESearchMode.CATEGORY);
        this.startActivity(intent);
    }

    public void openBasket() {
        Intent intent = new Intent(BaseActivity.this, BasketActivity.class);
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
    }

    private void createSearchPopupMenu(MenuItem item) {
        View menuItemView = findViewById(item.getItemId());

        PopupMenu popupMenu = new PopupMenu(BaseActivity.this, menuItemView);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.search_product:
                        intent = new Intent(BaseActivity.this, ProductsSearchActivity.class);
                        intent.putExtra(IntentConstants.EXTRA_SEARCH_MODE, ESearchMode.TEXT);
                        recreateNavigationTo(intent);
                        break;
                    case R.id.search_customer:
                        intent = new Intent(BaseActivity.this, CustomerSearchActivity.class);
                        recreateNavigationTo(intent);
                        break;
                }
                return false;
            }
        });

        popupMenu.inflate(R.menu.popup_menu_search);
        popupMenu.show();
    }


    private void createScanPopupMenu(MenuItem item) {
        View menuItemView = findViewById(item.getItemId());

        PopupMenu popupMenu = new PopupMenu(BaseActivity.this, menuItemView);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(BaseActivity.this, ScanActivity.class);
                switch (item.getItemId()) {
                    case R.id.scan_product:
                        intent.putExtra(Constants.EXTRA_SCAN_FILTER, EScanFilters.SCAN_PROD);
                        break;
                    case R.id.scan_customer:
                        intent.putExtra(Constants.EXTRA_SCAN_FILTER, EScanFilters.SCAN_CUSTOMER);
                        break;
                }
                recreateNavigationTo(intent);
                return false;
            }
        });
        popupMenu.inflate(R.menu.popup_menu_scan);

        popupMenu.show();
    }

    public void updateViewForLoadedCustomer() {

    }

    /**
     * Init the spinner with the shortcuts to the customer activity
     */
    protected void initCustomerSpinner() {
        if (!this.customerSpinnerInitialized && customerSpinner != null && customerContext != null && customerContext.isCustomerIdentified()) {

            List<String> spinnerArray = new ArrayList<>();
            // Name of the customer
            spinnerArray.add(customerContext.getCustomer().getDetails().getFormatName(this));
            // Customer activity tabs
            for (ActivityTab tab : configHelper.getCustomerActivityTabs(this)) {
                spinnerArray.add(tab.getTitle(this));
            }
            // Update customer
            spinnerArray.add(this.getResources().getString(R.string.edit_customer));
            // End session
            spinnerArray.add(this.getResources().getString(R.string.end_session));

            // Create adapter
            CustomerSpinnerAdapter<String> spinnerArrayAdapter = new CustomerSpinnerAdapter<>(this,
                    spinnerArray);

            // Set adapter
            customerSpinner.setAdapter(spinnerArrayAdapter);
            customerSpinner.setSelection(0, false);
            customerSpinner.setOnItemSelectedListener(this);
            customerSpinner.setPopupBackgroundResource(R.color.seller_dashboard_background);
            this.customerSpinnerInitialized = true;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        List<ActivityTab<Fragment>> tabs = configHelper.getCustomerActivityTabs(this);

        // Open Customer Activity
        if (position == 0) {
            goToCustomerActivity(position);
        }
        // Customer tabs shortcut
        else if (position >= CUSTOMER_SPINNER_META_DATA_COUNT && position < tabs.size() + CUSTOMER_SPINNER_META_DATA_COUNT) {
            // If we are already in Customer Activity, we just swich tab
            if (this instanceof CustomerActivity) {
                ((CustomerActivity) this).goToPage(position - CUSTOMER_SPINNER_META_DATA_COUNT, true);
                //else we start the activity
            } else {
                goToCustomerActivity(position);
            }
            // Edit Profile
        } else if (position == tabs.size() + CUSTOMER_SPINNER_META_DATA_COUNT) {
            Intent intent = new Intent(this, CustomerFormActivity.class);
            Customer customer = customerContext.getCustomer();
            intent.putExtra(IntentConstants.EXTRA_CUSTOMER, Parcels.wrap(customer));
            intent.putExtra(IntentConstants.EXTRA_FORM_EDIT_MODE, Constants.FORM_UPDATE_KEY);
            this.startActivity(intent);
            //Logout
        } else if (position == tabs.size() + CUSTOMER_SPINNER_META_DATA_COUNT + 1) {
            Intent intent = new Intent(this, SellerDashboardActivity.class);
            customerContext.clearContext();
            if (!(this instanceof SellerDashboardActivity)) {
                NavUtils.navigateUpTo(this, intent);
            }
            if (customerMenu != null) {
                customerMenu.setVisible(customerContext != null && customerContext.isCustomerIdentified());
            }
            // update view (left menu)
            updateViewForLoadedCustomer();
            //update basket Icon
            basketPresenter.getBasket();
        }


    }

    private void goToCustomerActivity(int position) {
        Intent intent = new Intent(this, CustomerActivity.class);
        if (position > 0) {
            intent.putExtra(IntentConstants.EXTRA_CUSTOMER_TAB, position - CUSTOMER_SPINNER_META_DATA_COUNT);
        }

        if (customerContext.isAliveCustomerActivityUp()) {
            NavUtils.navigateUpTo(this, intent);
        } else {
            this.startActivity(intent);
        }
    }

    @Subscribe
    public void onBasketUpdatedEvent(BasketUpdatedEvent basketUpdatedEvent) {
        // Ignore
        if (BasketUpdatedEvent.EBasketPersonType.SELLER.equals(basketUpdatedEvent.geteBasketPersonType())
                && customerContext.isCustomerIdentified()
                ){

        }else{
            displayBasketItemCount();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void applyTintColorToMenuItems(Menu menu, int[] menuItemIds) {
        for (int menuId : menuItemIds) {
            MenuItem menuItem = menu.findItem(menuId);
            if (menuItem != null) {
                if (menuItem.getIcon() != null) {
                    CompatUtils.setDrawableTint(menuItem.getIcon(), ContextCompat.getColor(this, R.color.actionBarItem));
                }
            }
        }
    }

    protected void setMenuItemsVisibility(Menu menu) {

        // hide options if not logged-in
        menu.findItem(R.id.action_scan).setVisible(menuItemsVisible() && sellerContext != null && sellerContext.isSellerIdentified());
        menu.findItem(R.id.action_search).setVisible(menuItemsVisible() && sellerContext != null && sellerContext.isSellerIdentified());

        updateBasketVisibility();

        // Scan
        MenuItem scanItem = menu.findItem(R.id.action_scan);
        if (scanItem != null) {
            scanItem.setVisible(menuItemsVisible() && sellerContext != null
                    && sellerContext.isSellerIdentified()
                    && configHelper.isFeatureActivated(EFeature.SCAN));
        }

    }


    protected void updateBasketVisibility() {
        // Basket
        if (basketMenu != null) {
            Basket basket = basketPresenter.getCachedBasket();
            if (basket == null) {
                basketMenu.setVisible(false);
                return;
            }

            basketMenu.setVisible(menuItemsVisible() && configHelper.shouldDisplayBasketFeatures(sellerContext, customerContext));
        }
    }

    protected void displayCatalogItem() {
        ;
    }

    private void displayBasketItemCount() {
        Basket basket = basketPresenter.getCachedBasket();
        if (basket != null && basketMenu != null) {
            basketMenu.setVisible(menuItemsVisible());
            BadgeDrawable.setBadgeCount(this, (LayerDrawable) basketMenu.getIcon(), String.valueOf(basket.getBasketItemsCount()));
        }
    }

    @Subscribe
    public void onFakeEvent(FakeEvent fakeEvent) {

    }

    @Subscribe
    public void onCustomerLoadedEvent(CustomerLoadedEvent event) {
        if (customerMenu != null && event.getCustomer() != null) {
            initCustomerSpinner();
            updateViewForLoadedCustomer();
            customerMenu.setVisible(true);
            if (event.isLinkSellerToCustomer() && configHelper.isFeatureActivated(EFeature.BASKET) && configHelper.getPaymentConfig().getCustomer_basket() && event.consume()) {
                basketPresenter.getBasket();
                // If we do not need to refresh customer basket, we might need to refresh the basket one after a ling seller -> customer
            }/*else if (!event.isLinkSellerToCustomer() && configHelper.isFeatureActivated(EFeature.BASKET) && configHelper.getPaymentConfig().getSeller_basket() && event.consume()){
                basketPresenter.getSellerBasket();
            }*/
        }

    }

    @Subscribe
    public void onSellerLoadedEvent(SellerLoadedEvent event) {

        if (configHelper.isFeatureActivated(EFeature.BASKET) && configHelper.getPaymentConfig().getSeller_basket() && event.consume()) {
            basketPresenter.getBasket();
        }
    }

}


