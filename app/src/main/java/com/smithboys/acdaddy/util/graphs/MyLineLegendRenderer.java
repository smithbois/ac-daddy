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

        final float phaseY = mAnimator.getPhaseY();
        final Path filled = outputPath; // Not sure if this is required, but this is done in the original code so preserving the same technique here.
        filled.reset();

        //Call the custom method to retrieve the dataSet for other line

        LineDataSet otherDataSet;
        if(dataSet.equals((ILineDataSet)GlobalDataSets.globalDataSet1)){
            // dataSet is dataSet1, other is dataSet2
            otherDataSet = GlobalDataSets.getGlobalDataSet2();
        } else if(dataSet.equals((ILineDataSet)GlobalDataSets.globalDataSet2)){
            // dataSet is dataSet2, other is dataSet1
            otherDataSet = GlobalDataSets.getGlobalDataSet1();
        } else {
            System.out.println("data set not found");
            throw new RuntimeException();
        }
        System.out.println("Our dataSet: " + dataSet);
        System.out.println("Other dataSet:" + otherDataSet);

        List<Entry> thisSetsEntries = new ArrayList<>();
        for (int i = 0; i < dataSet.getEntryCount(); i++){ thisSetsEntries.add(dataSet.getEntryForIndex(i)); }

        //final List<Entry> boundaryEntries = DataUtil.getLowestLine(thisSetsEntries, otherDataSet.getValues());
        //final List<Entry> boundaryEntries = DataUtil.genTestLine();
        //final List<Entry> boundaryEntries = otherDataSet.getValues();

        List<Subsection> boundaryLineSections = DataUtil.getLowestLine(thisSetsEntries, otherDataSet.getValues());
        System.out.println("reference line sections:");
        for(int i = 0; i < boundaryLineSections.size(); i++){
            System.out.println(boundaryLineSections.get(i).getValues().toString());
        }
        List<Subsection> dataSetSections = DataUtil.getSubdividedDataSet((LineDataSet) dataSet, boundaryLineSections);

        int startSubSectionIndex = 0;
        int startEntryIndex = 0;
        int indexesProcessed = 0;
        for(int i = 0; i < boundaryLineSections.size(); i++){
            for (int j = 0; j < boundaryLineSections.get(i).getValues().size(); j++){
                if(indexesProcessed == startIndex){
                    startSubSectionIndex = i;
                    startEntryIndex = j;
                    break;
                } else { indexesProcessed++; } } }

        int endSubSectionIndex = boundaryLineSections.size() - 1;
        int endEntryIndex = boundaryLineSections.get(boundaryLineSections.size() - 1).getValues().size() - 1;
        indexesProcessed = 0;
        for(int i = 0; i < boundaryLineSections.size(); i++){
            for (int j = 0; j < boundaryLineSections.get(i).getValues().size(); j++){
                if(indexesProcessed == endIndex){
                    endSubSectionIndex = i;
                    endEntryIndex = j;
                    break;
                } else { indexesProcessed++; } } }

        for(int i = 0; i < boundaryLineSections.size(); i++){
            if (i < startSubSectionIndex){ continue; }
            if (i > endSubSectionIndex){ break; }

            List<Entry> boundaryEntries = boundaryLineSections.get(i).getValues();
            List<Entry> dataSetEntries = dataSetSections.get(i).getValues();

            Entry firstDataSetEntry;
            Entry firstBoundaryEntry;

            if(i == startSubSectionIndex){
                firstDataSetEntry = dataSetEntries.get(startEntryIndex);
                firstBoundaryEntry = boundaryEntries.get(startEntryIndex);
            } else if (i == endSubSectionIndex){
                firstDataSetEntry = dataSetEntries.get(endEntryIndex);
                firstBoundaryEntry = boundaryEntries.get(endEntryIndex);
            } else {
                firstDataSetEntry = dataSetEntries.get(0);
                firstBoundaryEntry = boundaryEntries.get(0);
            }

            // Move down to boundary of first entry
            filled.reset();
            filled.moveTo(firstDataSetEntry.getX(), firstBoundaryEntry.getY() * phaseY);

            // Draw line up to value of first entry
            filled.lineTo(firstDataSetEntry.getX(), firstDataSetEntry.getY() * phaseY);

            // Draw line across to the values of the next entries
            Entry currentEntry;
            if(i == startSubSectionIndex){
                for (int x = startEntryIndex + 1; x <= (dataSetEntries.size()-1); x++) {
                    currentEntry = boundaryLineSections.get(i).getValues().get(x);
                    filled.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY);
                }
            } else if (i == endSubSectionIndex){
                for (int x = 0 + 1; x <= endEntryIndex; x++) {
                    currentEntry = boundaryLineSections.get(i).getValues().get(x);
                    filled.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY);
                }
            } else {
                for (int x = 0 + 1; x <= (dataSetEntries.size()-1); x++) {
                    currentEntry = boundaryLineSections.get(i).getValues().get(x);
                    filled.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY);
                }
            }

            // Draw down to the boundary value of the last entry, then back to the first boundary value
            Entry boundaryEntry1;
            if(i == startSubSectionIndex){
                for (int x = (dataSetEntries.size()-1) + 1; x > startEntryIndex; x--) {
                    boundaryEntry1 = boundaryEntries.get(x);
                    filled.lineTo(boundaryEntry1.getX(), boundaryEntry1.getY() * phaseY);
                }
            } else if (i == endSubSectionIndex){
                for (int x = endEntryIndex; x > 0; x--) {
                    boundaryEntry1 = boundaryEntries.get(x);
                    filled.lineTo(boundaryEntry1.getX(), boundaryEntry1.getY() * phaseY);
                }
            } else {
                for (int x = (dataSetEntries.size()-1); x > 0; x--) {
                    boundaryEntry1 = boundaryEntries.get(x);
                    filled.lineTo(boundaryEntry1.getX(), boundaryEntry1.getY() * phaseY);
                }
            }

            // Join up the perimeter
            filled.close();
        }
    }

}