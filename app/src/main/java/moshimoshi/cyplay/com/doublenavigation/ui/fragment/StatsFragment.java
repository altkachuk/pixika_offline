package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.graphics.Color;
import android.widget.TextView;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import ninja.cyplay.com.apilibrary.models.business.PR_Chart;

/**
 * Created by damien on 27/05/16.
 */
public class StatsFragment extends BaseFragment {

    @BindView(R.id.stats_title)
    TextView title;

    @BindView(R.id.stats_date)
    TextView date;

    protected PR_Chart chart;

    public StatsFragment() {

    }

    public void setChart(PR_Chart chart) {
        this.chart = chart;
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    protected void updateInfos() {
        if (chart != null) {
            // update colors
//            if (chart.getxAxisLabelTextColor() != null) {
//                title.setTextColor(Color.parseColor(chart.getxAxisLabelTextColor()));
//                date.setTextColor(Color.parseColor(chart.getxAxisLabelTextColor()));
//            }

            title.setText(chart.getTitle());
            title.setTextColor(Color.WHITE);
//            date.setText(chart.getTitle());
            date.setText("26 Jan. 2016");
            date.setTextColor(Color.WHITE);
        }
    }

}
