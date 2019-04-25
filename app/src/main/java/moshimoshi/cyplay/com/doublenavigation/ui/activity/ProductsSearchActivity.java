package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSuggestion;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.ESearchSuggestionMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterNumberChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceActionEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSearchPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSuggestionPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.Constants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ESearchMode;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ProductSearch;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.FilterableSortableProductsFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductFilterSortFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductHistoryFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductSuggestionsFragment;
import moshimoshi.cyplay.com.doublenavigation.utils.CompatUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;
import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_CURRENT_ACTIVE_FILTERS;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_CURRENT_SEARCH;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_SEARCH_QUERY;

/**
 * Created by damien on 10/05/16.
 */
public class ProductsSearchActivity extends MenuBaseActivity implements FilterableSortableProductsComponent<ProductFilter> {

    public final static int PRODUCT_SUGGESTION_QUERY_LENGTH_THRESHOLD = 3;

    @Inject
    ProductSearchPresenter productSearchPresenter;

    @Inject
    ProductSuggestionPresenter productSuggestionPresenter;

    @Inject
    GetProductsPresenter getProductsPresenter;

    @BindView(R.id.product_search)
    EditText productSearchQuery;

    @BindView(R.id.product_search_clear)
    ImageView clearSeach;

    @BindView(R.id.product_search_button)
    View productSearchButton;

    @BindView(R.id.product_search_results_container)
    View productSearchResultsContainer;

    @BindView(R.id.extra_product_search)
    View productSearchDefaultView;

    @BindView(R.id.product_search_container)
    View productSearchContainer;


    private ProductSuggestionsFragment productSuggestionsFragment = new ProductSuggestionsFragment();
    private FilterableSortableProductsFragment filterableSortableProductsFragment = new FilterableSortableProductsFragment();
    private ProductHistoryFragment productHistoryFragment;

    private ProductFilterSortFragment productFilterSortFragment;
    // String flag to know if a suggestion Query is in progress
    private String currentSuggestionQuery = null;
    private boolean suggestionQueryInProgress = false;

    private List<Product> products;
    private ProductSearch currentSearch;
    private ESearchSuggestionMode currentMode = null;  //ESearchSuggestionMode.NONE;

