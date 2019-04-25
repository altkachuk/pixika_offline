package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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
public class StatsLineChartFragment extends StatsFragment {

    @BindView(R.id.root)
    View root;

    @BindView(R.id.line_chart)
    LineChart lineChart;

    public StatsLineChartFragment() {
    }

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_line_chart, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateDesign();
        initChart();
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

    private void initChart() {
        if (chart != null) {
            // Set title and date
            updateInfos();

            // Default config
            ChartsUtil.setChartConfig(lineChart, chart);
            // Set aXis
            ChartsUtil.setAxisConfig(lineChart.getAxisLeft(), chart.getLeftAxis());
            ChartsUtil.setAxisConfig(lineChart.getAxisRight(), chart.getRightAxis());
            ChartsUtil.setXAxis(lineChart.getXAxis(), chart.getxAxis());

            // Data
            LineData lineData = getData(chart.getDatasets());
            lineChart.setData(lineData);
        }
    }

    private LineData getData(HashMap<String, PR_DataSet> chartDataSets) {
        LineData data = null;
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        ArrayList<String> xVals = null;
        if (chartDataSets != null) {
            int position = 0;
            for (Map.Entry<String, PR_DataSet> entry : chartDataSets.entrySet()) {
                PR_DataSet prDataSet = entry.getValue();
                // save first xval
                if (xVals == null)
                    xVals = prDataSet.getxVals();
                // id Yvals
                ArrayList<Entry> yVals = new ArrayList<>();
                if (prDataSet.getyVals() != null)
                    for (int i = 0; i < prDataSet.getyVals().size(); i++)
                        yVals.add(new Entry(prDataSet.getyVals().get(i), i));
                // Add dataset to container
                LineDataSet lineDataSet = new LineDataSet(yVals, entry.getKey());

                // Line config
                lineDataSet.setLineWidth(prDataSet.lineWidth);
                // line will be curved
                lineDataSet.setDrawCubic(true);
                lineDataSet.setCubicIntensity(prDataSet.cubicIntensity);
                // circle hole
                lineDataSet.setDrawCircleHole(prDataSet.drawCircleHole);
                lineDataSet.setCircleColorHole(Color.TRANSPARENT);

                // set DataSet color
                if (prDataSet.getColors() != null) {
                    int[] calculatedColors = new int[prDataSet.getColors().size()];
                    for (int i = 0 ; i < prDataSet.getColors().size() ; i++)
                        calculatedColors[i] = Color.parseColor(prDataSet.getColors().get(i));
                    lineDataSet.setColors(calculatedColors);
                    if (calculatedColors.length > position)
                        lineDataSet.setCircleColor(calculatedColors[position]);
                    else if (calculatedColors.length > 0)
                        lineDataSet.setCircleColor(calculatedColors[0]);
                }

                if (prDataSet.getLabel() != null ) {
                    if (prDataSet.getLabel().getFontSize() != null)
                        lineDataSet.setValueTextSize(Float.valueOf(prDataSet.getLabel().getFontSize()));
                    if (prDataSet.getLabel().getColor() != null)
                        lineDataSet.setValueTextColor(Color.parseColor(prDataSet.getLabel().getColor()));
                }

                dataSets.add(lineDataSet);
                position++;
            }
            // create a data object with the datasets
            data = new LineData(xVals, dataSets);
        }
        return data;
    }

}
