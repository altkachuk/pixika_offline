package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.DisplayActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.ScanActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomer;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by damien on 06/05/15.
 */
public class GetCustomerInfoPresenterImpl extends BasePresenter implements GetCustomerInfoPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    EventBus bus;

    @Inject
    CustomerContext customerContext;

    @Inject
    SellerContext sellerContext;

    @Inject
    ActionEventHelper actionEventHelper;

    private ResourceView<Customer> getCustomerInfoView;
    private CustomerInteractor customerInteractor;

    public GetCustomerInfoPresenterImpl(Context context, CustomerInteractor customerInteractor) {
        super(context);
        this.customerInteractor = customerInteractor;
    }

    @Override
    public void getCustomerInfo(final String id) {
        customerInteractor.getCustomerInfo(id, Customer.CUSTOMER_PROJECTION, new AbstractResourceRequestCallback<PR_ACustomer>() {

            @Override
            public void onSuccess(final PR_ACustomer pr_aCustomer) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        Customer customer = (Customer) pr_aCustomer;
                        customerContext.setCustomer(customer);

                        // Add Customer to History
                        sellerContext.addCustomerToHistory(customer);

                        // Success
                        getCustomerInfoView.onResourceViewResponse(new ResourceResponseEvent<>(customer,
                                null,
                                EResourceType.CUSTOMER));

                        actionEventHelper.log(new DisplayActionEventData()
                                .setResource(EResource.CUSTOMER)
                                .setValue(id)
                                .addObjectParams(customer)
                                .setStatus(EActionStatus.SUCCESS));
                    }
                });

            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        getCustomerInfoView.onResourceViewResponse(new ResourceResponseEvent<Customer>(null, new ResourceException(e), EResourceType.CUSTOMER));
                        actionEventHelper.log(new DisplayActionEventData()
                                .setResource(EResource.CUSTOMER)
                                .setValue(id)
                                .setStatus(EActionStatus.FAILURE));
                    }
                });

            }

        });
    }

    @Override
    public void getCustomerFromEan(final String ean) {
        customerInteractor.getCustomerInfoFromEAN(ean, new AbstractResourceRequestCallback<PR_ACustomer>() {

            @Override
            public void onSuccess(final PR_ACustomer customer) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        getCustomerInfoView.onResourceViewResponse(new ResourceResponseEvent<>((Customer) customer, null, EResourceType.CUSTOMER));
                        actionEventHelper.log(new ScanActionEventData(EResource.CUSTOMER)
                                .setStatus(EActionStatus.SUCCESS)
                                .setScannedCode(ean)
                        );
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        getCustomerInfoView.onResourceViewResponse(new ResourceResponseEvent<Customer>(null, new ResourceException(e), EResourceType.CUSTOMER));
                        actionEventHelper.log(new ScanActionEventData(EResource.CUSTOMER)
                                .setStatus(EActionStatus.FAILURE)
                                .setScannedCode(ean)
                        );
                    }
                });
            }
        });
    }

    @Override
    public void getCustomerOffer(String id) {
        List<String> projection = new ArrayList<>();
        projection.add("id");
        projection.add("offers");
        customerInteractor.getCustomerInfo(id, projection, new AbstractResourceRequestCallback<PR_ACustomer>() {
            @Override
            public void onSuccess(final PR_ACustomer pr_aCustomer) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        getCustomerInfoView.onResourceViewResponse(new ResourceResponseEvent<>((Customer) pr_aCustomer,
                                null,
                                EResourceType.CUSTOMER));
                    }
                });

            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        Log.e(LogUtils.generateTag(this), "Error on GetCustomerInfoPresenterImpl");
                        getCustomerInfoView.onResourceViewResponse(new ResourceResponseEvent<Customer>(null, new ResourceException(e), EResourceType.CUSTOMER));
                    }
                });

            }

        });
    }


    @Override
    public void setView(ResourceView<Customer> view) {
        getCustomerInfoView = view;
    }

}
