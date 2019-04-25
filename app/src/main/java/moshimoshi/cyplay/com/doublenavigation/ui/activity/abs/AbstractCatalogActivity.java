package moshimoshi.cyplay.com.doublenavigation.ui.activity.abs;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.CatalogueLevel;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterNumberChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.CataloguePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductsPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.EMenuAction;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FilterableSortableProductsComponent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ProductSearch;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductFilterSortFragment;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by romainlebouc on 20/12/2016.
 */

public abstract class AbstractCatalogActivity extends MenuBaseActivity implements ResourceView<CatalogueLevel>,
        FilterableSortableProductsComponent<ProductFilter> {


    protected final static String CATALOGUE_FRAGMENT_TAG = "CATALOGUE_FRAGMENT_TAG";
    private final static String CURRENT_CATEGORY_SAVED_STATE = "CURRENT_CATEGORY";
    private final static String PREVIOUS_CATEGORY = "PREVIOUS_CATEGORY";
    private final static String CURRENT_SEARCH = "CURRENT_SEARCH";
    private final static String CURRENT_ACTIVE_FILTERS = "CURRENT_ACTIVE_FILTERS";

    protected Boolean firstLevel = true;
    //boolean to judge is on the first category page
    protected Boolean firstPage = true;
    private ProductSearch currentSearch;
    // Catalog's Category
    protected Category previousCategory;
    protected Category currentCategory;


    @Inject
    protected CataloguePresenter cataloguePresenter;

    @Inject
    protected GetProductsPresenter getProductsPresenter;

    @BindView(R.id.coordinator_layout)
    protected CoordinatorLayout coordinatorLayout;

    @Nullable
    @BindView(R.id.catalog_category_products_count)
    protected TextView catalogCategoryProductsCount;

    @BindView(R.id.disable_click)
    protected View disableClickView;

    @Nullable
    @BindView(R.id.catalog_sort_container)
    protected View catalogSortContainer;

//    @Nullable
//    @BindView(R.id.product_results_container)
//    protected View productResultsContainer;

    @Nullable
    @BindView(R.id.product_search_results_container)
    protected View productSearchResultLayout;

    @Nullable
    @BindView(R.id.category_container)
    protected View categoryContainer;

    private List<ProductFilter> availableFilters;
    private List<ProductFilter> activeFilters = new ArrayList<>();
    private List<Product> products;

    protected ProductFilterSortFragment productFilterSortFragment;

    protected int currentOrientation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        //load category
        if (savedInstanceState != null) {
            firstPage = savedInstanceState.getBoolean("first_page", true);
            getSearchInfosFromBundle(savedInstanceState);
        } else {
            if (currentCategory == null) {
                loadRootCategory();
            }
        }
    }

    private void initialize(){
        setContentView(getContentViewLayoutId());

//        currentOrientation = this.getResources().getConfiguration().orientation;
        currentOrientation = Configuration.ORIENTATION_PORTRAIT;

        // Show back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // set presenter's view
        cataloguePresenter.setView(this);
        // Products presenter
        getProductsPresenter.setView(new FilterResourceView<List<Product>, ProductFilter>() {

            @Override
            public void onResourceViewResponse(FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent) {
                disableClickView.setVisibility(View.GONE);
                bus.post(resourceResponseEvent);
            }
        });

//        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//            productSearchResultLayout.setVisibility(View.VISIBLE);
//        }
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveCatalogInfos(outState);
        super.onSaveInstanceState(outState);
    }

    private void saveCatalogInfos(Bundle bundle) {
        bundle.putParcelable(CURRENT_CATEGORY_SAVED_STATE, Parcels.wrap(currentCategory));
        bundle.putParcelable(PREVIOUS_CATEGORY, Parcels.wrap(previousCategory));

        bundle.putParcelable(CURRENT_SEARCH, Parcels.wrap(currentSearch));
        bundle.putParcelable(CURRENT_ACTIVE_FILTERS, Parcels.wrap(activeFilters));

        bundle.putBoolean("first_page", firstPage);
    }

    private void getSearchInfosFromBundle(Bundle bundle) {
        currentCategory = Parcels.unwrap(bundle.getParcelable(CURRENT_CATEGORY_SAVED_STATE));
        previousCategory = Parcels.unwrap(bundle.getParcelable(PREVIOUS_CATEGORY));

        if (firstPage) {
            firstLevel = true;
            currentCategory = null;
            previousCategory = null;
        } else {
            firstLevel = false;
        }

        if (currentCategory == null) {
            loadRootCategory();
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                loadSearchPage(bundle);
            }
        } else {
            //if not do that, the page category will display the next sub category automatic
            currentCategory = previousCategory;
            displayNextCatalogPage(currentCategory);
        }

    }

    private void loadSearchPage(Bundle bundle) {
        currentSearch = Parcels.unwrap(bundle.getParcelable(CURRENT_SEARCH));
        if (bundle.getParcelable(CURRENT_ACTIVE_FILTERS) != null) {
            this.getActiveFilters().clear();
            this.getActiveFilters().addAll((List<ProductFilter>) Parcels.unwrap(bundle.getParcelable(CURRENT_ACTIVE_FILTERS)));
        }
        if (currentSearch != null) {
            loadExtraProducts(new Pagination(0, getResources().getInteger(R.integer.row_load_pagination) * getResources().getInteger(R.integer.product_search_columns_count)),
                    null, activeFilters);
        }
    }

    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_CATALOG.getCode();
    }


    protected abstract void loadRootCategory();

    protected abstract int getContentViewLayoutId();

    @Override
    public void loadProducts(ProductSearch search, Pagination pagination) {
        // run request
        runProductsSearch(search, pagination, null, null);
    }

    @Override
    public void loadExtraProducts(Pagination pagination,
                                  ResourceFieldSorting resourceFieldSorting,
                                  List resourceFilters) {
        runProductsSearch(currentSearch, pagination, resourceFieldSorting, resourceFilters);
    }


    protected void runProductsSearch(ProductSearch search,
                                     Pagination pagination,
                                     ResourceFieldSorting resourceFieldSorting,
                                     List<ProductFilter> resourceFilters) {

        if (search != null && search.getMode() != null && search.getValue().trim().length() > 0) {
            if (catalogCategoryProductsCount != null && !pagination.isExtraData()) {
                catalogCategoryProductsCount.setVisibility(View.INVISIBLE);
            }
            if (productFilterSortFragment != null && !pagination.isExtraData()) {
                productFilterSortFragment.setVisible(false);
            }
            currentSearch = search;
            bus.post(new ResourceRequestEvent<List<Product>>(EResourceType.PRODUCTS, null, pagination.isExtraData()));
            getProductsPresenter.getProductsForFamily(search.getValue(),
                    Product.PRODUCT_PREVIEW_PROJECTION,
                    pagination,
                    resourceFieldSorting,
                    resourceFilters);
        }
    }

    public abstract void displayNextCatalogPage(Category category);


    @Override
    public void clearFilters() {
        if (activeFilters != null) {
            activeFilters.clear();
        }
        if (availableFilters != null) {
            availableFilters.clear();
        }

    }

    @Override
    public List<ProductFilter> getAvailableFilters() {
        return availableFilters;
    }

    @Override
    public List<ProductFilter> getActiveFilters() {
        return activeFilters;
    }

    @Override
    public void setAvailableFilters(List<ProductFilter> productFilters) {
        this.availableFilters = productFilters;
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public ResourceFieldSorting getResourceFieldSorting() {
        return productFilterSortFragment != null ? productFilterSortFragment.getResourceFieldSorting() : null;
    }

    @Override
    public abstract void loadResource(Pagination pagination);

    @Override
    public int getProductsColumnCount() {
        return this.getResources().getInteger(R.integer.catalog_columns_count);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == IntentConstants.REQUEST_ITEMS_FILTERS) {
            // Make sure the request was successful
            if (resultCode == IntentConstants.RESULT_ITEMS_FILTERS) {
                // The user picked a contact.
                this.getActiveFilters().clear();
                this.getActiveFilters().addAll((List<ProductFilter>) Parcels.unwrap(data.getParcelableExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS)));
                this.setProducts(null);

                bus.post(new FilterNumberChangeEvent(getFilterCount()));
                // The Intent's data Uri identifies which contact was selected.
                // Do something with the contact here (bigger example below)
                this.loadResource(Pagination.getInitialPagingation());
            }
        }
    }

    private int getFilterCount() {
        int count = 0;
        if (this.getActiveFilters() != null && this.getActiveFilters().size() > 0) {
            for (ProductFilter filter : this.getActiveFilters()) {
                if (filter.getValues() != null && filter.getValues().size() > 0) {
                    count += filter.getValues().size();
                }
            }
        }
        return count;
    }
}
