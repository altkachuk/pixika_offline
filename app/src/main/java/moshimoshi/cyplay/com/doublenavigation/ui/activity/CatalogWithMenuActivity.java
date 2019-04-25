package moshimoshi.cyplay.com.doublenavigation.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.CatalogueLevel;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceActionEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractCatalogActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ESearchMode;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ProductSearch;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.FilterableSortableProductsFragment;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;

/**
 * Created by romainlebouc on 20/12/2016.
 */

public class CatalogWithMenuActivity extends AbstractCatalogActivity {

    @BindView(R.id.category_loading_view)
    LoadingView catalogLoadingView;

    private FilterableSortableProductsFragment filterableSortableProductsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterableSortableProductsFragment = (FilterableSortableProductsFragment) this.getSupportFragmentManager().findFragmentById(R.id.catalog_products_fragment);
    }

    public int getContentViewLayoutId() {
        return R.layout.activity_catalog_with_menu;
    }

    //TODO
    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CatalogCategoriesFragment myFragment = (CatalogCategoriesFragment) getSupportFragmentManager().findFragmentByTag(CATALOGUE_FRAGMENT_TAG);
        // For All Button
        if (myFragment != null && myFragment.getCategoryCurrent() != null) {
            previousCategory = new Category(myFragment.getCategoryCurrent());
            ProductSearch search = new ProductSearch(ESearchMode.CATEGORY, myFragment.getCategoryCurrent().getId());
            runProductsSearch(search,
                    new Pagination(0,  getResources().getInteger(R.integer.row_load_pagination)  * getResources().getInteger(R.integer.catalog_columns_count)),
                    configHelper.getCatalogSortConfig(ESearchMode.CATEGORY),
                    null);
        }

    }

    @Override
    public void onResourceViewResponse(ResourceResponseEvent<CatalogueLevel> resourceResponseEvent) {
        if (resourceResponseEvent.getEResourceType() == EResourceType.CATALOG_LEVEL) {
            if (resourceResponseEvent.getResourceException() == null) {
                CatalogueLevel catalogueLevel = resourceResponseEvent.getResource();
                if (!this.isDestroyed()) {
                    if (catalogueLevel != null && catalogueLevel.getCategory() != null && catalogueLevel.getCategory().getId() != null) {
                        // view loaded

                        //TODO : catalogue loading view
                        catalogLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);

                        // can now click on category
                        disableClickView.setVisibility(View.GONE);
                        // Create fragment
                        CatalogCategoriesFragment fragment = new CatalogCategoriesFragment();
                        // Fragment manager
                        FragmentManager fragmentManager = getSupportFragmentManager();

                        CatalogCategoriesFragment currentFragment = (CatalogCategoriesFragment) fragmentManager.findFragmentById(R.id.category_container);
                        if (currentFragment != null) {
                            currentFragment.onCatalogCategoriesFragment();
                        }

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        // Set Data
                        fragment.setSubCategories(catalogueLevel.getSub_categories());
                        // Add or replace if First level
                        if (firstLevel) {
                            fragment.setCategoryCurrent(new Category(catalogueLevel.getCategory()));
                            fragmentTransaction.add(R.id.category_container, fragment, CATALOGUE_FRAGMENT_TAG);
                            firstLevel = false;

                        } else {
                            // For All Button
                            fragment.setCategoryCurrent(new Category(catalogueLevel.getCategory()));

                            // For previous button
                            fragment.setCategoryParent(new Category(this.previousCategory));
                            // Fragment Transaction
                            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                            fragmentTransaction.replace(R.id.category_container, fragment, CATALOGUE_FRAGMENT_TAG);
                            fragmentTransaction.addToBackStack(null);
                        }

                        fragmentTransaction.commitAllowingStateLoss();
                        if (catalogueLevel.getCategory() != null) {
                            this.previousCategory = new Category(catalogueLevel.getCategory());
                        }
                    }

                }
            } else {
                // TODO : manage
                if (firstLevel) {
                    catalogLoadingView.setLoadingState(LoadingView.LoadingState.FAILED);
                }
                // re-allow to click
                disableClickView.setVisibility(View.GONE);
                // Hide loading if necessary
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.category_container);
                if (currentFragment != null && currentFragment instanceof CatalogCategoriesFragment) {
                    ((CatalogCategoriesFragment) currentFragment).onCatalogCategoriesFragment();
                }
                SnackBarHelper.buildSnackbar(coordinatorLayout, getString(R.string.reload_error_msg), null).show();
            }


        }
    }

    public void displayNextCatalogPage(Category category) {
        currentCategory = category;

        if (!isDestroyed()) {
            if (category != null) {
                // Display Categories
                if (category.getHas_sub_families()) {
                    cataloguePresenter.getCatalogFromCategory(category.getId(), category.getName());
                }
                //TODO : what to do at last level ??
                // when loading we cannot click on other category
                disableClickView.setVisibility(View.VISIBLE);
                //}
                ProductSearch search = new ProductSearch(ESearchMode.CATEGORY, category.getId());

                runProductsSearch(search,
                        new Pagination(0,  getResources().getInteger(R.integer.row_load_pagination)  * getResources().getInteger(R.integer.catalog_columns_count)),
                        configHelper.getCatalogSortConfig(ESearchMode.CATEGORY),
                        null);
            }
        }
    }

    @Override
    public void loadResource(Pagination pagination) {
        filterableSortableProductsFragment.loadResource(pagination);
    }

    @Override
    public void onBackBtnClicked() {

    }

    @Subscribe
    public void onResourceResponseEvent(FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent) {
        if (resourceResponseEvent.getEResourceType() == EResourceType.PRODUCTS) {
            int size = resourceResponseEvent.getCount();
            if (catalogCategoryProductsCount != null) {
                catalogCategoryProductsCount.setVisibility(View.VISIBLE);
                catalogCategoryProductsCount.setText(this.getResources().getQuantityString(R.plurals.products_count, size, size));
            }

        }
    }

    @Subscribe
    public void onResourceAction(ResourceActionEvent resourceActionEvent) {
        switch (resourceActionEvent.getResourceAction()) {
            case FILTER:
                Intent intent = new Intent(this, PanelFilteringActivity.class);

                intent.putExtra(IntentConstants.EXTRA_RESOURCES_FILTERS, Parcels.wrap(getAvailableFilters()));
                intent.putExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS, Parcels.wrap(getActiveFilters()));
                intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_COUNT, getProducts() != null ? getProducts().size() : 0);
                intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_RESOURCE, Product.API_RESOURCE_NAME);
                intent.putExtra(IntentConstants.EXTRA_FILTER_MODE, IntentConstants.MULTI_CHOICE_FILTER);

                this.startActivityForResult(intent, IntentConstants.REQUEST_ITEMS_FILTERS);
                break;
        }

    }

    protected void loadRootCategory() {

        // Root category
        //TODO : manage loading of first level
        if (firstLevel) {
            catalogLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }
        String rootCategoryId = configHelper.getCatalogRootCategoryId();
        cataloguePresenter.getCatalogFromCategory(rootCategoryId, getString(R.string.catalog));
        ProductSearch search = new ProductSearch(ESearchMode.CATEGORY, rootCategoryId);

        runProductsSearch(search,
                new Pagination(0,  getResources().getInteger(R.integer.row_load_pagination)  * getResources().getInteger(R.integer.catalog_columns_count)),
                configHelper.getCatalogSortConfig(ESearchMode.CATEGORY),
                null);

    }


}