    private List<ProductFilter> availableFilters;
    private final List<ProductFilter> activeFilters = new ArrayList<>();

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search_2);
        // Update design
        updateDesign();
        // Set presenter's view
        productSuggestionPresenter.setView(new ResourceView<ProductSuggestion>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<ProductSuggestion> resourceResponseEvent) {
                bus.post(resourceResponseEvent);
                suggestionQueryInProgress = false;
                runSuggestion(currentSuggestionQuery, false);
            }
        });
        productSearchPresenter.setView(new FilterResourceView<List<Product>, ProductFilter>() {
            @Override
            public void onResourceViewResponse(FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent) {
                bus.post(resourceResponseEvent);
            }
        });
        getProductsPresenter.setView(new FilterResourceView<List<Product>, ProductFilter>() {
            @Override
            public void onResourceViewResponse(FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent) {
                bus.post(resourceResponseEvent);
            }
        });

        initListeners();
        productFilterSortFragment = (ProductFilterSortFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_filter_sort);


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (currentMode == null) {
            this.setVisibilityMode(ESearchSuggestionMode.NONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentMode == ESearchSuggestionMode.NONE) {
            this.setVisibilityMode(ESearchSuggestionMode.NONE);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getSearchInfosFromBundle(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveSearchInfos(outState);
        super.onSaveInstanceState(outState);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_SEARCH_PRODUCT.getCode();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void saveSearchInfos(Bundle bundle) {
        bundle.putString(EXTRA_SEARCH_QUERY, productSearchQuery.getText().toString());
        bundle.putParcelable(EXTRA_CURRENT_SEARCH, Parcels.wrap(currentSearch));
        bundle.putParcelable(EXTRA_CURRENT_ACTIVE_FILTERS, Parcels.wrap(activeFilters));
    }

    private void getSearchInfosFromBundle(Bundle bundle) {
        String queryStr = bundle.getString(EXTRA_SEARCH_QUERY, "");
        productSearchQuery.setText(queryStr);
        currentSearch = Parcels.unwrap(bundle.getParcelable(EXTRA_CURRENT_SEARCH));
        this.getActiveFilters().clear();
        this.getActiveFilters().addAll((List<ProductFilter>) Parcels.unwrap(bundle.getParcelable(EXTRA_CURRENT_ACTIVE_FILTERS)));
        if (currentSearch != null) {
            bus.post(new FilterNumberChangeEvent(this.getActiveFilters().size()));
            loadExtraProducts(new Pagination(0, getResources().getInteger(R.integer.row_load_pagination) * getResources().getInteger(R.integer.product_search_columns_count)),
                    null, activeFilters);
        }
    }

    private void updateDesign() {
        for (Drawable drawable : productSearchQuery.getCompoundDrawables()) {
            if (drawable != null) {
                CompatUtils.setDrawableTint(drawable, ContextCompat.getColor(this, R.color.search_gray));

            }
        }

        productSearchButton.setEnabled(false);
    }

    private void initListeners() {
        // Search on keyboard click
        productSearchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchProducts();
                    return true;
                }
                return false;
            }
        });

        // Suggest on value bar typing
        productSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                runSuggestion(currentSuggestionQuery, true);
                productFilterSortFragment.setVisible(false);

                if (s.length() > 0) {
                    clearSeach.setVisibility(View.VISIBLE);
                    productSearchButton.setEnabled(true);
                } else {
                    clearSeach.setVisibility(View.GONE);
                    productSearchButton.setEnabled(false);
                }

            }
        });

    }

    private void runSuggestion(String currentSuggestionQuery, boolean changeMode) {
        String query = productSearchQuery.getText().toString().trim();
        if (changeMode || (!changeMode && currentMode == ESearchSuggestionMode.SUGGESTION)) {
            if (query != null && query.length() >= PRODUCT_SUGGESTION_QUERY_LENGTH_THRESHOLD) {
                setVisibilityMode(ESearchSuggestionMode.SUGGESTION);
                if (!suggestionQueryInProgress && !query.equals(currentSuggestionQuery)) {
                    this.currentSuggestionQuery = query;
                    suggestionQueryInProgress = true;
                    productSuggestionPresenter.suggestProducts(query);
                }

            } else {
                setVisibilityMode(ESearchSuggestionMode.NONE);
                this.currentSuggestionQuery = query;
            }
        }

    }

    private void runSearch(ProductSearch search,
                           Pagination pagination,
                           ResourceFieldSorting resourceFieldSorting,
                           List<ProductFilter> resourceFilters) {
        if (search != null && search.getMode() != null && search.getValue() != null && search.getValue().trim().length() > 0) {

            currentSearch = search;
            setVisibilityMode(ESearchSuggestionMode.SEARCH);
            bus.post(new ResourceRequestEvent<List<Product>>(EResourceType.PRODUCTS, null, pagination.isExtraData()));

            if (!pagination.isExtraData()) {
                products = null;
            }
            switch (search.getMode()) {
                case TEXT:
                    productSearchPresenter.searchProduct(search.getValue().trim(),
                            Product.PRODUCT_PREVIEW_PROJECTION,
                            pagination,
                            resourceFieldSorting,
                            resourceFilters
                    );
                    break;
                case CATEGORY:
                    getProductsPresenter.getProductsForFamily(search.getValue(),
                            Product.PRODUCT_PREVIEW_PROJECTION,
                            pagination,
                            resourceFieldSorting,
                            resourceFilters);
                    break;
                case BRAND:
                    getProductsPresenter.getProductsForBrand(search.getValue(),
                            Product.PRODUCT_PREVIEW_PROJECTION,
                            pagination,
                            resourceFieldSorting,
                            resourceFilters);
                    break;
            }
        }
    }

    public void setVisibilityMode(ESearchSuggestionMode searchSuggestionMode) {

        if (!isDestroyed()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (searchSuggestionMode) {
                case SUGGESTION:
                    fragmentTransaction.show(productSuggestionsFragment);
                    fragmentTransaction.replace(R.id.product_search_results_container, productSuggestionsFragment);
                    productSearchDefaultView.setVisibility(View.INVISIBLE);
                    break;
                case SEARCH:
                    fragmentTransaction.show(filterableSortableProductsFragment);
                    fragmentTransaction.replace(R.id.product_search_results_container, filterableSortableProductsFragment);
                    productSearchDefaultView.setVisibility(View.GONE);
                    break;
                case NONE:
                    if (configHelper.getConfig().getFeature().getProductConfig().getSearch().getHistory().getEnable()) {
                        productHistoryFragment = new ProductHistoryFragment();
                        fragmentTransaction.show(productHistoryFragment);
                        fragmentTransaction.replace(R.id.product_search_results_container, productHistoryFragment, ProductHistoryFragment.PRODUCT_HISTORY_FRAGMENT_TAG);
                    } else {
                        fragmentTransaction.hide(productSuggestionsFragment);
                        fragmentTransaction.hide(filterableSortableProductsFragment);
                        productSearchDefaultView.setVisibility(View.VISIBLE);
                    }
                    break;
            }

            this.animateSuggestions(searchSuggestionMode);
            currentMode = searchSuggestionMode;
            fragmentTransaction.commitAllowingStateLoss();
        }

    }

    private void animateSuggestions(ESearchSuggestionMode searchSuggestionMode) {
        ValueAnimator foreGroundBackGroundSearchLuminosityValueAnimator = null;
        ValueAnimator whiteBackGroundSearchLuminosityValueAnimator = null;

        if ((currentMode == ESearchSuggestionMode.NONE || currentMode == ESearchSuggestionMode.SEARCH)
                && searchSuggestionMode == ESearchSuggestionMode.SUGGESTION) {
            foreGroundBackGroundSearchLuminosityValueAnimator = ValueAnimator.ofInt(Constants.SEARCH_BACKGROUND_LUMINOSITY, Constants.SEARCH_FOREGROUND_LUMINOSITY);
            whiteBackGroundSearchLuminosityValueAnimator = ValueAnimator.ofInt(255, Constants.SEARCH_FOREGROUND_LUMINOSITY);

        } else if (currentMode == ESearchSuggestionMode.SUGGESTION
                && (searchSuggestionMode == ESearchSuggestionMode.NONE || searchSuggestionMode == ESearchSuggestionMode.SEARCH)) {
            foreGroundBackGroundSearchLuminosityValueAnimator = ValueAnimator.ofInt(Constants.SEARCH_FOREGROUND_LUMINOSITY, Constants.SEARCH_BACKGROUND_LUMINOSITY);
            whiteBackGroundSearchLuminosityValueAnimator = ValueAnimator.ofInt(Constants.SEARCH_FOREGROUND_LUMINOSITY, 255);
        } else if (searchSuggestionMode == ESearchSuggestionMode.SEARCH) {
            productSearchResultsContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.default_search_background));
            productSearchContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.default_search_background));
            productSearchDefaultView.setBackgroundColor(ContextCompat.getColor(this, R.color.default_search_background));
        }
        if (foreGroundBackGroundSearchLuminosityValueAnimator != null) {
            foreGroundBackGroundSearchLuminosityValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int color = (int) animation.getAnimatedValue();
                    productSearchContainer.setBackgroundColor(Color.rgb(color, color, color));
                    productSearchDefaultView.setBackgroundColor(Color.rgb(color, color, color));
                    if (filterableSortableProductsFragment.extraProductSearch != null) {
                        filterableSortableProductsFragment.extraProductSearch.setBackgroundColor(Color.rgb(color, color, color));
                    }
                }
            });
            whiteBackGroundSearchLuminosityValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int color = (int) animation.getAnimatedValue();
                    productSearchResultsContainer.setBackgroundColor(Color.rgb(color, color, color));
                }
            });

            foreGroundBackGroundSearchLuminosityValueAnimator.setDuration(256);
            whiteBackGroundSearchLuminosityValueAnimator.setDuration(256);
            foreGroundBackGroundSearchLuminosityValueAnimator.start();
            whiteBackGroundSearchLuminosityValueAnimator.start();
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void loadProducts(ProductSearch search, Pagination pagination) {
        runSearch(search, pagination, null, null);
    }

    @Override
    public void loadExtraProducts(Pagination pagination,
                                  ResourceFieldSorting resourceFieldSorting,
                                  List<ProductFilter> resourceFilters) {
        runSearch(currentSearch, pagination, resourceFieldSorting, resourceFilters);
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick({R.id.product_search_button})
    public void onSearchProducts() {
        searchProducts();
    }

    @OnClick({R.id.product_search_clear})
    public void onClearSearch() {
        productFilterSortFragment.setVisible(false);
        productSearchQuery.setText(StringUtils.EMPTY_STRING);
        currentSuggestionQuery = StringUtils.EMPTY_STRING;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(productSearchQuery, InputMethodManager.SHOW_IMPLICIT);
    }


    private void searchProducts() {
        clearFilters();
        productFilterSortFragment.clearSorting();
        products = null;
        ProductSearch search = new ProductSearch(ESearchMode.TEXT,
                productSearchQuery.getText().toString());
        loadProducts(search, new Pagination(0, getResources().getInteger(R.integer.row_load_pagination) * getResources().getInteger(R.integer.product_search_columns_count)));
    }

    @Override
    public void clearFilters() {
        if (activeFilters != null) {
            activeFilters.clear();
        }
        if (availableFilters != null) {
            availableFilters.clear();
        }
        bus.post(new FilterNumberChangeEvent(getActiveFilters().size()));
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

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public ResourceFieldSorting getResourceFieldSorting() {
        return productFilterSortFragment.getResourceFieldSorting();
    }

    @Override
    public void loadResource(Pagination pagination) {
        filterableSortableProductsFragment.loadResource(pagination);
    }

    @Override
    public int getProductsColumnCount() {
        return this.getResources().getInteger(R.integer.product_search_columns_count);
    }

    @Override
    public void onBackBtnClicked() {

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


    // -------------------------------------------------------------------------------------------
    //                                      Event(s)
    // -------------------------------------------------------------------------------------------

    @Subscribe
    public void onResourceResponseEvent(FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent) {
        if (resourceResponseEvent.getEResourceType() == EResourceType.PRODUCTS) {
            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                int size = resourceResponseEvent != null ? resourceResponseEvent.getCount() : 0;
                actionBar.setTitle(this.getResources().getQuantityString(R.plurals.products_count, size, size));
            }
        }
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

