package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductsPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductItemThumbnailAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;

/**
 * Created by damien on 17/04/16.
 */
public class ProductSubstitutionFragment extends ResourceBaseFragment<Object> implements FilterResourceView<List<Product>, ProductFilter> {

    @Inject
    EventBus bus;

    @Inject
    GetProductsPresenter productsPresenter;

    @BindView(R.id.product_related_recycler_view)
    RecyclerView recyclerView;

    private Product product;
    private ProductItemThumbnailAdapter adapter;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ProductItemThumbnailAdapter(getContext(), this.getResources().getInteger(R.integer.product_search_columns_count));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_substitution, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set Presenter's view
        productsPresenter.setView(this);
        // fillStocks recycler
        initRecycler();
        loadResource();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------


    private void initRecycler() {
        int spanCount = 3; // n columns
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        // Add 10dp spacing between items
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
    }


    private void runSubstitutionProductRequest() {
        Product product = ((ResourceActivity<Product>)this.getActivity()).getResource();
        if (product != null){
            productsPresenter.getProductsFromIds(product.getSubstitute_prod_ids(), Product.PRODUCT_PREVIEW_PROJECTION);
        }

    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    protected void setResource(Object products) {
        if ( products !=null){
            adapter.setItems((List<ProductItem>) products);
        }
    }

    @Override
    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Object> resourceResponseEvent) {
        if (EResourceType.PRODUCT == resourceResponseEvent.getEResourceType()){
            product = (Product)resourceResponseEvent.getResource();
            // Run request
            runSubstitutionProductRequest();
        }else if (EResourceType.PRODUCTS == resourceResponseEvent.getEResourceType()){
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Override
    public void onResourceRequestEvent(ResourceRequestEvent<Object> resourceRequestEvent) {

    }

    @Override
    public Object getCachedResource() {
        Product product = ((ResourceActivity<Product>)this.getActivity()).getResource();
        return product.getSubstitute_products();
    }

    @Override
    public void loadResource() {
        runSubstitutionProductRequest();
    }


    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.reload_button)
    public void onReloadSubstitutionProductButtonClick() {
        runSubstitutionProductRequest();
    }

    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public void onResourceViewResponse(FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent) {
        if ( EResourceType.PRODUCTS == resourceResponseEvent.getEResourceType()){
            bus.post(resourceResponseEvent);
        }
    }

}
