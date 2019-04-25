package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.CatalogueLevel;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceActionEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractCatalogActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ESearchMode;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ProductSearch;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.FilterableSortableProductsFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductFilterSortFragment;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;


public class CatalogActivity extends AbstractCatalogActivity {

    @Nullable
    @BindView(R.id.state_loading_view)
    LoadingView catalogLoadingView;


    private FilterableSortableProductsFragment filterableSortableProductsFragment;

//    private boolean onRotate = false;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public int getContentViewLayoutId() {
        return R.layout.activity_catalog;
    }


    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        //before backpressed
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            hideProductsAndFilterFragments();
        }
        super.onBackPressed();
        //after backpressed
        CatalogCategoriesFragment mFragment = (CatalogCategoriesFragment) getSupportFragmentManager().findFragmentByTag(CATALOGUE_FRAGMENT_TAG);
        // For All Button
        if (mFragment != null && mFragment.getCategoryCurrent() != null) {
            currentCategory = new Category(mFragment.getCategoryCurrent());
            if (mFragment.getCategoryParent() != null) {
                previousCategory = new Category(mFragment.getCategoryParent());
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
////            //landscape
//            productSearchResultLayout.setVisibility(View.VISIBLE);
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            productSearchResultLayout.setVisibility(View.GONE);
//        }


//        initialize();
////        onRotate = true;
//        if (firstPage) {
//            firstLevel = true;
//            currentSale = null;
//            previousCategory = null;
//        } else {
//            firstLevel = false;
//        }
//
//        //test the current orientation
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            //landscape
//            if (currentSale == null) {
//                loadRootCategory();
//                if (currentSearch != null) {
//                    loadExtraProducts(new Pagination(0, 6 * getResources().getInteger(R.integer.product_search_columns_count)),
//                            null, activeFilters);
//                }
//            } else {
//                displayNextCatalogPage(currentSale);
//            }
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            //portrait
//            if (currentSale == null) {
//                loadRootCategory();
//            } else {
//                displayNextCatalogPage(currentSale);
//            }
//        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void hideProductsAndFilterFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction filterFragmentTransaction = fragmentManager.beginTransaction();

        if (productFilterSortFragment != null)
            filterFragmentTransaction.hide(productFilterSortFragment);
        if (filterableSortableProductsFragment != null)
            filterFragmentTransaction.hide(filterableSortableProductsFragment);

        filterFragmentTransaction.commitAllowingStateLoss();
        productFilterSortFragment = null;
        filterableSortableProductsFragment = null;
    }


//    //load page of products at the right of layout
//    private void loadCategoryPageLandscape(String categoryId) {
//
//        filterableSortableProductsFragment = new FilterableSortableProductsFragment();
//        productFilterSortFragment = new ProductFilterSortFragment();
//
//        // when loading we cannot click on other category
//        disableClickView.setVisibility(View.VISIBLE);
//        // Display Products
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        initProductFilterSortFragment(fragmentManager);
//
//        FragmentTransaction categoryFragmentTransaction = fragmentManager.beginTransaction();
//        categoryFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
//        categoryFragmentTransaction.replace(R.id.product_results_container, filterableSortableProductsFragment);
////        categoryFragmentTransaction.addToBackStack(null);
//        categoryFragmentTransaction.commitAllowingStateLoss();
//
//        // Run the search
//        runSearch(categoryId);
//    }

    //load final page of products
    private void loadCategoryPagePortrait(Category category) {

        filterableSortableProductsFragment = new FilterableSortableProductsFragment();
        productFilterSortFragment = new ProductFilterSortFragment();

        // when loading we cannot click on other category
        disableClickView.setVisibility(View.VISIBLE);
        // Display Products
        FragmentManager fragmentManager = getSupportFragmentManager();

        initProductFilterSortFragment(fragmentManager);

        filterableSortableProductsFragment.setCategoryCurrent(new Category(category));
        this.setProducts(null);
        FragmentTransaction categoryFragmentTransaction = fragmentManager.beginTransaction();
        categoryFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        categoryFragmentTransaction.replace(R.id.category_container, filterableSortableProductsFragment);
        categoryFragmentTransaction.addToBackStack(null);
        categoryFragmentTransaction.commitAllowingStateLoss();

        runSearch(category.getId());
    }

    private void initProductFilterSortFragment(FragmentManager manager) {
        FragmentTransaction filterFragmentTransaction = manager.beginTransaction();
        filterFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        filterFragmentTransaction.replace(R.id.catalog_sort_container, productFilterSortFragment);
//        filterFragmentTransaction.addToBackStack(null);
        filterFragmentTransaction.commitAllowingStateLoss();
    }

    private void runSearch(String categoryId) {
        // Run the search
        ProductSearch search = new ProductSearch(ESearchMode.CATEGORY, categoryId);

        runProductsSearch(search,
                new Pagination(0, getResources().getInteger(R.integer.row_load_pagination) * getResources().getInteger(R.integer.catalog_columns_count)),
                configHelper.getCatalogSortConfig(ESearchMode.CATEGORY),
                null);
    }


    protected void loadRootCategory() {
        // Root category
        if (firstLevel && catalogLoadingView != null) {
            catalogLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }

        String rootCategoryId = configHelper.getCatalogRootCategoryId();
        cataloguePresenter.getCatalogFromCategory(rootCategoryId, getString(R.string.catalog));

//        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//            loadCategoryPageLandscape(rootCategoryId);
//        }

    }


    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------


    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------


    @OnClick(R.id.reload_button)
    public void onReloadClick() {
        loadRootCategory();
    }

    @OnClick(R.id.disable_click)
    public void onDisableViewClick() {
        // Do nothing to catch click
    }

    public void displayNextCatalogPage(Category category) {
        previousCategory = currentCategory;
        currentCategory = category;

        if (!isDestroyed()) {
            if (category != null) {
                if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    displayCatalogPagePortrait(category);
                } else {
//                    displayCatalogPageLandscape(category);
                }

            }
        }

    }
