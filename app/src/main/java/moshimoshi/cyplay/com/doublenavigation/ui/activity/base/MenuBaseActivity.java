package moshimoshi.cyplay.com.doublenavigation.ui.activity.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.BuildConfig;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_MenuItem;
import moshimoshi.cyplay.com.doublenavigation.model.events.MenuActionEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.EMenuAction;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerDashboardActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerTeamActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.HomeMenuAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.MessageProgressDialog;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.PicturedBadge;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.DividerItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.utils.ActivityHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.FeatureColor;
import moshimoshi.cyplay.com.doublenavigation.view.AuthenticationView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AUser;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;


/**
 * Created by damien on 01/04/16.
 */
public abstract class MenuBaseActivity extends BaseActivity implements AuthenticationView {

    @Inject
    SellerContext sellerContext;

    @Inject
    APIValue apiValue;

    // Navigation drawer:
    @Nullable
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    @Nullable
    @BindView(R.id.nav_view)
    protected View navView;

    // Primary toolbar and drawer toggle
    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Nullable
    @BindView(R.id.profile_view)
    PicturedBadge profileBadge;

    @Nullable
    @BindView(R.id.profile_name)
    TextView sellerName;


    @BindView(R.id.left_menu_recycler_view)
    RecyclerView leftMenuRecyclerView;
    // views that correspond to each navdrawer item, null if not yet created

    // Views of client name and version
    @Nullable
    @BindView(R.id.app_client_name)
    TextView appClientName;

    @Nullable
    @BindView(R.id.app_version)
    TextView appVersion;

    // View Container to create alpha animation
    @Nullable
    @BindView(R.id.main_content_container)
    protected View mainContent;

    // Progress Dialog to display logout progress
    private MessageProgressDialog progressDialog;

    // fade in and fade out durations for the main content when switching between
    // different Activities of the app through the Nav Drawer
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    // Left Menu items
    private List<PR_MenuItem> navDrawerItems;

    // Left Menu Adapter
    private HomeMenuAdapter homeMenuAdapter;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
        if (sellerContext != null) {
            profileBadge.setProfile(sellerContext.getSeller());
        }
        if (sellerContext != null && sellerContext.getSeller() != null) {
            sellerName.setText(sellerContext.getSeller().getFormatName());
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Progress Dialog
        progressDialog = new MessageProgressDialog(this);
        // fillStocks Authentication presenter's view
        if (authenticationPresenter != null)
            authenticationPresenter.setView(this);

        // set client name and version
        if (appClientName != null && appVersion != null) {
            appClientName.setText(BuildConfig.CLIENT_NAME + "-" + BuildConfig.BUILD_TYPE);
            appVersion.setText(BuildConfig.VERSION_NAME);
        }

        // setup navigation drawer
        if (!isLeftCrossClosable()) {
            setupNavDrawer();
        }

        // Animate the content with a FadeIn
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else
            Log.d(getLocalClassName(), "No view with ID main_content to fade in.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // populate the nav drawer with the correct items
        populateNavDrawer();

    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public void updateViewForLoadedCustomer() {
        super.updateViewForLoadedCustomer();
        populateNavDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_basket) {
            openBasket();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private Boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        // Close menu first if one is open
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            if (this instanceof SellerDashboardActivity) {
                if (doubleBackToExitPressedOnce) {
                    authenticationPresenter.invalidateToken();
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(this, getString(R.string.back_logout), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                super.onBackPressed();
            }
        }
    }

    protected abstract String getNavDrawerCurrentItem();

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    protected Toolbar getActionBarToolbar() {
        if (toolbar != null) {
            // Depending on which version of Android you are on the Toolbar or the ActionBar may be
            // active so the a11y description is set here.
            setSupportActionBar(toolbar);
        }
        return toolbar;
    }

    private void setupNavDrawer() {
        if (drawerLayout == null || toolbar == null) {
            return;
        }
        drawerLayout.setStatusBarBackgroundColor(Color.parseColor(FeatureColor.getColorHex(this, FeatureColor.PRIMARY_DARK, configHelper)));

        // Set up icon in the bar
        setSupportActionBar(toolbar);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                ActivityHelper.hideKeyboard(MenuBaseActivity.this);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();

    }

    private void populateNavDrawer() {
        if (configHelper.getConfig() != null
                && configHelper.getConfig() != null
                && configHelper.getConfig().getFeature().getDisplayConfig() != null
                && configHelper.getConfig().getFeature().getDisplayConfig().getSideBarItems() != null) {
            navDrawerItems = configHelper.getConfig().getFeature().getDisplayConfig().getSideBarItems();
            // Check if customer context
            updateCustomerRow();
        }
        homeMenuAdapter = new HomeMenuAdapter(MenuBaseActivity.this);
        homeMenuAdapter.setMenuItems(navDrawerItems);
        homeMenuAdapter.setSelectedItem(getNavDrawerCurrentItem());
        homeMenuAdapter.setCustomerContext(customerContext);
        initHomeMenuRecyclerView(homeMenuAdapter);
    }

    private void updateCustomerRow() {
        if (navDrawerItems != null) {
            for (int i = 0; i < navDrawerItems.size(); i++) {
                PR_MenuItem item = navDrawerItems.get(i);
                if (item != null && EMenuAction.NAVDRAWER_ITEM_CUSTOMER_IDENTIFIED.getCode().equals(item.getTag())) {
                    item.setVisibility(customerContext != null && customerContext.isCustomerIdentified());
                    break;
                }
            }
        }
    }

    private void initHomeMenuRecyclerView(HomeMenuAdapter adapter) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        leftMenuRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        leftMenuRecyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        leftMenuRecyclerView.setAdapter(adapter);
        // Add Decorator
        leftMenuRecyclerView.addItemDecoration(new DividerItemDecoration(MenuBaseActivity.this));
        // OnClick action
//        leftMenuRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MenuBaseActivity.this, new MenuItemClick()));
    }


    @Subscribe
    public void onMenuActionEvent(MenuActionEvent menuActionEvent) {

        EMenuAction eMenuAction = EMenuAction.getEMenuActionFromCode(menuActionEvent.getTag());
        if (eMenuAction != null) {
            eMenuAction.onActionSelected(this, drawerLayout, mainContent, authenticationPresenter);
        }

    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void showLoading() {
        if (progressDialog != null) {
            progressDialog.show();
        }

    }

    @Override
    public void onAccessTokenSuccess() {

    }

    @Override
    public void onInvalidateTokenSuccess() {
        // stop animation
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        // clear context
        if (sellerContext != null) {
            sellerContext.clearContext();
            PlayRetailApp.get(this).unRegisterScreenLogoutReceiver();
        }
        if (customerContext != null) {
            customerContext.clearContext();
        }
        // go back to the sellers team page
        NavUtils.navigateUpTo(this, new Intent(this, SellerTeamActivity.class));
    }

    @Override
    public void onInvalidateTokenError() {
        onInvalidateTokenSuccess();
    }

    @Override
    public void onGetSellerSuccess() {

    }

    @Override
    public void onPasswordChangedSuccess(PR_AUser user) {

    }

    @Override
    public void onPasswordChangedFailure(BaseException exception) {

    }
}
