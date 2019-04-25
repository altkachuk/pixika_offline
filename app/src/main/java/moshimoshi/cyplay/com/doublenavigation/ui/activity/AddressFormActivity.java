package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Address;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerProfile;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EAddressType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerAddressesPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.UpdateCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractFormActivity;
import moshimoshi.cyplay.com.doublenavigation.utils.IntentUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import moshimoshi.cyplay.com.doublenavigation.view.UpdateCustomerInfoView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by romainlebouc on 08/12/2016.
 */

public class AddressFormActivity extends AbstractFormActivity<Address> implements ResourceView<Address>, UpdateCustomerInfoView {

    @Inject
    CustomerAddressesPresenter customerAddressesPresenter;

    @Inject
    UpdateCustomerInfoPresenter updateCustomerInfoPresenter;


    private EAddressType eAddressType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerAddressesPresenter.setView(this);
        updateCustomerInfoPresenter.setView(this);

        if (getIntent().hasExtra(IntentConstants.EXTRA_SHIPPING_ADDRESS)) {
            formObject = IntentUtils.getExtraFromIntent(getIntent(), IntentConstants.EXTRA_SHIPPING_ADDRESS);
        } else {
            formObject = new Address();
        }

        if (getIntent().hasExtra(IntentConstants.EXTRA_FORM_ADDRESS_TYPE)) {
            eAddressType = EAddressType.values()[getIntent().getIntExtra(IntentConstants.EXTRA_FORM_ADDRESS_TYPE, 0)];
        }

        init();
    }

    @Override
    public void onResourceViewResponse(ResourceResponseEvent<Address> resourceResponseEvent) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (resourceResponseEvent.getEResourceType() == EResourceType.ADDRESSES) {

            if (resourceResponseEvent.getResourceException() == null) {
                formObject.setId(resourceResponseEvent.getResource().getId());
                if (getIntent().hasExtra(IntentConstants.EXTRA_SHIPPING_ADDRESS)) {
                    customerContext.getCustomer().updateAddress(formObject);
                } else {
                    customerContext.getCustomer().getAddresses().add(formObject);
                }

                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);

                finishForm(false);
            } else {
                String message;
                if (getIntent().hasExtra(IntentConstants.EXTRA_SHIPPING_ADDRESS)) {
                    message = this.getString(R.string.form_shipping_address_update_error, resourceResponseEvent.getResourceException().getMessage());
                } else {
                    message = this.getString(R.string.form_shipping_address_create_error, resourceResponseEvent.getResourceException().getMessage());
                }

                SnackBarHelper.buildSnackbar(coordinatorLayout,
                        message,
                        Snackbar.LENGTH_INDEFINITE,
                        this.getString(android.R.string.ok)).show();

            }
        }
    }

    @Override
    protected void saveObject() {
        if (eAddressType != null) {
            switch (eAddressType) {
                case EXTRA:
                    if (getIntent().hasExtra(IntentConstants.EXTRA_SHIPPING_ADDRESS)) {
                        customerAddressesPresenter.updateAddress(customerContext.getCustomerId(), formObject);
                    } else {
                        customerAddressesPresenter.createAddress(customerContext.getCustomerId(), formObject);
                    }
                    break;
                case DEFAULT:
                    CustomerProfile customerProfile = new CustomerProfile(customerContext.getCustomer(), formObject);
                    updateCustomerInfoPresenter.updateCustomerProfile(customerProfile);

            }
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onUpdatedCustomerSuccess(String customerId) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finishForm(false);
    }

    @Override
    public void onUpdatedCustomerFailure(ExceptionType exceptionType, String status, String message) {

        SnackBarHelper.buildSnackbar(coordinatorLayout,
                this.getString(R.string.form_update_error, message),
                Snackbar.LENGTH_INDEFINITE,
                this.getString(android.R.string.ok)).show();
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }
}
