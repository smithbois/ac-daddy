package com.smithboys.acdaddy.util.graphs;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.smithboys.acdaddy.data.GlobalDataSets;
import com.smithboys.acdaddy.data.Subsection;

import java.util.ArrayList;
import java.util.List;

public class MyLineLegendRenderer extends LineChartRenderer {

    MyLineLegendRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    // This method is same as its parent implementation. (Required so our version of generateFilledPath() is called.)
    @Override
    protected void drawLinearFill(Canvas c, ILineDataSet dataSet, Transformer trans, XBounds bounds) {

        final Path filled = mGenerateFilledPathBuffer;

        final int startingIndex = bounds.min;
        final int endingIndex = bounds.range + bounds.min;
        final int indexInterval = 128;

        int currentStartIndex;
        int currentEndIndex;
        int iterations = 0;

        // Doing this iteratively in order to avoid OutOfMemory errors that can happen on large bounds sets.
        do {
            currentStartIndex = startingIndex + (iterations * indexInterval);

            currentEndIndex = currentStartIndex + indexInterval;
            currentEndIndex = currentEndIndex > endingIndex ? endingIndex : currentEndIndex;

            if (currentStartIndex <= currentEndIndex) {
                generateFilledPath(dataSet, currentStartIndex, currentEndIndex, filled);

                trans.pathValueToPixel(filled);

                final Drawable drawable = dataSet.getFillDrawable();
                if (drawable != null) {
                    drawFilledPath(c, filled, drawable);
                }
                else {
                    drawFilledPath(c, filled, dataSet.getFillColor(), dataSet.getFillAlpha());
                }
            }

            iterations++;

        } while (currentStartIndex <= currentEndIndex);
    }

    // This method defines the perimeter of the area to be filled for straight-line (default) data sets.
    private void generateFilledPath(final ILineDataSet dataSet, final int startIndex, final int endIndex, final Path outputPath) {

        System.out.println("fill path start at: " + startIndex + ", end at: " + endIndex);
        System.out.println("output path: " + outputPath);

        final float phaseY = mAnimator.getPhaseY();

        //Call the custom method to retrieve the dataSet for other line

        LineDataSet otherDataSet;
        if(dataSet.equals((ILineDataSet)GlobalDataSets.globalDataSet1)){ otherDataSet = (LineDataSet) GlobalDataSets.getGlobalDataSet2().copy();
        } else if(dataSet.equals((ILineDataSet)GlobalDataSets.globalDataSet2)){ otherDataSet = (LineDataSet) GlobalDataSets.getGlobalDataSet1().copy();
        } else { System.out.println("data set not found"); throw new RuntimeException(); }

        System.out.println("Our dataSet: " + dataSet);
        System.out.println("Other dataSet:" + otherDataSet);

        List<Entry> thisSetsEntries = new ArrayList<>();
        for (int i = 0; i < dataSet.getEntryCount(); i++){ thisSetsEntries.add(dataSet.getEntryForIndex(i)); }

        //final List<Entry> boundaryEntries = DataUtil.getLowestLine(thisSetsEntries, otherDataSet.getValues());
        //final List<Entry> boundaryEntries = DataUtil.genTestLine();
        //final List<Entry> boundaryEntries = otherDataSet.getValues();

        List<Subsection> boundaryLineSections = DataUtil.getLowestLine(thisSetsEntries, otherDataSet.getValues());
        System.out.println("lower boundary line sections:");
        for(int i = 0; i < boundaryLineSections.size(); i++){ System.out.println(boundaryLineSections.get(i).getValues().toString()); }

        List<Subsection> dataSetSections = DataUtil.getSubdividedDataSet((LineDataSet) dataSet, boundaryLineSections);
        System.out.println("our line sections:");
        for(int i = 0; i < dataSetSections.size(); i++){ System.out.println(dataSetSections.get(i).getValues().toString()); }

        // for each subsection
        for(int i = 0; i < dataSetSections.size(); i++){
            Path filled = outputPath;
//            filled.reset();
            if(i == 0){
                Entry firstEntry = dataSetSections.get(0).getValues().get(0);
                filled.moveTo(firstEntry.getX(), boundaryLineSections.get(0).getValues().get(0).getY());
                filled.lineTo(firstEntry.getX(), firstEntry.getY() * phaseY);

                Entry currentEntry;
                for(int j = 1; j < dataSetSections.get(0).getValues().size(); j++){
                    currentEntry = dataSetSections.get(0).getValues().get(j);
                    filled.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY);
                }
                Entry currentBoundary;
                for(int j = dataSetSections.get(0).getValues().size() - 1; j > 0; j--){
                    currentBoundary = boundaryLineSections.get(0).getValues().get(j);
                    filled.lineTo(currentBoundary.getX(), currentBoundary.getY() * phaseY);
                }
                filled.close();
            } else {
                Entry firstEntry = dataSetSections.get(i-1).getValues().get(dataSetSections.get(i-1).getValues().size() - 1);
                filled.moveTo(firstEntry.getX(), firstEntry.getY());
                //filled.lineTo(firstEntry.getX(), firstEntry.getY() * phaseY);
                System.out.println("move to: " + firstEntry.getX() + ", " + firstEntry.getY());

                Entry currentEntry;
                for(int j = 0; j < dataSetSections.get(i).getValues().size(); j++){
                    currentEntry = dataSetSections.get(i).getValues().get(j);
                    filled.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY);
                    System.out.println("move to: " + currentEntry.getX() + ", " + currentEntry.getY());
                }
                Entry currentBoundary;
                for(int j = dataSetSections.get(i).getValues().size() - 1; j >= 0; j--){
                    currentBoundary = boundaryLineSections.get(i).getValues().get(j);
                    filled.lineTo(currentBoundary.getX(), currentBoundary.getY() * phaseY);
                    System.out.println("move to: " + currentBoundary.getX() + ", " + currentBoundary.getY());
                }
                filled.close();
            }
        }
    }
}