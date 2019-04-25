package moshimoshi.cyplay.com.doublenavigation.ui.activity.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;


import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.utils.DesignUtils;
import moshimoshi.cyplay.com.doublenavigation.utils.FeatureColor;

/**
 * Created by damien on 13/04/16.
 */
public abstract class BasketBasedActivity extends BaseActivity {

    // Navigation drawer:
    @Nullable
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    // Primary toolbar and drawher toggle
    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    // Item Invalid for left menu
    protected static final int NAVDRAWER_ITEM_INVALID = -1;


    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // setup navigation drawer
        setupNavDrawer();

    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        // Close menu first if one is open
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    protected Toolbar getActionBarToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            DesignUtils.setBackgroundColor(toolbar, Color.WHITE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        return toolbar;
    }

    private void setupNavDrawer() {
        if (drawerLayout == null || toolbar == null) {
            return;
        }
        drawerLayout.setStatusBarBackgroundColor(Color.parseColor(FeatureColor.getColorHex(this, FeatureColor.PRIMARY_DARK, configHelper)));
    }

}
