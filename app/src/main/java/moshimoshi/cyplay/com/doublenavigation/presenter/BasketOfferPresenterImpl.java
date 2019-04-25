package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerOfferInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasketOffer;

/**
 * Created by romainlebouc on 06/04/2017.
 */

public class BasketOfferPresenterImpl extends BasePresenter implements BasketOfferPresenter {

    @Inject
    CustomerContext customerContext;

    private CustomerOfferInteractor customerOfferInteractor;
    private ResourceView<BasketOffer> view;

    public BasketOfferPresenterImpl(Context context,
                                    CustomerOfferInteractor customerOfferInteractor) {
        super(context);
        this.customerOfferInteractor = customerOfferInteractor;
    }

    @Override
    public void setView(ResourceView<BasketOffer> view) {
        this.view = view;
    }


    @Override
    public void activeCustomerOffer(String offer_id) {
        if (customerContext != null && customerContext.isCustomerIdentified() && customerContext.getCustomer() != null) {
            BasketOffer basketOffer = new BasketOffer();
            basketOffer.setOffer_id(offer_id);
            customerOfferInteractor.addSubResource(customerContext.getCustomer().getId(), basketOffer, new AbstractResourceRequestCallback<PR_ABasketOffer>() {
                @Override
                public void onSuccess(PR_ABasketOffer pr_aBasketOffer) {
                    view.onResourceViewResponse(new ResourceResponseEvent<BasketOffer>(null, null, EResourceType.BASKET_OFFER));
                }

                @Override
                public void onError(BaseException e) {
                    ResourceResponseEvent<BasketOffer> resourceResourceResponseEvent = new ResourceResponseEvent<>(null, new ResourceException(e), EResourceType.BASKET_OFFER);
                    view.onResourceViewResponse(resourceResourceResponseEvent);
                }
            });
        }
    }

    @Override
    public void inActiveCustomerOffer(String offer_id) {
        if (customerContext != null && customerContext.isCustomerIdentified() && customerContext.getCustomer() != null) {
            BasketOffer basketOffer = new BasketOffer();
            basketOffer.setId(offer_id);
            customerOfferInteractor.deleteSubResource(customerContext.getCustomer().getId(), basketOffer, new AbstractResourceRequestCallback<PR_ABasketOffer>() {
                @Override
                public void onSuccess(final PR_ABasketOffer pr_aBasketOffer) {
                    super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                        @Override
                        public void execute() {
                            view.onResourceViewResponse(new ResourceResponseEvent(pr_aBasketOffer, null, EResourceType.BASKET_OFFER));
                        }
                    });
                }

                @Override
                public void onError(BaseException e) {

                }
            });
        }
    }
}
