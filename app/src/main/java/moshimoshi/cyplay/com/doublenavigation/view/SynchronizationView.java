package moshimoshi.cyplay.com.doublenavigation.view;

/**
 * Created by andre on 18-Mar-19.
 */

public interface SynchronizationView {

    void onLoadedSyncDataComlete(int updatedCustomers, int sales);
    void onLoadedSyncDataError();

    void onExportLoadedCmplete();
    void onExportLoadedError();

    void onLoadedCmplete();
    void onLoadedError();

    void onLoading(String text);
}
