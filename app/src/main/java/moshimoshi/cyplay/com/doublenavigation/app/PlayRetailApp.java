package moshimoshi.cyplay.com.doublenavigation.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.adyen.library.exceptions.AlreadyRegisteredException;
import com.adyen.library.real.LibraryReal;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.lang.reflect.Method;

import io.fabric.sdk.android.Fabric;
import moshimoshi.cyplay.com.doublenavigation.BuildConfig;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SplashScreenActivity;
import moshimoshi.cyplay.com.doublenavigation.utils.ClientSpec;
import moshimoshi.cyplay.com.doublenavigation.utils.ScreenLogOutReceiver;
import ninja.cyplay.com.apilibrary.MVPCleanArchitectureApplication;
import ninja.cyplay.com.apilibrary.domain.interactor.SellerInteractor;
import ninja.cyplay.com.apilibrary.models.business.enums.SSLCertificateSignatureType;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.models.singleton.ClientApplication;
import ninja.cyplay.com.apilibrary.utils.ClientUtil;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

public class PlayRetailApp extends MVPCleanArchitectureApplication {

    private BroadcastReceiver screenLogoutReceiver;
    private Tracker mTracker;
    private boolean analyticsEnabled = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        // Feed the factory of the API with the client's models
        ClientSpec.feedFactory();

        ClientUtil.setClientUrl(BuildConfig.BUILD_TYPE, BuildConfig.HOST);
        ClientUtil.setSslCertificateSignatureType(SSLCertificateSignatureType.valueOfCode(this.getResources().getInteger(R.integer.api_ssl_certifcate_type)));
        ClientUtil.setClientId(BuildConfig.BUILD_TYPE, BuildConfig.CLIENT_ID);
        ClientUtil.setClientSecret(BuildConfig.BUILD_TYPE, BuildConfig.CLIENT_SECRET);
        ClientUtil.setVerifyHostNameCertificate(BuildConfig.VERIFY_HOST_NAME_CERTIFICATE);

        ClientApplication.getInstance().setApplication(this);
        ClientApplication.getInstance().setRestartActivity(SplashScreenActivity.class);

        // Analytics
        Fabric.with(this, new Answers());

        initializeDependencies();

        // TODO : register only if Adyen Payment is enable
        try {
            String appName = getApplicationContext().getString(getApplicationContext().getApplicationInfo().labelRes);
            LibraryReal.registerLib(this, appName);
        } catch (AlreadyRegisteredException e) {
            Log.e(LogUtils.generateTag(this), e.getMessage(), e);
        }

        // Google analytics
        GoogleAnalytics.getInstance(this).setDryRun(!analyticsEnabled);
        if (!analyticsEnabled) {
            GoogleAnalytics.getInstance(this).setAppOptOut(!analyticsEnabled);
        }
        getDefaultTracker().enableAutoActivityTracking(true);

    }

    private boolean daggerInitialized = false;

    private void initializeDependencies() {
        if (!daggerInitialized) {
            // Initialize dependencies components that will be injected
            Injector.INSTANCE.initializeComponent(this);
            inject(this);
            daggerInitialized = true;
        }
    }

    public void inject(Object object) {
        try {
            Method method = Injector.INSTANCE.getPlayRetailComponent().getClass().getMethod("inject", object.getClass());
            method.invoke(Injector.INSTANCE.getPlayRetailComponent(), object);
        } catch (Exception e) {
            // If method not found no injection will be made
        }
    }

    public static PlayRetailApp get(Context context) {
        return (PlayRetailApp) context.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void unRegisterScreenLogoutReceiver() {
        try {
            unregisterReceiver(screenLogoutReceiver);
            screenLogoutReceiver = null;
        } catch (IllegalArgumentException e) {
            Log.e(LogUtils.generateTag(this), e.toString());
        }

    }

    public void registerScreenLogoutReceiver(SellerInteractor sellerInteractor,
                                             APIValue apiValue,
                                             SellerContext sellerContext,
                                             CustomerContext customerContext) {
        if (screenLogoutReceiver == null) {
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            screenLogoutReceiver = new ScreenLogOutReceiver(sellerInteractor, apiValue, sellerContext, customerContext);
            registerReceiver(screenLogoutReceiver, filter);
        }
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

}