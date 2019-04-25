package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import atproj.cyplay.com.dblibrary.util.FileUtil;
import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.BuildConfig;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EShopType;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetConfigPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetShopsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProvisionDevicePresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.DeviceRegistrationActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.ImportActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerTeamActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.SharedPreferenceConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import moshimoshi.cyplay.com.doublenavigation.view.GetConfigView;
import moshimoshi.cyplay.com.doublenavigation.view.GetShopsView;
import moshimoshi.cyplay.com.doublenavigation.view.ProvisionDeviceView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

import static android.content.Context.MODE_PRIVATE;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.constants.SharedPreferenceConstants.APP_INITIALIZED;

/**
 * Created by damien on 25/04/16.
 */
public class SplashScreenFragment extends BaseFragment implements GetConfigView, GetShopsView, ProvisionDeviceView {

    private final static List<String> FIELDS = new ArrayList<>();
    private final static List<String> SORT_BYS = new ArrayList<>();

    @Inject
    IDatabaseHandler databaseHandler;

    @Inject
    GetConfigPresenter getConfigPresenter;

    @Inject
    GetShopsPresenter getShopsPresenter;

    @Inject
    ProvisionDevicePresenter provisionDevicePresenter;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.state_loading_view)
    LoadingView stateLoadingView;

    @Nullable
    @BindView(R.id.background_image)
    ImageView backgroundImageView;

    @BindView(R.id.debug_text_view)
    TextView debugTextView;

    @BindView(R.id.version)
    TextView version;

    private String shopId;
    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // setView of presenter
        getConfigPresenter.setView(this);

        getShopsPresenter.setView(this);

        provisionDevicePresenter.setView(this);
        // debug gesture
        initDebugGesture();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // image Backgroung
        if (backgroundImageView != null) {
            Picasso.get()
                    .load(R.drawable.splash)
                    .fit()
                    .centerInside()
                    .into(backgroundImageView);
        }
        version.setText(BuildConfig.CLIENT_NAME + "-" + BuildConfig.BUILD_TYPE + " " + BuildConfig.VERSION_NAME);

    }

    @Override
    public void onStart() {
        super.onStart();

        if (databaseHandler.productPopulated()) {
            getConfig();
        } else {
            // start loading DB activity
            goToActivity(ImportActivity.class);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void initDebugGesture() {
        coordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();
            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);
                        if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap
                            numberOfTaps = 0;
                            lastTapTimeMs = 0;
                            break;
                        }

                        if (numberOfTaps > 0 && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                        }
                        lastTapTimeMs = System.currentTimeMillis();
                        if (numberOfTaps == 3) {
                            //handle triple tap
                            debugTextView.setVisibility(View.VISIBLE);
                        }
                }
                return true;
            }
        });
    }

    private void goToActivity(Class activityClass) {
        Intent intent = new Intent(getContext(), activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }


    public void provisionDevice(String shopId) {
        this.shopId = shopId;
        provisionDevicePresenter.provisionDevice(shopId);
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void showLoading() {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
    }

    @Override
    public void onConfigSuccess() {
        if (this.getActivity() != null && isAdded()) {
            goToActivity(SellerTeamActivity.class);
        }
    }

    @Override
    public void goToDeviceRegistration() {
        //save shopId in sharePreferences
        SharedPreferences sp = getActivity().getSharedPreferences(SharedPreferenceConstants.SP_CONFIGURATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SharedPreferenceConstants.SP_EXTRA_SHOP_ID, shopId);
        editor.apply();

        getShopsPresenter.getShops(true, FIELDS, SORT_BYS, EShopType.REGULAR);
    }

    @Override
    public void appNotSupported(String appVersion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.Theme_AlertDialog);
        String message = this.getContext().getResources().getString(R.string.app_version_not_suppored, appVersion);
        builder
                .setTitle(R.string.warning)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SplashScreenFragment.this.getActivity().finish();
                    }
                })
                .create().show();
    }


    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        if (this.getActivity() != null && isAdded()) {
            stateLoadingView.setLoadingState(LoadingView.LoadingState.FAILED);
            SnackBarHelper.buildSnackbar(coordinatorLayout, getString(R.string.reload_error_msg), null).show();
            debugTextView.setText(status + "\n" + message);
        }
    }

    @Override
    public void onShopsSuccess(List<Shop> shops) {
        provisionDevice(shops.get(0).getId());
    }

    @Override
    public void onShopsError() {

    }

    @Override
    public void onProvisionDeviceSuccess() {
        getConfig();
        //goToActivity(SellerTeamActivity.class);
    }

    @Override
    public void onProvisionDeviceError() {

    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.reload_button)
    public void onReloadClick() {
        getConfigPresenter.getConfig(shopId);
    }

    private void getConfig() {
        // id The config
        //get the shopId from sharePreference
        SharedPreferences sp = getActivity().getSharedPreferences(SharedPreferenceConstants.SP_CONFIGURATION, MODE_PRIVATE);
        shopId = sp.getString(SharedPreferenceConstants.SP_EXTRA_SHOP_ID, "");

        // Hackish delay to avoid certificate errors...
        SharedPreferences prefs = this.getContext().getSharedPreferences(SharedPreferenceConstants.SP_CONFIGURATION, MODE_PRIVATE);
        boolean initialized = prefs.getBoolean(APP_INITIALIZED, false);
        if (initialized) {
            getConfigPresenter.getConfig(shopId);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getConfigPresenter.getConfig(shopId);
                    SharedPreferences.Editor editor = SplashScreenFragment.this.getContext().getSharedPreferences(SharedPreferenceConstants.SP_CONFIGURATION, MODE_PRIVATE).edit();
                    editor.putBoolean(APP_INITIALIZED, true);
                    editor.commit();
                }
            }, 2048);
        }
    }

}