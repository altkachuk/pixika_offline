package moshimoshi.cyplay.com.doublenavigation.view;

import moshimoshi.cyplay.com.doublenavigation.model.business.BarCodeInfo;

/**
 * Created by anishosni on 28/04/15.
 */
public interface ScannerView extends BaseView {

    void showLoading();

    void onGetCorrespondanceSuccess(BarCodeInfo barCodeInfo);

}
