package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransaction;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransactionStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.view.PaymentTransactionView;
import ninja.cyplay.com.apilibrary.domain.interactor.PaymentInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.requests.ResourceRequest;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_APaymentTransaction;

/**
 * Created by romainlebouc on 06/12/2016.
 */

public class PaymentTransactionPresenterImpl extends BasePresenter implements PaymentTransactionPresenter {

    @Inject
    PaymentContext paymentContext;

    private PaymentInteractor paymentInteractor;
    private PaymentTransactionView paymentTransactionView;


    public PaymentTransactionPresenterImpl(Context context, PaymentInteractor paymentInteractor) {
        super(context);
        this.paymentInteractor = paymentInteractor;
    }

    @Override
    public void setView(PaymentTransactionView view) {
        this.paymentTransactionView = view;
    }


    @Override
    public void addTransactionToCustomerPayment(final ResourceRequest<CustomerPaymentTransaction> resourceRequest) {
        paymentContext.setCurrentPaymentTransaction(resourceRequest.getBody());
        paymentInteractor.addTransactionToCustomerPayment(resourceRequest.getId(), resourceRequest.getSecondLevelId(), resourceRequest.getBody(), new AbstractResourceRequestCallback<PR_APaymentTransaction>() {

            @Override
            public void onSuccess(final PR_APaymentTransaction pr_aPaymentTransaction) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        paymentContext.setCurrentTransactionId(((CustomerPaymentTransaction) pr_aPaymentTransaction).getId());
                        paymentTransactionView.onResourceViewResponse(new ResourceResponseEvent<>((CustomerPaymentTransaction) pr_aPaymentTransaction, null, EResourceType.PAYMENT_TRANSACTION, resourceRequest));
                    }
                });

            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        paymentTransactionView.onResourceViewResponse(new ResourceResponseEvent<CustomerPaymentTransaction>(null, new ResourceException(e), EResourceType.PAYMENT_TRANSACTION, resourceRequest));
                    }
                });
            }

        });
    }

    @Override
    public void updateCustomerTransactionPayment(final ResourceRequest<CustomerPaymentTransactionStatus> resourceRequest) {
        paymentInteractor.updateCustomerTransactionPayment(resourceRequest.getId(), resourceRequest.getSecondLevelId(), resourceRequest.getThirdLevelId(), resourceRequest.getBody(), new AbstractResourceRequestCallback<PR_APaymentTransaction>() {

            @Override
            public void onSuccess(final PR_APaymentTransaction pr_aPaymentTransaction) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        paymentTransactionView.onResourceViewResponse(new ResourceResponseEvent<>((CustomerPaymentTransaction) pr_aPaymentTransaction, null, EResourceType.PAYMENT_TRANSACTION, resourceRequest));
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        paymentTransactionView.onResourceViewResponse(new ResourceResponseEvent<CustomerPaymentTransaction>(null, new ResourceException(e), EResourceType.PAYMENT_TRANSACTION, resourceRequest));
                    }
                });
            }

        });
    }


}
