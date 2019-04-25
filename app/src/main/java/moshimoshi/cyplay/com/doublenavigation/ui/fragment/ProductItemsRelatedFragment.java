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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductsPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductItemThumbnailAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.decorator.GridSpacingItemDecoration;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;

/**
 * Created by romainlebouc on 29/08/16.
 */
public abstract class ProductItemsRelatedFragment extends ResourceBaseFragment<Object> implements FilterResourceView<List<Product>, ProductFilter> {

    @Inject
    EventBus bus;

    @Inject
    GetProductsPresenter productsPresenter;

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.product_related_recycler_view)
    RecyclerView recyclerView;

    protected Product product;
    protected ProductItemThumbnailAdapter productItemThumbnailAdapter;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productItemThumbnailAdapter = new ProductItemThumbnailAdapter(getContext(), this.getResources().getInteger(R.integer.product_search_columns_count));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_related, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set Presenter's view
        productsPresenter.setView(this);
        // fillStocks recycler
        initRecycler();
        if (this.getCachedResource() == null) {
            loadResource();
        }
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
        recyclerView.setAdapter(productItemThumbnailAdapter);
        // Add 10dp spacing between items
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
    }

    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------

    @Override
    protected abstract void setResource(Object products) ;

    @Override
    public abstract Object getCachedResource() ;

    public abstract  void loadResource();

    @Override
    public void onResourceViewResponse(FilterResourceResponseEvent<List<Product>, ProductFilter> resourceResponseEvent) {

        if (EResourceType.PRODUCTS == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse((FilterResourceResponseEvent)resourceResponseEvent);
        }
    }
}
