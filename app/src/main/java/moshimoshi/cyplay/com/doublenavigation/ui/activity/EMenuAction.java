package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.presenter.AuthenticationPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ESearchMode;
import ninja.cyplay.com.apilibrary.models.business.EScanFilters;
import ninja.cyplay.com.apilibrary.utils.Constants;

/**
 * Created by romainlebouc on 02/09/16.
 */
public enum EMenuAction {


    NAVDRAWER_ITEM_HOME("home") {
        public Intent getIntent(Activity activity) {
            return new Intent(activity, SellerDashboardActivity.class);
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    },
    NAVDRAWER_ITEM_CARD_CREATION("new_card") {
        public Intent getIntent(Activity activity) {
            Intent intent = new Intent(activity, CustomerFormActivity.class);
            intent.putExtra(IntentConstants.EXTRA_FORM_EDIT_MODE, Constants.FORM_CREATE_KEY);
            return intent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.NEW;
        }

    },
    NAVDRAWER_ITEM_SCAN_CARD_CREATION("scan_new_card") {
        public Intent getIntent(Activity activity) {
            Intent scanNewCardIntent = new Intent(activity, ScanActivity.class);
            scanNewCardIntent.putExtra(Constants.EXTRA_SCAN_FILTER, EScanFilters.SCAN_NEW_CUSTOMER);
            return scanNewCardIntent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.NEW;
        }

    },
    NAVDRAWER_ITEM_SCAN_PROD("scan_product") {
        public Intent getIntent(Activity activity) {
            Intent scanIntent = new Intent(activity, ScanActivity.class);
            scanIntent.putExtra(Constants.EXTRA_SCAN_FILTER, EScanFilters.SCAN_PROD);
            return scanIntent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.NEW;
        }
    }, NAVDRAWER_ITEM_SCAN_CLIENT("scan_client") {
        public Intent getIntent(Activity activity) {
            Intent scanProdIntent = new Intent(activity, ScanActivity.class);
            scanProdIntent.putExtra(Constants.EXTRA_SCAN_FILTER, EScanFilters.SCAN_CUSTOMER);
            return scanProdIntent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.NEW;
        }
    }, NAVDRAWER_ITEM_SEARCH_PRODUCT("search_product") {
        public Intent getIntent(Activity activity) {
            Intent intent = new Intent(activity, ProductsSearchActivity.class);
            intent.putExtra(IntentConstants.EXTRA_SEARCH_MODE, ESearchMode.TEXT);
            return intent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }

    }, NAVDRAWER_ITEM_SEARCH_CLIENT("search_client") {
        public Intent getIntent(Activity activity) {
            return new Intent(activity, CustomerSearchActivity.class);
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    }, NAVDRAWER_ITEM_CATALOG("catalog") {
        public Intent getIntent(Activity activity) {
            Intent intent;
//            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                intent = new Intent(activity, CatalogWithMenuActivity.class);
//            } else {
            intent = new Intent(activity, CatalogActivity.class);
//            }
            intent.putExtra(IntentConstants.EXTRA_SEARCH_MODE, ESearchMode.CATEGORY);
            return intent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    }, NAVDRAWER_ITEM_CUSTOMER_IDENTIFIED("identified_customer") {
        public Intent getIntent(Activity activity) {
            return new Intent(activity, CustomerActivity.class);
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    }, NAVDRAWER_ITEM_OFFERS("offers") {
        public Intent getIntent(Activity activity) {
            Intent offersIntent = new Intent(activity, OffersActivity.class);
            offersIntent.putExtra(Constants.EXTRA_SHOP_OFFERS_ONLY, true);
            return offersIntent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }

    }, NAVDRAWER_ITEM_OFFERS_BY_TYPE("offers_by_type") {
        public Intent getIntent(Activity activity) {
            Intent offersIntent = new Intent(activity, OffersByTypeActivity.class);
            offersIntent.putExtra(Constants.EXTRA_SHOP_OFFERS_ONLY, true);
            return offersIntent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    }, NAVDRAWER_ITEM_CONTACT("support") {
        @Override
        public Intent getIntent(Activity activity) {
            return new Intent(activity, SupportActivity.class);
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    },NAVDRAWER_ITEM_LOGOUT("logout") {
        public Intent getIntent(Activity activity) {
            return null;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {
            if (authenticationPresenter != null) {
                authenticationPresenter.invalidateToken();
            }
        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.NONE;
        }

    }, NAVDRAWER_ITEM_CALENDAR("calendar") {
        public Intent getIntent(Activity activity) {
            return new Intent(activity, CalendarActivity.class);
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }


    }, NAVDRAWER_ITEM_CONFIGURATION("configuration") {
        @Override
        public Intent getIntent(Activity activity) {
            return new Intent(activity, ConfigurationActivity.class);
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    },
    NAVDRAWER_ITEM_WEBVIEW_RENDEZVOUS_INSTITUT("webview_institut_appointment") {
        @Override
        public Intent getIntent(Activity activity) {
            Intent intent = new Intent(activity, RendezVousInstitutWebViewActivity.class);
            intent.putExtra(IntentConstants.EXTRA_WEBVIEW_ID, this.getCode());
            intent.putExtra(IntentConstants.EXTRA_WEBVIEW_TITLE, activity.getString(R.string.title_institut_appointment));
            return intent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    },
    NAVDRAWER_ITEM_WEBVIEW_CLICK_AND_COLLECT("webview_clickandcollect") {
        @Override
        public Intent getIntent(Activity activity) {
            Intent intent = new Intent(activity, ClickAndCollectWebviewActivity.class);
            intent.putExtra(IntentConstants.EXTRA_WEBVIEW_ID, this.getCode());
            intent.putExtra(IntentConstants.EXTRA_WEBVIEW_TITLE, activity.getString(R.string.title_click_and_collect));
            return intent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    },
    NAVDRAWER_ITEM_SYNCHRONIZATION_DATA("synchronization") {
        @Override
        public Intent getIntent(Activity activity) {
            Intent intent = new Intent(activity, SynchronizationActivity.class);
            intent.putExtra(IntentConstants.EXTRA_WEBVIEW_ID, this.getCode());
            intent.putExtra(IntentConstants.EXTRA_WEBVIEW_TITLE, activity.getString(R.string.title_synchronization));
            return intent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    },
    NAVDRAWER_ITEM_SALES_HISTORY("sales_history") {
        @Override
        public Intent getIntent(Activity activity) {
            Intent intent = new Intent(activity, SalesHistoryActivity.class);
            intent.putExtra(IntentConstants.EXTRA_WEBVIEW_ID, this.getCode());
            intent.putExtra(IntentConstants.EXTRA_WEBVIEW_TITLE, activity.getString(R.string.title_sales_history));
            return intent;
        }

        @Override
        public void executeExtra(AuthenticationPresenter authenticationPresenter) {

        }

        @Override
        public EMenuActionActivity getEMenuActionActivity() {
            return EMenuActionActivity.BACK_STACK;
        }
    };

    EMenuAction(String code) {
        this.code = code;
    }

    private final String code;
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 128;
    public final static Map<String, EMenuAction> CODE_2_MENU_ACTION = new HashMap<>();

    public abstract Intent getIntent(Activity activity);

    public abstract void executeExtra(AuthenticationPresenter authenticationPresenter);

    public abstract EMenuActionActivity getEMenuActionActivity();

    static {
        for (EMenuAction eMenuAction : EMenuAction.values()) {
            CODE_2_MENU_ACTION.put(eMenuAction.code, eMenuAction);
        }
    }

    public static EMenuAction getEMenuActionFromCode(String code) {
        return CODE_2_MENU_ACTION.get(code);
    }

    private static void createBackStack(final MenuBaseActivity activity, final Intent intent, DrawerLayout drawerLayout, View mainContent) {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // launch the target Activity after a short delay, to allow the close animation to play
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    TaskStackBuilder builder = TaskStackBuilder.create(activity);
                    builder.addNextIntentWithParentStack(intent);
                    builder.startActivities();
                    activity.overridePendingTransition(0, 0);
                }
            }, NAVDRAWER_LAUNCH_DELAY);
            // FadeOut animation
            if (mainContent != null) {
                mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
            }
        } else {
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public String getCode() {
        return code;
    }

    public void onActionSelected(MenuBaseActivity activity, DrawerLayout drawerLayout, View mainContent, AuthenticationPresenter authenticationPresenter) {
        Intent intent = this.getIntent(activity);
        this.executeExtra(authenticationPresenter);
        switch (this.getEMenuActionActivity()) {
            case NEW:
                activity.startActivity(intent);
                break;
            case BACK_STACK:
                createBackStack(activity, intent, drawerLayout, mainContent);
                break;
            case NONE:
                break;
        }

        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

}