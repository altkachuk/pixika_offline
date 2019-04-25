package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by romainlebouc on 02/09/16.
 */
public class OffersActivity extends MenuBaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_offers);
    }
    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_OFFERS.getCode();
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }


}
