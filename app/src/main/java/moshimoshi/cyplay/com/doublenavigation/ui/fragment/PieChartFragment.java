package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

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
public class PieChartFragment extends StatsFragment {

    @BindView(R.id.root)
    View root;

    @BindView(R.id.pie_chart)
    PieChart pieChart;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_pie_chart, container, false);
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

            if (!chart.getInteractionEnabled())
                ChartsUtil.disableUserActions(pieChart);

            // Default config
            ChartsUtil.setChartConfig(pieChart, chart);

            // Data
            PieData pieData = getData(chart.getDatasets());
            pieChart.setData(pieData);
        }
    }

    private PieData getData(HashMap<String, PR_DataSet> chartDataSets) {
        PieData data = null;
        PieDataSet pieDataSet;
        ArrayList<String> xVals;
        if (chartDataSets != null) {
            for (Map.Entry<String, PR_DataSet> entry : chartDataSets.entrySet()) {
                PR_DataSet prDataSet = entry.getValue();
                // xvals
                xVals = prDataSet.getxVals();

                ArrayList<Entry> yVals = new ArrayList<>();
                if (prDataSet.getyVals() != null)
                    for (int i = 0; i < prDataSet.getyVals().size(); i++)
                        yVals.add(new Entry(prDataSet.getyVals().get(i), i));

                // Create dataSet
                pieDataSet = new PieDataSet(yVals, entry.getKey());

                // set DataSet color
                if (prDataSet.getColors() != null) {
                    int[] calculatedColors = new int[prDataSet.getColors().size()];
                    for (int i = 0; i < prDataSet.getColors().size(); i++)
                        calculatedColors[i] = Color.parseColor(prDataSet.getColors().get(i));
                    pieDataSet.setColors(calculatedColors);
                }

                if (prDataSet.getLabel() != null) {
                    pieDataSet.setDrawValues(prDataSet.getLabel().enabled);
                    pieDataSet.setValueTextColor(Color.parseColor(prDataSet.getLabel().getColor()));
                    if (prDataSet.getLabel().getFontSize() != null)
                        pieDataSet.setValueTextSize(Float.valueOf(prDataSet.getLabel().getFontSize()));
                }

                // Data
                data = new PieData(xVals, pieDataSet);
            }
        }
        return data;
    }

}
