package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import javax.inject.Inject;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.presenter.SynchronizationPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.view.SynchronizationView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by wentongwang on 22/06/2017.
 */

public class SynchronizationActivity extends MenuBaseActivity implements SynchronizationView {

    @Inject
    SynchronizationPresenter synchronizationPresenter;

    @Inject
    IDatabaseHandler databaseHandler;

    @BindView(R.id.state_loading_view)
    LoadingView stateLoadingView;

    @BindView(R.id.debug_text)
    TextView debugText;

    @BindView(R.id.sync_button)
    Button syncButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchroniation);

        synchronizationPresenter.setView(this);

        stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        synchronizationPresenter.loadSyncData();
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_SYNCHRONIZATION_DATA.getCode();
    }

    @OnClick(R.id.sync_button)
    protected void onClickSyncButton() {
        syncButton.setVisibility(View.GONE);
        stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
        synchronizationPresenter.sendDataIn();
    }


    public void clearDebugText() {
        debugText.setText("");
    }

    public void addDebugText(final String text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String s = debugText.getText().toString();
                String[] sarr = s.split("\n");
                if (sarr.length > 10) {
                    s = "";
                    for (int i = sarr.length - 10; i < sarr.length; i++) {
                        s += "\n" + sarr[i];
                    }
                }
                s += "\n" + text;
                debugText.setText(s);
            }
        });
    }

    // -------------------------------------------------------------------------------------------
    // SynchronizationView


    @Override
    public void onLoadedSyncDataComlete(int updatedCustomers, int sales) {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        clearDebugText();
        addDebugText("New/Updated contacts: " + updatedCustomers);
        addDebugText("New orders: " + sales);

    }

    @Override
    public void onLoadedSyncDataError() {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.NO_RESULT);
    }

    @Override
    public void onExportLoadedCmplete() {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        addDebugText("Export complete");

        synchronizationPresenter.clear();
        addDebugText("Data deleted");

        goToActivity(ImportActivity.class);
    }

    @Override
    public void onExportLoadedError() {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.NO_RESULT);
        addDebugText("Error: try again");
        syncButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadedCmplete() {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);

        Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onLoadedError() {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.NO_RESULT);
        addDebugText("Error: try again");
        syncButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoading(String text) {
        addDebugText(text);
    }

    // SynchronizationView
    // -------------------------------------------------------------------------------------------

    private void goToActivity(Class activityClass) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
}
