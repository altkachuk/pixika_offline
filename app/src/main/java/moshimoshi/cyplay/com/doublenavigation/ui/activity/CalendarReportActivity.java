package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;


/**
 * Created by damien on 18/04/16.
 */
public class CalendarReportActivity extends BaseActivity {

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_report);
    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @Nullable
    @OnClick(R.id.close_button)
    public void onCloseClick() {
        supportFinishAfterTransition();
    }

}
