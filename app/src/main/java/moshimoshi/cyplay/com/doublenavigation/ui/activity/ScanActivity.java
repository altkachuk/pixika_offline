package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.google.zxing.Result;

import javax.inject.Inject;

import butterknife.BindView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BarCodeInfo;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.ScanActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ScannerPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.MessageProgressDialog;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.MarshMallowPermission;
import moshimoshi.cyplay.com.doublenavigation.view.FilterResourceView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import moshimoshi.cyplay.com.doublenavigation.view.ScannerView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.business.EScanFilters;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.utils.Constants;
import ninja.cyplay.com.apilibrary.utils.enumeration.EBarCodeMapping;

/**
 * Created by romainlebouc on 22/04/16.
 */
public class ScanActivity extends BaseActivity implements ZXingScannerView.ResultHandler, ScannerView {

    @Inject
    ActionEventHelper actionEventHelper;

    @Inject
    CustomerContext customerContext;

    @Inject
    SellerContext sellerContext;

    @Inject
    ScannerPresenter scannerPresenter;

    @Inject
    GetProductPresenter getProductPresenter;

    @Inject
    GetCustomerInfoPresenter getCustomerInfoPresenter;

    @BindView(R.id.camera_view)
    ZXingScannerView scannerView;

    @BindView(R.id.scan_status)
    TextView scanStatus;