//
//    private void displayCatalogPageLandscape(Category category) {
//        // Display Categories
//        cataloguePresenter.getCatalogFromCategory(category.getCustomerId(), category.getName());
//        //TODO : what to do at last level ??
//        loadCategoryPageLandscape(category.getCustomerId());
//    }

    private void displayCatalogPagePortrait(Category category) {
        // Display Categories
        if (category.getHas_sub_families()) {
            cataloguePresenter.getCatalogFromCategory(category.getId(), category.getName());
        } else {
            loadCategoryPagePortrait(category);
        }
    }


    @Override
    public void loadResource(Pagination pagination) {
        if (filterableSortableProductsFragment != null) {
            filterableSortableProductsFragment.loadResource(pagination);
        }
    }

    @Override
    public void onBackBtnClicked() {
        onBackPressed();
    }

    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent catalogueLevelResourceRequestEvent) {
        if (catalogueLevelResourceRequestEvent.getEResourceType() == EResourceType.CATALOG_LEVEL) {
            disableClickView.setVisibility(View.VISIBLE);
        }
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


    @Override
    public void onResourceViewResponse(ResourceResponseEvent<CatalogueLevel> resourceResponseEvent) {
        if (resourceResponseEvent.getEResourceType() == EResourceType.CATALOG_LEVEL) {
            if (resourceResponseEvent.getResourceException() == null) {
                CatalogueLevel catalogueLevel = resourceResponseEvent.getResource();
                if (!this.isDestroyed()) {
                    if (catalogueLevel != null && catalogueLevel.getCategory() != null && catalogueLevel.getCategory().getId() != null) {
                        // view loaded
                        if (catalogLoadingView != null) {
                            catalogLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
                        }
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
                            currentCategory = new Category(catalogueLevel.getCategory());
                            fragment.setCategoryCurrent(new Category(catalogueLevel.getCategory()));
                            fragmentTransaction.add(R.id.category_container, fragment, CATALOGUE_FRAGMENT_TAG);

                            //do productsSearch when orientation is landscape
//                            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//                                runSearch(catalogueLevel.getCategory().getCustomerId());
//                            }
                            firstPage = true;
                            firstLevel = false;
                        } else {
                            // For All Button
                            fragment.setCategoryCurrent(new Category(catalogueLevel.getCategory()));

                            // For previous button
                            fragment.setCategoryParent(new Category(this.previousCategory));
                            // Fragment Transaction
                            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                            fragmentTransaction.replace(R.id.category_container, fragment, CATALOGUE_FRAGMENT_TAG);
                            //TODO: after the rotation, the same fragment add to back stack again!!!!!?
//                            if (!onRotate) {
                            fragmentTransaction.addToBackStack(null);
//                                onRotate = false;
//                            }
                            firstPage = false;
                        }
                        fragmentTransaction.commitAllowingStateLoss();

//                        if (catalogueLevel.getCategory() != null) {
//                            this.previousCategory = new Category(catalogueLevel.getCategory());
//                        }
                    }

                }
            } else {
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

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        //TODO
    }

    @Subscribe
    public void onResourceAction(ResourceActionEvent resourceActionEvent) {
        switch (resourceActionEvent.getResourceAction()) {
            case FILTER:
                Intent intent;
                switch (this.getResources().getConfiguration().orientation) {
                    case ORIENTATION_LANDSCAPE:
                        intent = new Intent(this, PanelFilteringActivity.class);

                        intent.putExtra(IntentConstants.EXTRA_RESOURCES_FILTERS, Parcels.wrap(getAvailableFilters()));
                        intent.putExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS, Parcels.wrap(getActiveFilters()));
                        intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_COUNT, getProducts() != null ? getProducts().size() : 0);
                        intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_RESOURCE, Product.API_RESOURCE_NAME);
                        intent.putExtra(IntentConstants.EXTRA_FILTER_MODE, IntentConstants.MULTI_CHOICE_FILTER);

                        this.startActivityForResult(intent, IntentConstants.REQUEST_ITEMS_FILTERS);
                        break;
                    case ORIENTATION_PORTRAIT:
                    default:
                        intent = new Intent(this, FullScreenFilteringActivity.class);

                        intent.putExtra(IntentConstants.EXTRA_RESOURCES_FILTERS, Parcels.wrap(getAvailableFilters()));
                        intent.putExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS, Parcels.wrap(getActiveFilters()));
                        intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_COUNT, getProducts() != null ? getProducts().size() : 0);
                        intent.putExtra(IntentConstants.EXTRA_RESOURCE_FILTER_RESOURCE, Product.API_RESOURCE_NAME);
                        intent.putExtra(IntentConstants.EXTRA_FILTER_MODE, IntentConstants.MULTI_CHOICE_FILTER);

                        this.startActivityForResult(intent, IntentConstants.REQUEST_ITEMS_FILTERS);
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
                        break;

                }
        }
    }


}
