package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import moshimoshi.cyplay.com.doublenavigation.model.business.AttributeReview;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductReview;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductReviewAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractPaginatedResourceRequestCallbackImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.ResourceRequestCallbackImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.PaginatedResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductReviewAttributeInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductReviewInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductReview;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductReviewAttribute;
import ninja.cyplay.com.apilibrary.utils.CompatUtils;


/**
 * Created by romainlebouc on 03/01/2017.
 */

public class ProductReviewPresenterImpl extends BasePresenter implements ProductReviewPresenter {

    private final Context context;
    private ResourceView<ProductReview> productReviewView;
    private ResourceView<ProductReview> createResourceView;

    private ProductReviewInteractor productReviewInteractor;
    private ProductReviewAttributeInteractor productReviewAttributeInteractor;

    public ProductReviewPresenterImpl(Context context,
                                      ProductReviewInteractor productReviewInteractor,
                                      ProductReviewAttributeInteractor productReviewAttributeInteractor) {
        super(context);
        this.context = context;
        this.productReviewInteractor = productReviewInteractor;
        this.productReviewAttributeInteractor = productReviewAttributeInteractor;
    }


    public void setView(ResourceView<ProductReview> view) {
        this.productReviewView = view;
    }

    public void setCreateView(ResourceView<ProductReview> view) {
        this.createResourceView = view;
    }


    public void addReview(ProductReview productReview) {
        productReviewInteractor.addResource(productReview,
                new ResourceRequestCallbackImpl<>(
                        context,
                        createResourceView,
                        EResourceType.PRODUCT_REVIEW));
    }

    public void updateReview(ProductReview productReview) {
        productReviewInteractor.updateResource(productReview,
                new ResourceRequestCallbackImpl<>(
                        context,
                        createResourceView,
                        EResourceType.PRODUCT_REVIEW));
    }


    public void getProductReview(final String customerId,
                                 final String productId,
                                 final String skuId) {

        // First, we get the product reviews attributes
        productReviewAttributeInteractor.getResources(null, null, new PaginatedResourceRequestCallback<PR_AProductReviewAttribute>() {

            @Override
            public void onError(BaseException e) {
                productReviewView.onResourceViewResponse(new ResourceResponseEvent<ProductReview>(null, new ResourceException(e), EResourceType.PRODUCT_REVIEW));
            }

            @Override
            public void onSuccess(final List<PR_AProductReviewAttribute> resource, Integer count, String previous, String next) {
                final List<ProductReviewAttribute> productReviewAttributes = (List<ProductReviewAttribute>) (List<?>) resource;
                productReviewInteractor.getResources(ProductReviewInteractor.getParametersMap(customerId, productId, skuId),
                        ProductReview.PRODUCT_REVIEW_PROJECTION,
                        new AbstractPaginatedResourceRequestCallbackImpl<PR_AProductReview>() {

                            @Override
                            public void onError(BaseException e) {
                                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                                    @Override
                                    public void execute() {

                                    }
                                });
                            }

                            @Override
                            public void onSuccess(final List<PR_AProductReview> resource, Integer count, String previous, String next) {
                                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                                    @Override
                                    public void execute() {
                                        ProductReview productReview;

                                        if (resource != null && !resource.isEmpty()) {
                                            productReview = (ProductReview) resource.get(0);
                                        } else {
                                            productReview = new ProductReview();
                                            productReview.setCustomer_id(customerId);
                                            productReview.setProduct_id(productId);
                                            productReview.setLast_update(new Date());
                                            productReview.setSku_id(skuId);
                                            productReview.setLang(CompatUtils.toLanguageTag(Locale.getDefault()));
                                            productReview.setAttribute_reviews(new ArrayList<AttributeReview>());
                                        }
                                        for (ProductReviewAttribute productReviewAttribute : productReviewAttributes) {
                                            AttributeReview attributeReview = productReview.getAttributeReviewForAttributeId(productReviewAttribute.getId());
                                            if (attributeReview == null) {
                                                attributeReview = new AttributeReview(productReviewAttribute);
                                                productReview.getAttribute_reviews().add(attributeReview);
                                            }
                                            attributeReview.setProductReviewAttribute(productReviewAttribute);
                                        }


                                        ResourceResponseEvent<ProductReview> resourceResourceResponseEvent = new ResourceResponseEvent<>(productReview,
                                                null,
                                                EResourceType.PRODUCT_REVIEW
                                        );
                                        productReviewView.onResourceViewResponse(resourceResourceResponseEvent);
                                    }
                                });


                            }
                        });
            }
        });


    }

}
