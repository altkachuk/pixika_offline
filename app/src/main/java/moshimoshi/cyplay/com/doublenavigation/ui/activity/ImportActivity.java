package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import javax.inject.Inject;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.presenter.ImportPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.LocalDBFragment;
import moshimoshi.cyplay.com.doublenavigation.view.ImportView;

/**
 * Created by romainlebouc on 20/04/16.
 */
public class ImportActivity extends BaseActivity implements ImportView {

    @Inject
    ImportPresenter importPresenter;

    @Inject
    IDatabaseHandler databaseHandler;

    private LocalDBFragment localDBFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.connectedActivity = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        // id fragment
        localDBFragment = (LocalDBFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_local_db);

        importPresenter.setView(this);

        importPresenter.loadConfig();

        checkPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    1);
        } else {
            load();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int res = grantResults[i];

            if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE) && res == PackageManager.PERMISSION_GRANTED) {
                load();
            }
        }
    }

    public void load() {
        if (databaseHandler.productPopulated()) {
            onLoadedCmplete();
        } else {
            localDBFragment.setState(LoadingView.LoadingState.LOADING);
            importPresenter.getDataOut();
        }
    }

    // -------------------------------------------------------------------------------------------
    // ImportView


    @Override
    public void onLoadedCmplete() {
        localDBFragment.setState(LoadingView.LoadingState.LOADED);
        localDBFragment.start();
    }

    @Override
    public void onLoadedError() {
        localDBFragment.setState(LoadingView.LoadingState.NO_RESULT);
        localDBFragment.showLoadButton();
    }

    @Override
    public void onLoading(String text) {
        localDBFragment.addDebugText(text);
    }

    // ImportView
    // -------------------------------------------------------------------------------------------



}
