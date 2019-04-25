package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.view.ScannerView;
import ninja.cyplay.com.apilibrary.models.business.EScanFilters;

/**
 * Created by damien on 29/04/15.
 */
public interface ScannerPresenter extends Presenter<ScannerView> {

    void checkScanCode(String scanner_string, EScanFilters eScanFilters);

}
