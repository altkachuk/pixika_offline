package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by wentongwang on 07/03/2017.
 */

public class OffersByTypeActivity extends MenuBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_by_type);
    }
    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_OFFERS_BY_TYPE.getCode();
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }


}
