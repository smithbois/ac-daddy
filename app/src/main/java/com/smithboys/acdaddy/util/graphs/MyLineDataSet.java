package com.smithboys.acdaddy.util.graphs;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.List;

public class MyLineDataSet extends LineDataSet {

    public MyLineDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
    }
}
