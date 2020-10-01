package com.smithboys.acdaddy.util.graphs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.smithboys.acdaddy.R;
import com.smithboys.acdaddy.data.GlobalDataSets;

import java.util.ArrayList;

public class DataUtil {
    public static void displayLineChart(LineChart mChart, ArrayList<Entry> values, ArrayList<Entry> values2, Context context){
        LineDataSet set1, set2;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
            set1.setValues(values);
            set2.setValues(values2);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = formatDataSet(context, values, 1);
            set2 = formatDataSet(context, values2, 2);

            GlobalDataSets.setGlobalDataSet1(set1);
            GlobalDataSets.setGlobalDataSet2(set2);

            set1.setFillFormatter(new MyFillFormatter(set2));
            mChart.setRenderer(new MyLineLegendRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler()));

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);

            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }
    }

    private static LineDataSet formatDataSet(Context context, ArrayList<Entry> values, int setNumber){
        LineDataSet set = new LineDataSet(values, "Sample Data");
        set.setDrawIcons(false);
        //set.enableDashedLine(10f, 5f, 0f);
        //set.enableDashedHighlightLine(10f, 5f, 0f);
        set.setColor(ContextCompat.getColor(context, R.color.white));
        set.setCircleColor(ContextCompat.getColor(context, R.color.white));
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(true);
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);
        set.setFillColor(R.color.blue);

//        if (Utils.getSDKInt() >= 18) {
//            if (setNumber == 1){
//                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_blue);
//                set.setFillDrawable(drawable);
//            } else {
//                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_red);
//                set.setFillDrawable(drawable);
//            }
//        } else {
//            set.setFillColor(R.color.white);
//        }
        return set;

    }
}
