package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerProfile;
import moshimoshi.cyplay.com.doublenavigation.presenter.CreateCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.UpdateCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractFormActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.CameraInitiator;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.ICameraActivity;
import moshimoshi.cyplay.com.doublenavigation.utils.IntentUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.MarshMallowPermission;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import moshimoshi.cyplay.com.doublenavigation.view.CreateCustomerView;
import moshimoshi.cyplay.com.doublenavigation.view.UpdateCustomerInfoView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.utils.Constants;

/**
 * Created by damien on 10/05/16.
 */
public class CustomerFormActivity extends AbstractFormActivity<Customer> implements UpdateCustomerInfoView, CreateCustomerView, ICameraActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    @Inject
    UpdateCustomerInfoPresenter updateCustomerInfoPresenter;

    @Inject
    CreateCustomerInfoPresenter createCustomerInfoPresenter;

    private CameraInitiator cameraInitiator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateCustomerInfoPresenter.setView(this);
        createCustomerInfoPresenter.setView(this);


        // Get CustomerDetails
        if (getIntent().hasExtra(IntentConstants.EXTRA_CUSTOMER)) {
            formObject = IntentUtils.getExtraFromIntent(getIntent(), IntentConstants.EXTRA_CUSTOMER);
            if (formObject != null && formObject.getDetails() != null && getSupportActionBar() != null)
                getSupportActionBar().setTitle(formObject.getDetails().getFormatName(this));
        } else {
            formObject = new Customer();
            if (configHelper != null && configHelper.getCurrentShop() != null) {
                formObject.getDetails().setCreation_shop_id(configHelper.getCurrentShop().getId());
                formObject.getDetails().setMain_shop_id(configHelper.getCurrentShop().getId());
            }

            if (this.getIntent().hasExtra(IntentConstants.EXTRA_CUSTOMER_FORM_EAN)) {
                formObject.setEan_card(getIntent().getStringExtra(IntentConstants.EXTRA_CUSTOMER_FORM_EAN));
            }
        }
        init();

    }

    @Override
    public void onResume() {
        super.onResume();
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        }
    }


    protected void saveObject() {
        if (Constants.FORM_UPDATE_KEY.equals(formMode)) {
            updateCustomerInfoPresenter.updateCustomerProfile(new CustomerProfile(formObject));
        } else if (Constants.FORM_CREATE_KEY.equals(formMode)) {
            createCustomerInfoPresenter.createCustomerProfile(new CustomerProfile(formObject));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            cameraInitiator.imageRecieved();
        }
    }

    @Override
    public void setCameraInitiator(CameraInitiator cameraInitiator) {
        this.cameraInitiator = cameraInitiator;
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreateCustomerSuccess(Customer customer) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (customer != null) {
            Intent intent = new Intent(this, CustomerActivity.class);
            customerContext.setCustomerId(customer.getId());
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, SellerDashboardActivity.class);
            startActivity(intent);
        }
        finishForm(false);

    }

    @Override
    public void onUpdatedCustomerFailure(ExceptionType exceptionType, String status, String message) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        SnackBarHelper.buildSnackbar(coordinatorLayout,
                this.getString(R.string.form_update_error, message),
                Snackbar.LENGTH_INDEFINITE,
                this.getString(android.R.string.ok)).show();
    }


    @Override
    public void showLoading() {
        if (progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void onUpdatedCustomerSuccess(String customerId) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        Intent intent = new Intent(this, CustomerActivity.class);
        customerContext.setCustomerId(customerId);
        startActivity(intent);
    }


    @Override
    public void onCreateCustomerError(ExceptionType exceptionType, String status, String message) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        SnackBarHelper.buildSnackbar(coordinatorLayout,
                this.getString(R.string.form_create_error, message),
                Snackbar.LENGTH_INDEFINITE,
                this.getString(android.R.string.ok)).show();

    }


    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

}
