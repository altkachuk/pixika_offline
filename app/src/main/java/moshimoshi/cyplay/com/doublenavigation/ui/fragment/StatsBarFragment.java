package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.utils.ChartsUtil;
import ninja.cyplay.com.apilibrary.models.business.PR_DataSet;

/**
 * Created by damien on 31/05/16.
 */
public class StatsBarFragment extends StatsFragment {

    @BindView(R.id.root)
    View root;

    private Boolean horizontalChart = false;



    public void setHorizontalChart(Boolean horizontalChart) {
        this.horizontalChart = horizontalChart;
    }
// -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (horizontalChart)
            return inflater.inflate(R.layout.fragment_stats_horizontal_bar, container, false);
        return inflater.inflate(R.layout.fragment_stats_vertical_bar, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateDesign();
        initChart((BarChart) view.findViewById(R.id.bar_chart));
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void updateDesign() {
        if (chart != null) {
            if (chart.getBackgroundColor() != null)
                root.setBackgroundColor(Color.parseColor(chart.getBackgroundColor()));
        }
    }

    private void initChart(BarLineChartBase barChart) {
        if (chart != null) {
            // Set title and date
            updateInfos();

            // Default config
            ChartsUtil.setChartConfig(barChart, chart);
            // Set aXis
            ChartsUtil.setAxisConfig(barChart.getAxisLeft(), chart.getLeftAxis());
            ChartsUtil.setAxisConfig(barChart.getAxisRight(), chart.getRightAxis());
            ChartsUtil.setXAxis(barChart.getXAxis(), chart.getxAxis());

            // Data
            BarData barData = getData(chart.getDatasets());
            barChart.setData(barData);
        }
    }

    private BarData getData(HashMap<String, PR_DataSet> chartDataSets) {
        BarData data = null;
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        ArrayList<String> xVals = null;
        if (chartDataSets != null) {
            for (Map.Entry<String, PR_DataSet> entry : chartDataSets.entrySet()) {
                PR_DataSet prDataSet = entry.getValue();
                // save first xval
                if (xVals == null)
                    xVals = prDataSet.getxVals();
                // id Yvals
                ArrayList<BarEntry> yVals = new ArrayList<>();
                if (prDataSet.getyVals() != null)
                    for (int i = 0; i < prDataSet.getyVals().size(); i++)
                        yVals.add(new BarEntry(prDataSet.getyVals().get(i), i));
                // Add dataset to container
                BarDataSet barDataSet = new BarDataSet(yVals, entry.getKey());

                // set DataSet color
                if (prDataSet.getColors() != null) {
                    int[] calculatedColors = new int[prDataSet.getColors().size()];
                    for (int i = 0 ; i < prDataSet.getColors().size() ; i++)
                        calculatedColors[i] = Color.parseColor(prDataSet.getColors().get(i));
                    barDataSet.setColors(calculatedColors);
                }
                if (prDataSet.getLabel() != null ) {
                    if (prDataSet.getLabel().getFontSize() != null)
                        barDataSet.setValueTextSize(Float.valueOf(prDataSet.getLabel().getFontSize()));
                    if (prDataSet.getLabel().getColor() != null)
                        barDataSet.setValueTextColor(Color.parseColor(prDataSet.getLabel().getColor()));
                }

                dataSets.add(barDataSet);
            }
            // create a data object with the datasets
            data = new BarData(xVals, dataSets);
        }
        return data;
    }

}
