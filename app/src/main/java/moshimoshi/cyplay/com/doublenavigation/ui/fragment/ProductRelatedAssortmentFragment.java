package moshimoshi.cyplay.com.doublenavigation.ui.fragment;


import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EResourceAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ResourceActivity;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;

/**
 * Created by damien on 17/04/16.
 */
public class ProductRelatedAssortmentFragment extends ProductItemsRelatedFragment implements DisplayEventFragment {

    @Override
    protected void setResource(Object products) {
        if (product != null) {
            if (products != null) {
                List<Product> assortments = (List<Product>) products;
                if (assortments != null) {
                    int index = assortments.indexOf(product);
                    if (index >= 0) {
                        assortments.remove(index);
                    }
                }
                product.setAssortment_products(assortments);
            } else {
                product.setAssortment_products(new ArrayList<Product>());
            }
            productItemThumbnailAdapter.setItems((List<ProductItem>) (List<?>) product.getAssortment_products());
        }
    }

    @Override
    public Object getCachedResource() {
        Product product = ((ResourceActivity<Product>) this.getActivity()).getResource();
        return product != null ? product.getAssortment_products() : null;
    }

    @Override
    public void loadResource() {
        runProductRelatedRequest();
    }

    private void runProductRelatedRequest() {
        this.product = ((ResourceActivity<Product>) this.getActivity()).getResource();
        if (product != null && product.getAssortment_products() == null) {
            productsPresenter.getProductsForAssortment(product.getAssortment(), Product.PRODUCT_PREVIEW_PROJECTION, null);
        }
    }

    @Subscribe
    public void onResourceResponseEvent(ResourceResponseEvent<Object> resourceResponseEvent) {
        if (EResourceType.PRODUCT == resourceResponseEvent.getEResourceType()) {
            product = (Product) resourceResponseEvent.getResource();
            // Run request
            loadResource();
        }
    }

    @Subscribe
    public void onResourceResponseEvent(FilterResourceResponseEvent<Product, Object> resourceResponseEvent) {
        if (EResourceType.PRODUCTS == resourceResponseEvent.getEResourceType()) {
            super.onResourceResponse((ResourceResponseEvent) resourceResponseEvent);
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
                .setAttribute(EResourceAttribute.RELATED_ASSORTMENTS.getCode())
                .setValue(product != null ? product.getId() : null)
                .setStatus(EActionStatus.SUCCESS));
    }

}
