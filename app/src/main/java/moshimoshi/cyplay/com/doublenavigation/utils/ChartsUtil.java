package moshimoshi.cyplay.com.doublenavigation.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;

import ninja.cyplay.com.apilibrary.models.business.PR_Chart;
import ninja.cyplay.com.apilibrary.models.business.charts.PR_ChartAxis;
import ninja.cyplay.com.apilibrary.models.business.charts.PR_ChartLegend;

/**
 * Created by damien on 31/05/16.
 */
public class ChartsUtil {

    public static void setChartConfig(BarLineChartBase barLineChartBase, PR_Chart chartConfig) {
        if (barLineChartBase != null && chartConfig != null) {
            // user interaction
            if (!chartConfig.interactionEnabled)
                disableUserActions(barLineChartBase);
            if (chartConfig.getGridBackgroundColor() != null)
                barLineChartBase.setGridBackgroundColor(Color.parseColor(chartConfig.getGridBackgroundColor()));

            if (chartConfig.getOffsets() != null) {
                barLineChartBase.setExtraTopOffset(chartConfig.getOffsets().top);
                barLineChartBase.setExtraBottomOffset(chartConfig.getOffsets().bottom);
                barLineChartBase.setExtraLeftOffset(chartConfig.getOffsets().left);
                barLineChartBase.setExtraRightOffset(chartConfig.getOffsets().right);
            }

            // no Data
            barLineChartBase.setNoDataText(chartConfig.noDataMessage);

            if (barLineChartBase instanceof HorizontalBarChart)
                ((HorizontalBarChart)barLineChartBase).setDrawValueAboveBar(chartConfig.getDrawValueAboveBar());
            else if (barLineChartBase instanceof BarChart)
                ((BarChart)barLineChartBase).setDrawValueAboveBar(chartConfig.getDrawValueAboveBar());
            // Legend
            setLegend(barLineChartBase.getLegend(), chartConfig.getLegend());
            // hide default text
            barLineChartBase.setDescription("");
        }
    }

    public static void setChartConfig(PieChart pieChart, PR_Chart chartConfig) {
        if (pieChart != null && chartConfig != null) {
            // user interaction
            if (!chartConfig.interactionEnabled)
                disableUserActions(pieChart);

            if (chartConfig.getOffsets() != null) {
                pieChart.setExtraTopOffset(chartConfig.getOffsets().top);
                pieChart.setExtraBottomOffset(chartConfig.getOffsets().bottom);
                pieChart.setExtraLeftOffset(chartConfig.getOffsets().left);
                pieChart.setExtraRightOffset(chartConfig.getOffsets().right);
            }

            // no Data
            pieChart.setNoDataText(chartConfig.noDataMessage);
            // Legend
            setLegend(pieChart.getLegend(), chartConfig.getLegend());
            // hide default text
            pieChart.setDescription("");
            // Donuts or not donuts
            if (chartConfig.getHole() != null) {
                pieChart.setDrawHoleEnabled(chartConfig.getHole().enabled);
                pieChart.setHoleRadius(chartConfig.getHole().radiusPercent);
                if (chartConfig.getHole().getColor() != null)
                    pieChart.setHoleColor(Color.parseColor(chartConfig.getHole().getColor()));

            }
        }
    }

    public static void setLegend(Legend legend, PR_ChartLegend legendConfig) {
        if (legendConfig != null) {
            if (legendConfig.getLabel() != null) {
                legend.setEnabled(legendConfig.getLabel().enabled);
                if (legendConfig.getLabel().getColor() != null)
                    legend.setTextColor(Color.parseColor(legendConfig.getLabel().color));
                switch (legendConfig.getForm()) {
                    case "Circle":
                        legend.setForm(Legend.LegendForm.CIRCLE);
                        break;
                    case "Line":
                        legend.setForm(Legend.LegendForm.LINE);
                        break;
                    case "Square":
                        legend.setForm(Legend.LegendForm.SQUARE);
                        break;
                }
            }
        }
    }

    public static void setAxisConfig(AxisBase axis, PR_ChartAxis axisConfig) {
        if (axis != null && axisConfig != null) {
            // Set axis Line
            if (axisConfig.getAxisLine() != null) {
                axis.setDrawAxisLine(axisConfig.getAxisLine().enabled);
                if (axisConfig.getAxisLine().width != null)
                    axis.setAxisLineWidth(axisConfig.getAxisLine().width);
                axis.setDrawAxisLine(axisConfig.getAxisLine().enabled);
                if (axisConfig.getAxisLine().color != null)
                    axis.setAxisLineColor(Color.parseColor(axisConfig.getAxisLine().color));
            }
            // Set axis Grid
            if (axisConfig.getGridLine() != null) {
                axis.setDrawGridLines(axisConfig.getGridLine().enabled);
                if (axisConfig.getGridLine().width != null)
                    axis.setGridLineWidth(axisConfig.getGridLine().width);
                if (axisConfig.getGridLine().color != null)
                    axis.setGridColor(Color.parseColor(axisConfig.getGridLine().color));
            }
            // setLabel
            if (axisConfig.getLabel() != null) {
                axis.setDrawLabels(axisConfig.getLabel().enabled);
                if (axisConfig.getLabel().getColor() != null)
                    axis.setTextColor(Color.parseColor(axisConfig.getLabel().getColor()));
            }
        }
    }

    public static void setXAxis(XAxis axis, PR_ChartAxis axisConfig) {
        setAxisConfig(axis, axisConfig);
        if (axisConfig.getLabel() != null && axisConfig.getLabel().getPosition() != null) {
            switch (axisConfig.getLabel().getPosition()) {
                case "bothSided":
                    axis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                    break;
                case "bottom":
                    axis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    break;
                case "bottomInside":
                    axis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
                    break;
                case "top":
                    axis.setPosition(XAxis.XAxisPosition.TOP);
                    break;
                case "topInside":
                    axis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
                    break;
            }
        }

        if (axisConfig.getLabel() != null) {
            axis.setLabelsToSkip(axisConfig.getLabel().labelsToSkip);
            axis.setSpaceBetweenLabels(axisConfig.getLabel().getSpaceBetweenLabels());
        }
        // prevent label from being cut off
        axis.setAvoidFirstLastClipping(true);
    }

    public static void disableUserActions(BarLineChartBase barLineChartBase) {
        barLineChartBase.setTouchEnabled(false);
        barLineChartBase.setDragEnabled(false);
        barLineChartBase.setScaleEnabled(false);
        barLineChartBase.setScaleXEnabled(false);
        barLineChartBase.setScaleYEnabled(false);
        barLineChartBase.setPinchZoom(false);
        barLineChartBase.setDoubleTapToZoomEnabled(false);
        barLineChartBase.setHighlightPerDragEnabled(false);
        barLineChartBase.setHighlightPerTapEnabled(false);
        barLineChartBase.setDragDecelerationEnabled(false);
    }

    public static void disableUserActions(PieChart pieChart) {
        pieChart.setTouchEnabled(false);
        pieChart.setHighlightPerTapEnabled(false);
        pieChart.setDragDecelerationEnabled(false);
    }

}
