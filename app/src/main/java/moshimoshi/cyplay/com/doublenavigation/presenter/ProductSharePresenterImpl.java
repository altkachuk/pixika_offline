package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductShare;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EChannel;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.ShareActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.CreateResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductShareInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;

/**
 * Created by romainlebouc on 24/08/16.
 */
public class ProductSharePresenterImpl extends BasePresenter implements ProductSharePresenter {

    @Inject
    ActionEventHelper actionEventHelper;

    private ProductShareInteractor productShareInteractor;
    private CreateResourceView<ProductShare> productShareView;

    public ProductSharePresenterImpl(Context context, ProductShareInteractor productShareInteractor) {
        super(context);
        this.productShareInteractor = productShareInteractor;
    }

    @Override
    public void shareProducts(final ProductShare productShare) {
        productShareInteractor.shareProducts(productShare, new AbstractResourceRequestCallback<ProductShare>() {

            @Override
            public void onSuccess(final ProductShare productShare) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        ResourceResponseEvent<ProductShare> createResourceResourceResponseEvent = new ResourceResponseEvent<>(productShare,
                                null,
                                EResourceType.PRODUCT_SHARE
                        );
                        productShareView.onResourceViewCreateResponse(createResourceResourceResponseEvent);

                        if (productShare != null) {
                            actionEventHelper.log(new ShareActionEventData()
                                    .setResource(EResource.CUSTOMER)
                                    .setAttribute("wishlist")
                                    .setStatus(EActionStatus.SUCCESS)
                                    .setChannel(EChannel.EMAIL)
                                    .setShareIds("product_id", productShare.getProduct_ids()));
                        }
                    }
                });


            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        productShareView.onError(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
                        if (productShare != null) {
                            actionEventHelper.log(new ShareActionEventData()
                                    .setResource(EResource.CUSTOMER)
                                    .setAttribute("wishlist")
                                    .setStatus(EActionStatus.FAILURE)
                                    .setChannel(EChannel.EMAIL)
                                    .setShareIds("product_id", productShare.getProduct_ids()));
                        }
                    }
                });


            }
        });


    }

    @Override
    public void setView(CreateResourceView<ProductShare> view) {
        this.productShareView = view;
    }

}
