package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.FilterableSortableProductsComponent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ProductsSearchActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.PaginatedResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductItemThumbnailAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.EndlessRecyclerOnScrollListener;
import moshimoshi.cyplay.com.doublenavigation.utils.URLUtils;
import ninja.cyplay.com.apilibrary.models.meta.Pagination;
import ninja.cyplay.com.apilibrary.utils.ActivityHelper;

/**
 * Created by romainlebouc on 19/08/16.
 */
public class FilterableSortableProductsFragment extends PaginatedResourceBaseFragment<Product> {

    // Product Search
    @Nullable
    @BindView(R.id.product_search_recycler_view)
    RecyclerView productSearchRecyclerView;

    @Nullable
    @BindView(R.id.extra_product_search)
    public View extraProductSearch;

    private FilterableSortableProductsComponent filterableSortableProductsComponent;
    private ProductItemThumbnailAdapter productsAdapter;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent;

    private Category categoryCurrent;
    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filterableSortableProductsComponent = (FilterableSortableProductsComponent) this.getActivity();

        productsAdapter = new ProductItemThumbnailAdapter(this.getContext(),
                filterableSortableProductsComponent.getProductsColumnCount(),
                filterableSortableProductsComponent.getActiveFilters());
        initSearchRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (filterableSortableProductsComponent.getProducts() == null) {
            loadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        }
        updateActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filterable_sortable_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if  (! (this.getActivity() instanceof ProductsSearchActivity)){
            extraProductSearch.setVisibility(View.GONE);
        }
    }


    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void updateActionBar(){
        ActionBar actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
        if (categoryCurrent != null && categoryCurrent.getName() != null && actionBar != null) {
            actionBar.setTitle(this.categoryCurrent.getName());
        }
    }

    private void initSearchRecyclerView() {
        boolean includeEdge = true;
        int spacing = (int) getResources().getDimension(R.dimen.contextual_small_margin);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), filterableSortableProductsComponent.getProductsColumnCount());
        productSearchRecyclerView.setHasFixedSize(true);

        // set layout
        productSearchRecyclerView.setLayoutManager(layoutManager);
        // Add Decorator spacing between items
        productSearchRecyclerView.addItemDecoration(new GridSpacingItemDecoration(filterableSortableProductsComponent.getProductsColumnCount(), spacing, includeEdge));
        // Add Animation
        productSearchRecyclerView.setItemAnimator(new FadeInAnimator());
        // link view with Adapter
        productSearchRecyclerView.setAdapter(productsAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager, getResources().getInteger(R.integer.row_load_pagination) * filterableSortableProductsComponent.getProductsColumnCount()) {
            @Override
            public void onLoadMore(int current_page) {
                try {
                    if (resourceResponseEvent != null && resourceResponseEvent.getNext() != null) {
                        Pagination pagination = URLUtils.getOffSetAndLimitFromQuery(new URL(resourceResponseEvent.getNext()));
                        loadResource(new Pagination(pagination.getOffset(), pagination.getLimit()));
                    }
                } catch (MalformedURLException | UnsupportedEncodingException e) {
                    Log.e(FilterableSortableProductsFragment.class.getName(), e.getMessage());
                }
            }
        };
        productSearchRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
        productSearchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    ActivityHelper.hideSoftKeyboard(FilterableSortableProductsFragment.this.getActivity());
                }
            }

        });
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void setResource(List<Product> products, boolean extra) {
        if (!extra) {
            ActivityHelper.hideSoftKeyboard(this.getActivity());
            filterableSortableProductsComponent.setProducts(products);
            productsAdapter.setItems(filterableSortableProductsComponent.getProducts());
            endlessRecyclerOnScrollListener.clear();
        } else {
            List<Product> componentProducts = filterableSortableProductsComponent.getProducts();
            int insertPosition = componentProducts == null ? 0 : componentProducts.size();

            if (componentProducts != null && products != null) {
                componentProducts.addAll(products);
                productsAdapter.notifyItemInserted(insertPosition);
            }
        }
    }

    @Override
    public List<Product> getCachedResource() {
        return filterableSortableProductsComponent.getProducts();
    }

    @Override
    public void loadResource(Pagination pagination) {
        ((FilterableSortableProductsComponent) this.getActivity()).loadExtraProducts(pagination,
                filterableSortableProductsComponent.getResourceFieldSorting(),
                filterableSortableProductsComponent.getActiveFilters());
    }

    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------

    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent<List<Product>> resourceRequestEvent) {
        if (resourceRequestEvent.getEResourceType() == EResourceType.PRODUCTS) {
            super.onResourceRequest(resourceRequestEvent, resourceRequestEvent.isExtra());
        }
    }

    @Subscribe
    public void onResourceResponseEvent(FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent) {
        if (resourceResponseEvent.getEResourceType() == EResourceType.PRODUCTS) {
            this.resourceResponseEvent = resourceResponseEvent;
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    public void setCategoryCurrent(Category categoryCurrent) {
        this.categoryCurrent = categoryCurrent;
    }

}

