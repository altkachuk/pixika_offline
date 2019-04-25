package moshimoshi.cyplay.com.doublenavigation.ui.activity.abs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.SellerLoginWithTemporaryPasswordChangeActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;

import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_PASSWORD_RESET;
import static moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants.EXTRA_SELLER;

/**
 * Created by romainlebouc on 03/02/2017.
 */

public class AbstractSellerLoginActivity extends BaseActivity {

    @Inject
    SellerContext sellerContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getIntent().hasExtra(EXTRA_PASSWORD_RESET) && this.getIntent().getBooleanExtra(EXTRA_PASSWORD_RESET, true)) {
            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.activity_authentication_change_password);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_password:
                this.supportFinishAfterTransition();
                Intent intent = new Intent(this, SellerLoginWithTemporaryPasswordChangeActivity.class);
                //intent.putExtra(EXTRA_SELLER, this.getIntent().getParcelableExtra(EXTRA_SELLER));
                this.startActivity(intent);
                break;
            case android.R.id.home:
                sellerContext.clearContext();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sellerContext.clearContext();
    }

}
