package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;


/**
 * Created by romainlebouc on 16/11/16.
 */
public class BasketActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

    }

    protected boolean isLeftCrossClosable() {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
    }

    public void finishBasket() {
        supportFinishAfterTransition();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
    }

}
