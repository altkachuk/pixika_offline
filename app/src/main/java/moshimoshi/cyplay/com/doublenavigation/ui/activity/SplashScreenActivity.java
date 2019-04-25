package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;

/**
 * Created by damien on 22/04/16.
 */
public class SplashScreenActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.connectedActivity = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
