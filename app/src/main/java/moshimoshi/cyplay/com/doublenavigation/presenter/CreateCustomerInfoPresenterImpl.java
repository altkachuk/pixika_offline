package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerProfile;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.CreateActionEventData;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.CreateCustomerView;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomer;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by damien on 20/01/16.
 */
public class CreateCustomerInfoPresenterImpl extends BasePresenter implements CreateCustomerInfoPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    ActionEventHelper actionEventHelper;

    private Context context;
    private CreateCustomerView createCustomerView;
    private CustomerInteractor customerInteractor;

    public CreateCustomerInfoPresenterImpl(Context context, CustomerInteractor customerInteractor) {
        super(context);
        this.context = context;
        this.customerInteractor = customerInteractor;
    }

    @Override
    public void setView(CreateCustomerView view) {
        this.createCustomerView = view;
    }

    @Override
    public void createCustomerProfile(final CustomerProfile customer) {
        createCustomerView.showLoading();
        customerInteractor.createCustomerInfo(customer, new AbstractResourceRequestCallback<PR_ACustomer>() {
            @Override
            public void onSuccess(final PR_ACustomer pr_aCustomer) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        Customer createdCustomer = (Customer) pr_aCustomer;
                        createCustomerView.onCreateCustomerSuccess(createdCustomer);
                        actionEventHelper.log(new CreateActionEventData()
                                .addObjectParams(customer)
                                .setValue(createdCustomer.getId())
                                .setResource(EResource.CUSTOMER)
                                .setStatus(EActionStatus.SUCCESS)
                        );
                    }
                });


            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        actionEventHelper.log(new CreateActionEventData()
                                .addObjectParams(customer)
                                .setResource(EResource.CUSTOMER)
                                .setStatus(EActionStatus.FAILURE)
                        );
                        createCustomerView.onCreateCustomerError(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
                    }
                });


            }
        });
    }

}
