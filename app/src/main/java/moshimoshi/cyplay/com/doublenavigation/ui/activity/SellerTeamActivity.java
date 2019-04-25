package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.fragment.SellerTeamFragment;

/**
 * Created by romainlebouc on 20/04/16.
 */
public class SellerTeamActivity extends BaseActivity {

    private SellerTeamFragment sellerTeamFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.connectedActivity = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_team);
        // id fragment
        sellerTeamFragment = (SellerTeamFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_seller_team);
        // set name
        if (getSupportActionBar() != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(getString(R.string.activity_seller_team));
            if (configHelper.getCurrentShop() != null){
                builder.append(" - ").append(configHelper.getCurrentShop().getLong_name());
            }
            getSupportActionBar().setTitle(builder.toString());
        }

    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seller_team, menu);

        super.applyTintColorToMenuItems(menu,
                new int[]{R.id.action_update, R.id.action_store});
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_update:
                if (sellerTeamFragment != null) {
                    sellerTeamFragment.loadSellersList();
                    return true;
                }
                break;
            case R.id.action_store:
                Intent intent = new Intent(SellerTeamActivity.this, DeviceRegistrationActivity.class);
                intent.putExtra(IntentConstants.EXTRA_EDIT_STORE, true);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
