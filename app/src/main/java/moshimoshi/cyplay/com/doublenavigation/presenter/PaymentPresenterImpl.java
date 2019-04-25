package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentStatus;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.AddPaymentActionEvent;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.UpdatePaymentActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.PaymentInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_APayment;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by damien on 29/06/15.
 */
public class PaymentPresenterImpl extends BasePresenter implements PaymentPresenter {


    @Inject
    ActionEventHelper actionEventHelper;

    @Inject
    ConfigHelper configHelper;

    @Inject
    APIValue apiValue;

    @Inject
    PaymentContext paymentContext;

    private ResourceView<Payment> paymentView;

    private PaymentInteractor paymentInteractor;

    // Device selected

    public PaymentPresenterImpl(Context context, PaymentInteractor paymentInteractor) {
        super(context);
        this.paymentInteractor = paymentInteractor;
    }

    @Override
    public void setView(ResourceView<Payment> view) {
        this.paymentView = view;
    }

    // -------------------------------------------------------------------------------------------
    //                                      Payment
    // -------------------------------------------------------------------------------------------

    @Override
    public void getCustomerPayment(String customerId, String paymentId) {
        paymentInteractor.getCustomerPayment(customerId, paymentId, new AbstractResourceRequestCallback<PR_APayment>() {
            @Override
            public void onSuccess(final PR_APayment pr_aPayment) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        paymentView.onResourceViewResponse(new ResourceResponseEvent<>((Payment) pr_aPayment, null, EResourceType.PAYMENT));
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        paymentView.onResourceViewResponse(new ResourceResponseEvent<Payment>(null, new ResourceException(e), EResourceType.PAYMENT));
                    }
                });
            }
        });
    }

    @Override
    public void addCustomerPayment(final String customerId, Payment payment) {
        paymentInteractor.addCustomerPayment(customerId, payment, new AbstractResourceRequestCallback<PR_APayment>() {
            @Override
            public void onSuccess(final PR_APayment pr_aPayment) {
                actionEventHelper.log(new AddPaymentActionEvent().setStatus(EActionStatus.SUCCESS));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        Payment payment = (Payment) pr_aPayment;
                        paymentContext.setPayment(payment);
                        paymentView.onResourceViewResponse(new ResourceResponseEvent<>((Payment) pr_aPayment, null, EResourceType.PAYMENT));
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                actionEventHelper.log(new AddPaymentActionEvent().setStatus(EActionStatus.FAILURE));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        paymentView.onResourceViewResponse(new ResourceResponseEvent<Payment>(null, new ResourceException(e), EResourceType.PAYMENT));
                    }
                });
            }

        });
    }

    @Override
    public void updateCustomerPayment(String customerId, final String customerPaymentId, CustomerPaymentStatus customerPayment) {
        paymentInteractor.updateCustomerPayment(customerId, customerPaymentId, customerPayment, new AbstractResourceRequestCallback<PR_APayment>() {

            @Override
            public void onSuccess(final PR_APayment pr_aPayment) {
                actionEventHelper.log(new UpdatePaymentActionEventData(((Payment) pr_aPayment).getId()).setStatus(EActionStatus.SUCCESS));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        paymentView.onResourceViewResponse(new ResourceResponseEvent<>((Payment) pr_aPayment, null, EResourceType.PAYMENT));
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                actionEventHelper.log(new UpdatePaymentActionEventData(customerPaymentId).setStatus(EActionStatus.FAILURE));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        paymentView.onResourceViewResponse(new ResourceResponseEvent<Payment>(null, new ResourceException(e), EResourceType.PAYMENT));
                    }
                });
            }

        });
    }


}
