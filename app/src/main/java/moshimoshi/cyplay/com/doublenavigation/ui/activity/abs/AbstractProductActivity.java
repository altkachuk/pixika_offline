package moshimoshi.cyplay.com.doublenavigation.ui.activity.abs;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Fee;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.history.ProductHistory;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.GoToAvailabilityEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSelectedSkuChangeEvent;

import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.HistoryContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.FeePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BasketBasedActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.ActivityTab;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ViewPagerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.ProductAvailabilityFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.listener.DisplayEventPagerListener;
import moshimoshi.cyplay.com.doublenavigation.utils.ListUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by romainlebouc on 19/12/2016.
 */

public abstract class AbstractProductActivity extends BasketBasedActivity implements FilterResourceView<Product, ProductFilter>, ResourceActivity<Product> {

    @Inject
    SellerContext sellerContext;

    @Inject
    protected CustomerContext customerContext;

    @Inject
    GetProductPresenter getProductPresenter;

    @Inject
    FeePresenter feePresenter;

    @Inject
    HistoryContext historyContext;

    @BindView(R.id.coordinator_layout)
    public CoordinatorLayout coordinatorLayout;

    @BindView(R.id.product_pager)
    ViewPager productViewPager;

    @BindView(R.id.product_tabs)
    TabLayout productTabLayout;

    private List<ProductFilter> productFilters;
    private String initialSkuId;

    protected ViewPagerAdapter adapter;

    protected Product product;
    protected Sku selectedSku;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayoutId());
        getProductPresenter.setView(this);
        feePresenter.setView(new ResourceView<List<Fee>>() {
            @Override
            public void onResourceViewResponse(ResourceResponseEvent<List<Fee>> resourceResponseEvent) {
                bus.post(resourceResponseEvent);
            }
        });
        this.loadResource();
        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        initViewPager();

        if (getIntent().hasExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS)) {
            productFilters = Parcels.unwrap(getIntent().getParcelableExtra(IntentConstants.EXTRA_RESOURCES_ACTIVE_FILTERS));
        }
        if (getIntent().hasExtra(IntentConstants.EXTRA_SKU_ID)) {
            initialSkuId = getIntent().getStringExtra(IntentConstants.EXTRA_SKU_ID);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        selectedSku = Parcels.unwrap(savedInstanceState.getParcelable(IntentConstants.EXTRA_SKU));
        bus.post(new ProductSelectedSkuChangeEvent(selectedSku));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(IntentConstants.EXTRA_SKU, Parcels.wrap(selectedSku));
    }

    public abstract int getContentViewLayoutId();

    public int getInitialSelectedSkuPosition() {
        return product.getFilteredSkuPosition(this, initialSkuId, productFilters);
    }

    private void initViewPager() {
        for (ActivityTab customerActivityTab : configHelper.getProductActivityTabs(this, customerContext)) {
            try {
                adapter.addFragment((Fragment) customerActivityTab.getFragmentClass().newInstance(),
                        customerActivityTab.getTitle(this));
            } catch (InstantiationException | IllegalAccessException e) {
                Log.e(AbstractProductActivity.class.getName(), e.getMessage(), e);
            }
        }
        // fillStocks viewPager
        productViewPager.setAdapter(adapter);
        productViewPager.addOnPageChangeListener(new DisplayEventPagerListener(adapter));
        // fillStocks tabLayout
        productTabLayout.setupWithViewPager(productViewPager);

    }

    @Override
    public Product getResource() {
        return this.product;
    }

    @Override
    public void loadResource() {
        if (getIntent().hasExtra(IntentConstants.EXTRA_PRODUCT_ID)) {
            String productId = getIntent().getStringExtra(IntentConstants.EXTRA_PRODUCT_ID);
            getProductPresenter.getProduct(productId, Product.PRODUCT_PROJECTION);
            bus.post(new ResourceRequestEvent<Product>(EResourceType.PRODUCT, productId));
        }
    }

    public Sku getSelectedSku() {
        return selectedSku;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public void onResourceViewResponse(FilterResourceResponseEvent<Product, ProductFilter> resourceResponseEvent) {
        if (EResourceType.PRODUCT == resourceResponseEvent.getEResourceType()) {
            // get product
            this.product = resourceResponseEvent.getResource();

            // set header
            if (product != null) {
                Collections.sort(product.getSkus(), new Comparator<Sku>() {
                    @Override
                    public int compare(Sku sku2, Sku sku1) {
                        return ListUtils.compare(sku2.getName(), sku1.getName());
                    }
                });
                adapter.notifyDataSetChanged();
            }

            if (bus != null) {
                bus.post(resourceResponseEvent);
            }
            historyContext.addProductHistory(new ProductHistory(sellerContext.getSeller().getId(), this.product, new Date()));
        } else if (EResourceType.PRODUCT_STOCK == resourceResponseEvent.getEResourceType()) {
            if (this.product != null) {
                if (resourceResponseEvent.getResource() != null) {
                    this.product.updateStock(resourceResponseEvent.getResource());
                    bus.post(resourceResponseEvent);
                } else if (resourceResponseEvent.getResourceException() != null) {
                    bus.post(resourceResponseEvent);
                    SnackBarHelper.buildSnackbar(coordinatorLayout,
                            this.getString(R.string.product_stock_error),
                            Snackbar.LENGTH_SHORT,
                            this.getString(android.R.string.ok)).show();

                }
            }
        }
    }

    @Subscribe
    public void onProductSelectedSkuChangeEvent(ProductSelectedSkuChangeEvent productSelectedSkuChangeEvent) {
        this.selectedSku = productSelectedSkuChangeEvent.getSku();
        // on sku changes
        if (configHelper.shouldGetStockAsynchronously() && product != null) {
            boolean skuAvailabilitesFilled = product.isSkuAvailabilitesFilled(selectedSku);
            if (!skuAvailabilitesFilled) {
                bus.post(new ResourceRequestEvent<Product>(EResourceType.PRODUCT_STOCK));
                getProductPresenter.getProductStock(product.getId(), selectedSku.getId());
            } else {
                bus.post(new ResourceResponseEvent(product, null, EResourceType.PRODUCT_STOCK));
            }
        }

        if (configHelper.getProductConfig().getFees().getExists()) {
            feePresenter.getFees(product.getId(),
                    selectedSku.getId(),
                    configHelper.getCurrentShop().getId());
        }


    }

    @Subscribe
    public void onGoToAvailabilityEvent(GoToAvailabilityEvent event) {
        int availabilityPos = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).getClass() == ProductAvailabilityFragment.class) {
                availabilityPos = i;
                break;
            }
        }
        productViewPager.setCurrentItem(availabilityPos);
    }
}
