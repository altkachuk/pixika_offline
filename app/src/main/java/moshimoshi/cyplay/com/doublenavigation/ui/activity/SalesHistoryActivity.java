package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.SalesPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SaleFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SalesFragment;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;


public class SalesHistoryActivity extends MenuBaseActivity implements ResourceView<List<Sale>> {

    private static final String SALES_FRAGMENT_TAG = "SALES_FRAGMENT_TAG";
    private static final String SALE_FRAGMENT_TAG = "SALE_FRAGMENT_TAG";

    @Inject
    SalesPresenter salesPresenter;

    @Nullable
    @BindView(R.id.state_loading_view)
    LoadingView loadingView;

    private SaleFragment saleFragment;

//    private boolean onRotate = false;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        //load sales
        loadAllSales();
    }

    private void initialize(){
        setContentView(getContentViewLayoutId());

        // Show back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // set presenter's view
        salesPresenter.setView(this);
    }

    public int getContentViewLayoutId() {
        return R.layout.activity_sales_history;
    }


    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        //before backpressed
        hideSaleFragment();

        super.onBackPressed();

        //after backpressed
        SalesFragment mFragment = (SalesFragment) getSupportFragmentManager().findFragmentByTag(SALES_FRAGMENT_TAG);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void hideSaleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (saleFragment != null)
            fragmentTransaction.hide(saleFragment);

        fragmentTransaction.commitAllowingStateLoss();
        saleFragment = null;
    }

    private void loadAllSales() {
        if (loadingView != null) {
            loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }

        salesPresenter.getAllSales();
    }

    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_CATALOG.getCode();
    }


    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------


    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    public void displaySale(Sale sale) {
        saleFragment = new SaleFragment();

        // Display Products
        FragmentManager fragmentManager = getSupportFragmentManager();

        saleFragment.setSale(sale);
        FragmentTransaction categoryFragmentTransaction = fragmentManager.beginTransaction();
        categoryFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        categoryFragmentTransaction.replace(R.id.sale_container, saleFragment, SALE_FRAGMENT_TAG);
        categoryFragmentTransaction.addToBackStack(null);
        categoryFragmentTransaction.commitAllowingStateLoss();
    }

    public void displayAndUpdateSales() {
        hideSaleFragment();
        loadAllSales();
    }


    @Override
    public void onResourceViewResponse(ResourceResponseEvent<List<Sale>> resourceResponseEvent) {
        if (loadingView != null) {
            loadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        }

        // Fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(SALES_FRAGMENT_TAG) != null) {
            SalesFragment fragment = (SalesFragment) fragmentManager.findFragmentByTag(SALES_FRAGMENT_TAG);
            // Set Data
            fragment.setSales(resourceResponseEvent.getResource());
        } else {
            // Create fragment
            SalesFragment fragment = new SalesFragment();
            // Set Data
            fragment.setSales(resourceResponseEvent.getResource());

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.sales_container, fragment, SALES_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }

    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        //TODO
    }


}
