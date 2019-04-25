package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.abs.AbstractSellerLoginActivity;

/**
 * Created by romainlebouc on 20/04/16.
 */
public class SellerLogInActivity extends AbstractSellerLoginActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.connectedActivity = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }



}