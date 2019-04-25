package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ProductSelectedSkuChangeEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractProductActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;

/**
 * Created by damien on 25/08/16.
 */
public class ProductSkuSkusRelatedFragment extends ProductItemsRelatedFragment implements DisplayEventFragment {

    private Sku sku;

    @Override
    protected void setResource(Object products) {
        sku = ((AbstractProductActivity) this.getActivity()).getSelectedSku();
        if (sku != null) {
            if (products != null) {
                sku.setRelated_product_skus((List<Product>) products);

            } else {
                sku.setRelated_product_skus(new ArrayList<Product>());
            }
            productItemThumbnailAdapter.setItems((List<ProductItem>) (List<?>) sku.getRelated_product_skus());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Object getCachedResource() {
        sku = ((AbstractProductActivity) this.getActivity()).getSelectedSku();
        return sku != null && sku.getRelated_product_skus() != null ? sku.getRelated_product_skus() : null;
    }

    @Override
    public void loadResource() {
        runSkuRelatedRequest();
    }

    private void runSkuRelatedRequest() {
        sku = ((AbstractProductActivity) this.getActivity()).getSelectedSku();
        if (sku != null && sku.getRelated_sku_ids() != null) {
            productsPresenter.getProductsFromSkuIds(sku.getRelated_sku_ids(), Product.PRODUCT_PREVIEW_PROJECTION);
        } else {
            this.onResourceResponseEvent(new ResourceResponseEvent<Object>(new ArrayList<Product>(), null, EResourceType.PRODUCTS));
        }
    }

    @Subscribe
    public void onSkuChange(ProductSelectedSkuChangeEvent productSelectedSkuChangeEvent) {
    }

    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Object> resourceResponseEvent) {
        if (EResourceType.PRODUCT == resourceResponseEvent.getEResourceType()) {
            product = (Product) resourceResponseEvent.getResource();
            loadResource();
        }
        if (EResourceType.PRODUCTS == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse(resourceResponseEvent);
        }
    }

    @Override
    @Subscribe
    public void onResourceRequestEvent(ResourceRequestEvent<Object> resourceRequestEvent) {
        if (EResourceType.PRODUCTS == resourceRequestEvent.getEResourceType()) {
            super.onResourceRequest(resourceRequestEvent);
        }
    }

    @Override
    public void logEvent() {
        actionEventHelper.log(new DisplayActionEventData()
                .setResource(EResource.PRODUCT)
                .setAttribute(EResourceAttribute.SKUS.getCode())
                .setSubAttribute("related_skus")
                .setValue(product != null ? product.getId() : null)
                .setStatus(EActionStatus.SUCCESS));
    }
}
