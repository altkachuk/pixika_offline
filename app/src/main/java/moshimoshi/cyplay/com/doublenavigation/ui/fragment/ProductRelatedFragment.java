package moshimoshi.cyplay.com.doublenavigation.ui.fragment;


import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;

/**
 * Created by damien on 17/04/16.
 */
public class ProductRelatedFragment extends ProductItemsRelatedFragment implements DisplayEventFragment {

    @Override
    protected void setResource(Object products) {
        if (product != null) {
            if (products != null) {
                product.setRelated_products((List<Product>) products);
            } else {
                product.setRelated_products(new ArrayList<Product>());
            }
            productItemThumbnailAdapter.setItems((List<ProductItem>) (List<?>) product.getRelated_products());
        }
    }

    @Override
    public Object getCachedResource() {
        Product product = ((ResourceActivity<Product>) this.getActivity()).getResource();
        return product != null ? product.getRelated_products() : null;
    }

    @Override
    public void loadResource() {
        runProductRelatedRequest();
    }

    private void runProductRelatedRequest() {
        this.product = ((ResourceActivity<Product>) this.getActivity()).getResource();
        if (product != null && product.getRelated_products() == null)
            productsPresenter.getProductsFromIds(product.getRelated_prod_ids(), Product.PRODUCT_PREVIEW_PROJECTION);
    }

    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Object> resourceResponseEvent) {
        if (EResourceType.PRODUCT == resourceResponseEvent.getEResourceType()) {
            product = (Product) resourceResponseEvent.getResource();
            // Run request
            loadResource();
        } else if (EResourceType.PRODUCTS == resourceResponseEvent.getEResourceType()) {
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
                .setAttribute(EResourceAttribute.RELATED_PRODUCTS.getCode())
                .setValue(product != null ? product.getId() : null)
                .setStatus(EActionStatus.SUCCESS));
    }
}
