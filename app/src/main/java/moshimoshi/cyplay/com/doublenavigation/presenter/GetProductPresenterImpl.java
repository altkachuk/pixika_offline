package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.ScanActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProduct;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by anishosni on 05/05/15.
 */
public class GetProductPresenterImpl extends BasePresenter implements GetProductPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    EventBus bus;

    @Inject
    ActionEventHelper actionEventHelper;

    private Context context;
    private ProductInteractor productInteractor;
    private FilterResourceView<Product, ProductFilter> getProductView;

    private final static String RESOURCE_NOT_FOUND_CODE = "404";

    public GetProductPresenterImpl(Context context, ProductInteractor productInteractor) {
        super(context);
        this.context = context;
        this.productInteractor = productInteractor;
    }

    @Override
    public void setView(FilterResourceView<Product, ProductFilter> view) {
        getProductView = view;
    }

    @Override
    public void getProductInfoFromBarCode(final String barCode) {
        productInteractor.getProductFromBarCode(barCode, new AbstractResourceRequestCallback<PR_AProduct>() {
            @Override
            public void onSuccess(final PR_AProduct product) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        // Success
                        getProductView.onResourceViewResponse(new FilterResourceResponseEvent<Product, ProductFilter>((Product) product, null, EResourceType.PRODUCT));
                        actionEventHelper.log(new ScanActionEventData(EResource.PRODUCT)
                                .setValue(barCode)
                                .setScannedCode(barCode)
                                .setStatus(EActionStatus.SUCCESS));
                    }
                });

            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        getProductView.onResourceViewResponse(new FilterResourceResponseEvent<Product, ProductFilter>(null, new ResourceException(e), EResourceType.PRODUCT));
                        actionEventHelper.log(new ScanActionEventData(EResource.PRODUCT)
                                .setValue(barCode)
                                .setScannedCode(barCode)
                                .setStatus(EActionStatus.FAILURE));
                    }
                });


            }

        });
    }

    @Override
    public void getProduct(final String productId, List<String> project) {
        productInteractor.getProduct(productId, project, new AbstractResourceRequestCallback<PR_AProduct>() {

            @Override
            public void onSuccess(final PR_AProduct product) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        // Success
                        getProductView.onResourceViewResponse(new FilterResourceResponseEvent<Product, ProductFilter>((Product) product, null, EResourceType.PRODUCT));
                        actionEventHelper.log(new DisplayActionEventData()
                                .setResource(EResource.PRODUCT)
                                .setValue(productId)
                                .addObjectParams(product)
                                .setStatus(EActionStatus.SUCCESS));
                    }
                });


            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        if (RESOURCE_NOT_FOUND_CODE.equals(e.getResponse().getStatus())) {
                            getProductView.onResourceViewResponse(new FilterResourceResponseEvent<Product, ProductFilter>(null, null, EResourceType.PRODUCT));
                        } else {
                            Log.e(LogUtils.generateTag(this), "Error on GetProductPresenterImpl");
                            getProductView.onResourceViewResponse(new FilterResourceResponseEvent<Product, ProductFilter>(null, new ResourceException(e), EResourceType.PRODUCT));
                            actionEventHelper.log(new DisplayActionEventData()
                                    .setResource(EResource.PRODUCT)
                                    .setValue(productId)
                                    .setStatus(EActionStatus.FAILURE));
                        }
                    }
                });


            }
        });
    }

    public void getProductStock(String productId, String skuId) {

        productInteractor.getProductSku(productId, skuId, Product.PRODUCT_STOCK_PROJECTION, new AbstractResourceRequestCallback<PR_AProduct>() {

            @Override
            public void onSuccess(final PR_AProduct product) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        getProductView.onResourceViewResponse(new FilterResourceResponseEvent<Product, ProductFilter>((Product) product, null, EResourceType.PRODUCT_STOCK));
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        if (e.getResponse() != null && RESOURCE_NOT_FOUND_CODE.equals(e.getResponse().getStatus())) {
                            getProductView.onResourceViewResponse(new FilterResourceResponseEvent<Product, ProductFilter>(null, null, EResourceType.PRODUCT_STOCK));
                        } else {
                            getProductView.onResourceViewResponse(new FilterResourceResponseEvent<Product, ProductFilter>(null, new ResourceException(e), EResourceType.PRODUCT_STOCK));
                        }
                    }
                });


            }
        });

    }

}
