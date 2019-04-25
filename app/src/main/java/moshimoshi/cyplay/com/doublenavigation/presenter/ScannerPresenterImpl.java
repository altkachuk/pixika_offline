package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.BarCodeInfo;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.view.ScannerView;
import ninja.cyplay.com.apilibrary.domain.interactor.ScannerInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABarCodeInfo;
import ninja.cyplay.com.apilibrary.models.business.EScanFilters;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by anishosni on 29/04/15.
 */
public class ScannerPresenterImpl extends BasePresenter implements ScannerPresenter {

    @Inject
    APIValue apiValue;

    private Context context;
    private ScannerView scannerView;
    private ScannerInteractor scannerInteractor;

    public ScannerPresenterImpl(Context context, ScannerInteractor scannerInteractor) {
        super(context);
        this.context = context;
        this.scannerInteractor = scannerInteractor;
    }

    @Override
    public void setView(ScannerView view) {
        this.scannerView = view;
    }

    @Override
    public void checkScanCode(String scanner_string, final EScanFilters eScanFilters) {
        scannerView.showLoading();
        this.checkBarCodeType(scanner_string, eScanFilters);
    }

    private void checkBarCodeType(String scanner_string, final EScanFilters eScanFilters) {
        scannerInteractor.execute(scanner_string, eScanFilters, new AbstractResourceRequestCallback<PR_ABarCodeInfo>() {
            @Override
            public void onSuccess(final PR_ABarCodeInfo pr_aBarCodeInfo) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        scannerView.onGetCorrespondanceSuccess((BarCodeInfo) pr_aBarCodeInfo);
                    }
                });

            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        Log.e(LogUtils.generateTag(this), "Error on ScannerPresenterImpl");
                        scannerView.onError(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
                    }
                });

            }
        });
    }

}
