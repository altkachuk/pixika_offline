package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.ImageProductMedia;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.PricePerUnit;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ska;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSelectedSkuChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractProductActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceBaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.helper.ImageGalleryHelper;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ProductSkuSelectorAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.CrossFader;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;

/**
 * Created by romainlebouc on 05/08/16.
 */
public class ProductItemFragment extends ResourceBaseFragment<Product> implements DisplayEventFragment {


    @Inject
    ActionEventHelper actionEventHelper;

    @BindView(R.id.shop_price_container)
    View shopPriceContainer;

    @BindView(R.id.product_sku_shop_price)
    TextView shopPrice;

    @BindView(R.id.product_sku_shop_price_per_unit)
    TextView shopPricePerUnitTextView;

    @BindView(R.id.web_price_container)
    View webPriceContainer;

    @BindView(R.id.product_sku_web_price)
    TextView webPrice;

    @BindView(R.id.product_sku_web_price_per_unit)
    TextView webPricePerUnitTextView;

    @BindView(R.id.product_sku_spinner)
    Spinner skuSpinner;

    @BindView(R.id.product_images_gallery)
    ViewPager productImagesPager;

    // ProductImagesAdpater => Could come back if Jupiter, Venus and Uranus are aligned again
    @Deprecated
    @BindView(R.id.product_images_gallery_thumbnails_scrollview)
    ScrollView thumbnailsScrollView;

    @Deprecated
    @BindView(R.id.product_images_gallery_thumbnails)
    LinearLayout thumbnails;

    private Product product;
    private Sku sku;
    private ImageGalleryHelper imageGalleryHelper;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create image gallery adapter
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageGalleryHelper = new ImageGalleryHelper(getContext(), bus, productImagesPager, thumbnails);
        productImagesPager.setAdapter(imageGalleryHelper.getGalleryAdapter());
        productImagesPager.setOffscreenPageLimit(3); // how many images to load into memory
        // page Change listener
        productImagesPager.addOnPageChangeListener(new ImageViewPagerOnPageChangeListener());
        // create imageGallery
        if (((AbstractProductActivity) getActivity()).getProduct() != null) {
            imageGalleryHelper.createImageGallery(((AbstractProductActivity) getActivity()).getProduct(), sku, thumbnails);
        }
        thumbnailsScrollView.setVisibility(ImageGalleryHelper.THUMBNAIL_VIEW_ENABLE ? View.VISIBLE : View.GONE);


    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void setSku(Sku sku) {
        this.sku = sku;
        if (sku != null) {
            imageGalleryHelper.setSku(sku);
            imageGalleryHelper.createImageGallery(product, sku, thumbnails);
            updateSkuPrices();
        }
    }

    private void initSkuSpinner() {
        // Create adapter
        ProductSkuSelectorAdapter spinnerArrayAdapter = new ProductSkuSelectorAdapter(getContext(), product);
        // Set adapter
        skuSpinner.setAdapter(spinnerArrayAdapter);
        Sku selectedSku = ((AbstractProductActivity) this.getActivity()).getSelectedSku();
        if (product != null && selectedSku != null) {
            int index = product.getSkus().indexOf(selectedSku);
            if (index > 0) {
                skuSpinner.setSelection(index, false);
                bus.post(new ProductSelectedSkuChangeEvent(selectedSku));
            }
        } else if (product != null && selectedSku == null) {
            int position = ((AbstractProductActivity) this.getActivity()).getInitialSelectedSkuPosition();
            skuSpinner.setSelection(position, false);
            bus.post(new ProductSelectedSkuChangeEvent(product.getSkus().get(position)));
        }

        skuSpinner.setOnItemSelectedListener(new SkuSelectedListener());
        skuSpinner.setEnabled(!(product.getSkus() != null && product.getSkus().size() <= 1));

    }

    private void updateSkuPrices() {
        if (sku != null) {
            this.updateSkuPrice(sku.getCurrent_shop_availability(), EShopType.REGULAR);
            this.updateSkuPrice(sku.getWeb_shop_availability(), EShopType.WEB);
        }
    }

