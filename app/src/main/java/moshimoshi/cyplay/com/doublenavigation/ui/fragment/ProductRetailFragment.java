package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.ImageProductMedia;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.StockByShopCategory;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EOfferDiscountType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopCategory;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSelectedSkuChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ProductGalleryActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractProductActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.GalleryPagerAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.StockByShopCategoryAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.VerticalViewPager;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;

import static android.widget.LinearLayout.VERTICAL;

/**
 * Created by damien on 02/11/16.
 */
public class ProductRetailFragment extends ResourceBaseFragment<Product>  implements DisplayEventFragment{

    @Inject
    ConfigHelper configHelper;

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.stock_container)
    LinearLayout stockContainer;

    @BindView(R.id.vertical_view_pager)
    VerticalViewPager viewPager;

    @BindView(R.id.indicator)
    CirclePageIndicator mIndicator;

    @BindView(R.id.product_loaded)
    LinearLayout productLoadedContainer;

    @BindView(R.id.stock_loading)
    TextView stockLoading;

    @BindView(R.id.discount_indicator)
    TextView discountIndicator;

    @BindView(R.id.new_product_indicator)
    TextView newProductIndicator;

    // Image Gallery
    private GalleryPagerAdapter galleryPagerAdapter;

    // Product Stock
    private StockByShopCategoryAdapter stockByShopCategoryAdapter;

    List<EShopCategory> eShopCategories = null;

    private boolean openingGallery = false;
    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_retail, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        openingGallery = false;
        Product product = ((AbstractProductActivity) getActivity()).getResource();
        Sku selectedSku = ((AbstractProductActivity) getActivity()).getSelectedSku();
        if (product != null) {
            boolean skuAvailabilitesFilled = product.isSkuAvailabilitesFilled(selectedSku);
            if (skuAvailabilitesFilled) {
                displayStocks(product);
            }
            displaySelectedSku(selectedSku);
            displayDiscount(product);
        }

    }

    private void displayDiscount(Product product) {
        if (product == null) {
            discountIndicator.setVisibility(View.GONE);
            newProductIndicator.setVisibility(View.GONE);
            return;
        }

        if (product.isNewProduct())
            newProductIndicator.setVisibility(View.VISIBLE);
        else
            newProductIndicator.setVisibility(View.GONE);


        ProductOffer percentOffer = product.getOfferByDiscountType(EOfferDiscountType.PERCENT);
        if (percentOffer != null) {
            discountIndicator.setVisibility(View.VISIBLE);
            discountIndicator.setText(getContext().getString(R.string.product_discount_percent, StringUtils.getPrecentStr(percentOffer.getDiscount_value())));
        } else {
            //if do not have percent offer, show amount offer
            ProductOffer amountOffer = product.getOfferByDiscountType(EOfferDiscountType.AMOUNT);
            if (amountOffer != null) {
                discountIndicator.setVisibility(View.VISIBLE);
                Double discount = StringUtils.getDoubleFromStr(amountOffer.getDiscount_value());
                discountIndicator.setText(getContext().getString(R.string.product_discount_percent, configHelper.formatPrice(discount)));
            } else
                discountIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create adapter
        galleryPagerAdapter = new GalleryPagerAdapter(getContext());
        galleryPagerAdapter.setOpenGalleryListener(new GalleryPagerAdapter.OpenGalleryListener() {
            @Override
            public void open() {
                if (!openingGallery) {
                    onShowGallery();
                    openingGallery = true;
                }
            }
        });
        viewPager.setAdapter(galleryPagerAdapter);
        // Gallery Pager Indicator
        mIndicator.setViewPager(viewPager);
        mIndicator.setOrientation(VERTICAL);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stockByShopCategoryAdapter = new StockByShopCategoryAdapter(this.getContext());
        if (configHelper.getProductConfig() != null
                && configHelper.getProductConfig() != null) {
            eShopCategories = configHelper.getProductConfig().getEShopCategoryDisplay();
        }

        if (eShopCategories == null || !eShopCategories.isEmpty()) {
            stockContainer.setVisibility(View.VISIBLE);
            stockContainer.bringToFront();
        }

    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------


    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public Product getCachedResource() {
        return ((ResourceActivity<Product>) this.getActivity()).getResource();
    }

    @Override
    public void loadResource() {
        ((AbstractProductActivity) this.getActivity()).loadResource();
    }

    @Override
    protected void setResource(Product resourceProduct) {
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    //    @OnClick(R.id.product_gallery_shortcut)
    void onShowGallery() {
        Sku selectedSku = ((AbstractProductActivity) getActivity()).getSelectedSku();
        Product product = ((AbstractProductActivity) getActivity()).getProduct();
        int selectedPic = viewPager.getCurrentItem();
        Intent intent = new Intent(this.getActivity(), ProductGalleryActivity.class);
        intent.putExtra(IntentConstants.EXTRA_SKU, Parcels.wrap(selectedSku));
        intent.putExtra(IntentConstants.EXTRA_PRODUCT, Parcels.wrap(product));
        intent.putExtra(IntentConstants.EXTRA_SELECTED_PICTURE, selectedPic);
        this.getContext().startActivity(intent);
    }

    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------

    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Product> resourceResponseEvent) {
        if (EResourceType.PRODUCT == resourceResponseEvent.getEResourceType()) {
            displayDiscount(resourceResponseEvent.getResource());
            super.onResourceResponse(resourceResponseEvent);
        } else if (EResourceType.PRODUCT_STOCK == resourceResponseEvent.getEResourceType()) {
            if (resourceResponseEvent.getResourceException() == null) {
                displayStocks(resourceResponseEvent.getResource());
            } else {
                displayStocksLoading(EStockLoadingStatus.FAILED);
            }
        }
    }

    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent<Product> resourceRequestEvent) {
        if (EResourceType.PRODUCT == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    @Subscribe
    public void onProductSelectedSkuChangeEvent(ProductSelectedSkuChangeEvent productSelectedSkuChangeEvent) {
        Sku selectedSku = ((AbstractProductActivity) getActivity()).getSelectedSku();
        displaySelectedSku(selectedSku);
        displayStocksLoading(EStockLoadingStatus.LOADING);
    }

    private void displaySelectedSku(Sku selectedSku) {
        if (selectedSku != null) {
            // Create list from sku's media
            List<Media> skuMedias = Media.getMediasOfType(selectedSku.getMedias(), EMediaType.PICTURE);
            List<Media> productMedias = Media.getMediasOfType(((AbstractProductActivity) getActivity()).getResource().getMedias(), EMediaType.PICTURE);

            List<ImageProductMedia> displayedMedias = new ArrayList<>();
            if (skuMedias != null) {
                for (int i = 0; i < skuMedias.size(); i++) {
                    displayedMedias.add(new ImageProductMedia(selectedSku, skuMedias.get(i), i));
                }
            }
            if (productMedias != null) {
                for (int i = 0; i < productMedias.size(); i++) {
                    displayedMedias.add(new ImageProductMedia(selectedSku, productMedias.get(i), i));
                }
            }

            galleryPagerAdapter.setImgs(displayedMedias);
            galleryPagerAdapter.notifyDataSetChanged();
            // indicator visibility
            mIndicator.setVisibility(displayedMedias.size() > 1 ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void displayStocks(Product productWithStock) {
        Sku selectedSku = ((AbstractProductActivity) getActivity()).getSelectedSku();

        List<StockByShopCategory> stockByShopCategoryList = StockByShopCategory.getStockByShopCategory(productWithStock,
                selectedSku.getId(),
                configHelper.getCurrentShop(),
                eShopCategories);

        stockByShopCategoryAdapter.setItems(stockByShopCategoryList);
        stockLoading.setVisibility(View.GONE);
    }


    private enum EStockLoadingStatus {
        LOADING(R.string.stock_loading),
        LOADED(android.R.string.ok),
        FAILED(R.string.stock_failed);
        private int labelId;

        public int getLabelId() {
            return labelId;
        }

        EStockLoadingStatus(int labelId) {
            this.labelId = labelId;
        }
    }

    private void displayStocksLoading(EStockLoadingStatus status) {
        stockByShopCategoryAdapter.setItems(null);
        stockLoading.setVisibility(View.VISIBLE);
        stockLoading.setText(status.getLabelId());
    }

    @Override
    public void logEvent() {
        Sku sku = ((AbstractProductActivity) getActivity()).getSelectedSku();
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.PRODUCT)
                .setAttribute(EResourceAttribute.SKUS.getCode())
                .setSubAttribute("infos")
                .setValue(sku != null ? sku.getId() : null)
                .setStatus(EActionStatus.SUCCESS));
    }
}