    private EScanFilters scanFilter = EScanFilters.SCAN_ALL;
    private MessageProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initScannerView();
        checkIntent();
        initPresenters();
        progressDialog = new MessageProgressDialog(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        } else {
            scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            scannerView.startCamera();          // Start camera on resume
        }
        // just in case
        scannerView.resumeCameraPreview(ScanActivity.this);
        progressDialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        switch (scanFilter) {
            case SCAN_CUSTOMER:
                this.getCustomer(rawResult.getText());

                break;
            case SCAN_PROD:
                this.getProduct(rawResult.getText());

                break;
            case SCAN_NEW_CUSTOMER:
                if (configHelper.isCorrectFidelityCardNumber(rawResult.getText())) {
                    showNewCustomerWindow(rawResult.getText());
                    actionEventHelper.log(new ScanActionEventData(EResource.CUSTOMER)
                            .setStatus(EActionStatus.SUCCESS)
                            .setScannedCode(rawResult.getText())
                    );
                } else {
                    onScanError(R.string.scan_customer_invalid_bar_code_title, R.string.scan_customer_invalid_bar_code_text);
                    actionEventHelper.log(new ScanActionEventData(EResource.CUSTOMER)
                            .setStatus(EActionStatus.FAILURE)
                            .setScannedCode(rawResult.getText())
                    );
                }
                break;
            default:
                scannerPresenter.checkScanCode(rawResult.getText(), scanFilter);
                break;
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initScannerView() {
        scannerView.setAutoFocus(true);

    }

    private void checkIntent() {
        if (getIntent() != null && getIntent().hasExtra(Constants.EXTRA_SCAN_FILTER)) {
            scanFilter = (EScanFilters) getIntent().getSerializableExtra(Constants.EXTRA_SCAN_FILTER);
            if (this.getSupportActionBar() != null) {
                this.getSupportActionBar().setTitle(scanFilter.getTitleResourceId());
            }
        }
    }

    private void initPresenters() {
        // Set views
        scannerPresenter.setView(this);
        getProductPresenter.setView(new ProductResourceViewResponseListener());
        getCustomerInfoPresenter.setView(new CustomerResourceViewResponseListener());
    }

    public void showCustomer(String cid) {
        progressDialog.dismiss();
        // will value the customer
        Intent intent = new Intent(this, CustomerActivity.class);
        // id of the customer to value for
        customerContext.setCustomerId(cid);
        startActivity(intent);
        finish();
    }

    public void showNewCustomerWindow(final String barCode) {

        // Show the popup
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.add_newcustomer_popup_title)
                .setMessage(R.string.add_newcustomer_popup_text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ScanActivity.this, CustomerFormActivity.class);
                        intent.putExtra(IntentConstants.EXTRA_CUSTOMER_FORM_EAN, barCode);
                        intent.putExtra(IntentConstants.EXTRA_FORM_EDIT_MODE, Constants.FORM_CREATE_KEY);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        scannerView.resumeCameraPreview(ScanActivity.this);
                        ScanActivity.this.progressDialog.dismiss();
                    }
                })
                .show();
    }

    public void onScanError(final int titleResourceId, final int messageResourceId) {
        progressDialog.dismiss();

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new AlertDialog.Builder(ScanActivity.this)
                        .setCancelable(false)
                        .setTitle(titleResourceId)
                        .setMessage(messageResourceId)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                scannerView.resumeCameraPreview(ScanActivity.this);
                            }
                        })
                        .show();
            }
        });

    }

    public void onScanError() {
        onScanError(R.string.barcode_notfound_popup_title, R.string.barcode_notfound_popup_text);
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl common
    // -------------------------------------------------------------------------------------------

    @Override
    public void showLoading() {
        progressDialog.show();
    }


    private class ProductResourceViewResponseListener implements FilterResourceView<Product, ProductFilter> {

        @Override
        public void onResourceViewResponse(FilterResourceResponseEvent<Product, ProductFilter> resourceResponseEvent) {
            ScanActivity.this.setStatus(android.view.View.GONE, -1);
            if (resourceResponseEvent.getResource() != null) {
                Intent intent = new Intent(ScanActivity.this, ProductWithSelectorsActivity.class);
                intent.putExtra(IntentConstants.EXTRA_PRODUCT_ID, (resourceResponseEvent.getResource()).getId());
                intent.putExtra(IntentConstants.EXTRA_PRODUCT_NAME, (resourceResponseEvent.getResource()).getName());
                startActivity(intent);
                finish();
            } else if (resourceResponseEvent.getResourceException() != null
                    && resourceResponseEvent.getResourceException().getMessage() != null
                    && resourceResponseEvent.getResourceException().getMessage().length() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                builder.setMessage(resourceResponseEvent.getResourceException().getMessage())
                        .setCancelable(false)
                        .setTitle(R.string.util_attention)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                scannerView.resumeCameraPreview(ScanActivity.this);
                            }
                        });
                builder.create().show();
            } else if (resourceResponseEvent.getResource() == null) {
                onScanError(R.string.scan_product_not_found_title, R.string.scan_product_not_found_text);
            }
        }
    }

    private class CustomerResourceViewResponseListener implements ResourceView<Customer> {

        @Override
        public void onResourceViewResponse(ResourceResponseEvent<Customer> resourceResponseEvent) {
            ScanActivity.this.setStatus(android.view.View.GONE, -1);
            if (resourceResponseEvent.getResource() != null) {
                progressDialog.dismiss();
                // will value the customer
                Intent intent = new Intent(ScanActivity.this, CustomerActivity.class);
                // id of the customer to value for
                customerContext.setCustomerId(resourceResponseEvent.getResource().getId());
                startActivity(intent);
                finish();
            } else if (resourceResponseEvent.getResourceException() != null
                    && resourceResponseEvent.getResourceException().getMessage() != null
                    && resourceResponseEvent.getResourceException().getMessage().length() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                builder.setMessage(resourceResponseEvent.getResourceException().getMessage())
                        .setCancelable(false)
                        .setTitle(R.string.util_attention)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                scannerView.resumeCameraPreview(ScanActivity.this);
                            }
                        });
                builder.create().show();
            } else if (resourceResponseEvent.getResource() == null) {
                onScanError(R.string.scan_customer_invalid_bar_code_title, R.string.scan_customer_invalid_bar_code_text);
            }
        }

    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        onScanError();
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl GetCorrespondence
    // -------------------------------------------------------------------------------------------

    @Override
    public void onGetCorrespondanceSuccess(BarCodeInfo barCodeInfo) {
        EBarCodeMapping barCodeMapping = EBarCodeMapping.valueofCode(barCodeInfo.getEanc());
        if (barCodeMapping == null) {
            onScanError();
        } else {
            switch (scanFilter) {
                case SCAN_ALL:
                    switch (barCodeMapping) {
                        case CORRESPONDENCE_CARD_CODE:
                            showCustomer(barCodeInfo.getCustomer_id());
                            break;
                        case CORRESPONDENCE_PRODUCT:
                            this.getProduct(barCodeInfo.getProduct_id());
                            break;
                        case CORRESPONDENCE_CUSTOMER_TO_ADD:
                        case CORRESPONDENCE_CUSTOMER_TO_ADD_OR_UPDATE:
                            showNewCustomerWindow(barCodeInfo.getCustomer_id());
                            break;
                        default:
                            onScanError();
                    }
                    break;
                case SCAN_PROD:
                    switch (barCodeMapping) {
                        case CORRESPONDENCE_PRODUCT:
                            this.getProduct(barCodeInfo.getProduct_id());
                            break;
                        default:
                            onScanError();
                    }
                    break;
                case SCAN_NEW_CUSTOMER:
                    showNewCustomerWindow(barCodeInfo.getCustomer_id());
                    break;
                case SCAN_CUSTOMER:
                    switch (barCodeMapping) {
                        case CORRESPONDENCE_CARD_CODE:
                            showCustomer(barCodeInfo.getCustomer_id());
                            break;
                        case CORRESPONDENCE_CUSTOMER_TO_ADD:
                        case CORRESPONDENCE_CUSTOMER_TO_ADD_OR_UPDATE:
                            showNewCustomerWindow(barCodeInfo.getCustomer_id());
                            break;
                        default:
                            onScanError(R.string.scan_customer_invalid_bar_code_title,
                                    R.string.scan_customer_invalid_bar_code_text);
                    }
                    break;
                case SCAN_PROD_OR_EMPTY_CARD:
                    switch (barCodeMapping) {
                        case CORRESPONDENCE_CARD_CODE:
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(R.string.scan_card_already_assigned_message)
                                    .setCancelable(false)
                                    .setTitle(R.string.scan_card_already_assigned_title)
                                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            scannerView.resumeCameraPreview(ScanActivity.this);
                                        }
                                    });
                            // Create the AlertDialog object and return it
                            builder.create().show();
                            break;
                        case CORRESPONDENCE_CUSTOMER_TO_ADD:
                        case CORRESPONDENCE_CUSTOMER_TO_ADD_OR_UPDATE:
                            showNewCustomerWindow(barCodeInfo.getCustomer_id());
                            break;
                        case CORRESPONDENCE_PRODUCT:
                            this.getProduct(barCodeInfo.getProduct_id());
                            break;
                        default:
                            onScanError();
                    }

            }
        }

    }

    private void getCustomer(String ean) {
        getCustomerInfoPresenter.getCustomerFromEan(ean);
        ScanActivity.this.setStatus(android.view.View.VISIBLE, R.string.scan_retrieving_customer_data);
    }

    private void getProduct(String ean) {
        getProductPresenter.getProductInfoFromBarCode(ean);
        ScanActivity.this.setStatus(android.view.View.VISIBLE, R.string.scan_retrieving_customer_data);
    }

    private void setStatus(int visibility, int labelId) {
        this.scanStatus.setVisibility(visibility);
        if (labelId > 0) {
            this.scanStatus.setText(labelId);
        }
    }


}