    private void updateSkuPrice(Ska ska, EShopType eShopType) {
        switch (eShopType) {
            case REGULAR:
                this.updateSkuPrice(ska, shopPrice, shopPricePerUnitTextView);
                break;
            case WEB:
                this.updateSkuPrice(ska, webPrice, webPricePerUnitTextView);
                break;
            default:

        }

    }

    private void updateSkuPrice(Ska ska, TextView price, TextView pricePerUnitTextView) {
        price.setText(configHelper.formatPrice(ska != null ? ska.getPrice() : null));
        price.setTextColor(configHelper.getBrandColor(this.getContext(), product));
        PricePerUnit pricePerUnit = ska != null ? ska.getPrice_per_unit() : null;
        if (pricePerUnit != null && pricePerUnit.getPrice() != null && sku != null && sku.getUnit() != null) {
            String builder = configHelper.formatPrice(pricePerUnit.getPrice()) +
                    " / " +
                    sku.getUnit();
            pricePerUnitTextView.setText(builder);
        } else {
            pricePerUnitTextView.setText(StringUtils.EMPTY_STRING);
        }
    }


    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
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
    protected void setResource(Product product) {
        if (product != null) {
            this.product = product;
            imageGalleryHelper.setProduct(product);
            initSkuSpinner();
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    private class SkuSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position < product.getSkus().size()) {
                Sku selectedSku = product.getSkus().get(position);
                bus.post(new ProductSelectedSkuChangeEvent(selectedSku));
                // Select correct image if possible
                if (imageGalleryHelper.getImageProductMedias() != null && selectedSku != null) {
                    int imagePositionToSelect = 0;
                    // find 1st image position of the sku selected
                    if (product != null && product.getMedias() != null)
                        imagePositionToSelect += Media.getMediasOfType(product.getMedias(), EMediaType.PICTURE).size();
                    // Go through list of medias to calculate position
                    for (int i = 0; i < position; i++) {
                        if (product != null && product.getSkus() != null && product.getSkus().size() > i) {
                            if (product.getSkus().get(i).getMedias() != null)
                                imagePositionToSelect += Media.getMediasOfType(product.getSkus().get(i).getMedias(), EMediaType.PICTURE).size();
                        }
                    }
                    // update pager
                    productImagesPager.setCurrentItem(imagePositionToSelect);
                    // update thumbnail
                    imageGalleryHelper.selectThumbnailAtPosition(imagePositionToSelect);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class ImageViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (imageGalleryHelper.getImageProductMedias() != null && imageGalleryHelper.getImageProductMedias().size() > position) {
                // update thumbnail
                imageGalleryHelper.selectThumbnailAtPosition(position);
                // update sku selected if necessary
                ImageProductMedia imageProductMedia = imageGalleryHelper.getImageProductMedias().get(position);
                // update spinner
                if (imageProductMedia != null && imageProductMedia.getSku() != null) {
                    skuSpinner.setSelection(imageProductMedia.getSkuPosition(), false);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private boolean shopPriceVisible = true;

    @OnClick(R.id.product_sku_price_type_switch)
    public void onSwitchPriceClick() {
        displayPrice();

        shopPriceVisible = !shopPriceVisible;
    }

    private void displayPrice() {
        if (shopPriceVisible) {
            new CrossFader(shopPriceContainer, webPriceContainer, 150).start();
        } else {
            new CrossFader(webPriceContainer, shopPriceContainer, 150).start();
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                       Event(s)
    // -------------------------------------------------------------------------------------------

    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Product> resourceResponseEvent) {
        if (EResourceType.PRODUCT == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
            imageGalleryHelper.createImageGallery(resourceResponseEvent.getResource(), sku, thumbnails);
        }
    }

    @Override
    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent<Product> resourceRequestEvent) {
        if (EResourceType.PRODUCT == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    @Subscribe
    public void onProductSelectedSkuChangeEvent(ProductSelectedSkuChangeEvent productSelectedSkuChangeEvent) {
        setSku(productSelectedSkuChangeEvent.getSku());
    }

    @Override
    public void logEvent() {
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.PRODUCT)
                .setAttribute(EResourceAttribute.SKUS.getCode())
                .setSubAttribute("details")
                .setValue(product.getId())
                .setStatus(EActionStatus.SUCCESS));
    }

}
