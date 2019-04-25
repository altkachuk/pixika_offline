package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import org.parceler.Parcels;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Offer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ResourcesFilterMultipleChoiceFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ResourcesFilterSingleChoiceFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.abs.FilterFragmentHandler;

/**
 * Created by damien on 19/08/16.
 */
public class FullScreenFilteringActivity extends BaseActivity {

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    private FilterFragmentHandler filterFragmentHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_resource_filter);
        Intent intent = this.getIntent();
        switch (intent.getStringExtra(IntentConstants.EXTRA_FILTER_MODE)){
            case IntentConstants.MULTI_CHOICE_FILTER:
                initMultiFilterFragment();
                break;
            case IntentConstants.SINGLE_CHOICE_FILTER:
                initSingleFilterFragment();
                break;
            default:
                initSingleFilterFragment();
                break;
        }
        // Show back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

            if (intent != null && intent.hasExtra(IntentConstants.EXTRA_RESOURCE_FILTER_COUNT)) {
                int productCount = intent.getIntExtra(IntentConstants.EXTRA_RESOURCE_FILTER_COUNT, 0);

                String resourceType = intent.getStringExtra(IntentConstants.EXTRA_RESOURCE_FILTER_RESOURCE);
                switch (resourceType != null ? resourceType : "") {
                    case Ticket.API_RESOURCE_NAME:
                        actionBar.setTitle(this.getResources().getQuantityString(R.plurals.sales_filter, productCount, productCount));
                        break;
                    case Product.API_RESOURCE_NAME:
                        actionBar.setTitle(R.string.filter_selection);
                        break;
                    case Offer.API_RESOURCE_NAME:
                        actionBar.setTitle(this.getResources().getQuantityString(R.plurals.offers_filter, productCount, productCount));
                        break;
                    default:
                        actionBar.setTitle(this.getResources().getQuantityString(R.plurals.items_filter, productCount, productCount));
                }
            }
        }

    }

    private void initMultiFilterFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        ResourcesFilterMultipleChoiceFragment resourcesFilterMultipleChoiceFragment = new ResourcesFilterMultipleChoiceFragment();
        filterFragmentHandler = resourcesFilterMultipleChoiceFragment;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.filter_fragment_container, resourcesFilterMultipleChoiceFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initSingleFilterFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        ResourcesFilterSingleChoiceFragment resourcesFilterMultipleChoiceFragment = new ResourcesFilterSingleChoiceFragment();
        filterFragmentHandler = resourcesFilterMultipleChoiceFragment;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.filter_fragment_container, resourcesFilterMultipleChoiceFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        finish();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filters_component, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.supportFinishAfterTransition();
                break;
            case R.id.action_reset_filter:
                filterFragmentHandler.resetActiveFilter();
                Intent intent = new Intent();
                intent.putExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS, Parcels.wrap(filterFragmentHandler.getActiveFilters()));
                this.setResult(IntentConstants.RESULT_ITEMS_FILTERS, intent);
                supportFinishAfterTransition();
                break;
            case R.id.action_valid_filter:
                Intent validIntent = new Intent();
                validIntent.putExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS, Parcels.wrap(filterFragmentHandler.getActiveFilters()));
                this.setResult(IntentConstants.RESULT_ITEMS_FILTERS, validIntent);
                supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void supportFinishAfterTransition() {
        super.supportFinishAfterTransition();
        this.overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
    }
}
