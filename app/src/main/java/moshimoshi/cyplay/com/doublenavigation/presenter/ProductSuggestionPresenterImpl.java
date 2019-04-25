package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSuggestion;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.SuggestionActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductSuggestion;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by damien on 05/05/15.
 */
public class ProductSuggestionPresenterImpl extends BasePresenter implements ProductSuggestionPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    ActionEventHelper actionEventHelper;

    private ProductInteractor productInteractor;
    private Context context;
    private ResourceView<ProductSuggestion> productSuggestionResourceView;

    public ProductSuggestionPresenterImpl(Context context, ProductInteractor productInteractor) {
        super(context);
        this.context = context;
        this.productInteractor = productInteractor;
    }

    @Override
    public void suggestProducts(final String productName) {

        actionEventHelper.log(new SuggestionActionEventData().setResource(EResource.PRODUCT).addSuggestionQueryParams("q", productName));

        this.productInteractor.suggestProducts(productName, this.context.getResources().getInteger(R.integer.product_suggestions_columns_count), new AbstractResourceRequestCallback<PR_AProductSuggestion>() {

            @Override
            public void onSuccess(final PR_AProductSuggestion pr_aProductSuggestion) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        productSuggestionResourceView.onResourceViewResponse(new ResourceResponseEvent<>(
                                (ProductSuggestion) pr_aProductSuggestion,
                                null,
                                EResourceType.PRODUCT_SUGGESTION));
                    }
                });


            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {

                        Log.e(LogUtils.generateTag(this), "Error on ProductSuggestionPresenterImpl");
                        productSuggestionResourceView.onResourceViewResponse(new ResourceResponseEvent<ProductSuggestion>(null,
                                new ResourceException(e), EResourceType.PRODUCT_SUGGESTION));
                    }
                });

            }


        });
    }

    @Override
    public void setView(ResourceView<ProductSuggestion> view) {
        this.productSuggestionResourceView = view;
    }
}
