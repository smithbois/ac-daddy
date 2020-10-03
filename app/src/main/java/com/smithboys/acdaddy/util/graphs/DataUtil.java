package com.smithboys.acdaddy.util.graphs;

import android.content.Context;
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
import com.smithboys.acdaddy.data.LineSegment;
import com.smithboys.acdaddy.data.Subsection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        if (Utils.getSDKInt() >= 18) {
            if (setNumber == 1){
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_red);
                set.setFillDrawable(drawable);
            } else {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_blue);
                set.setFillDrawable(drawable);
            }
        } else {
            set.setFillColor(R.color.white);
        }
        return set;

    }

    public static List<Subsection> getLowestLine(List<Entry> line1, List<Entry> line2){

        // create groups of line segments from points
        ArrayList<LineSegment> lineSegments1 = new ArrayList<>();
        ArrayList<LineSegment> lineSegments2 = new ArrayList<>();

        for(int i = 0; i < (line1.size() - 1); i++){ lineSegments1.add(new LineSegment(line1.get(i), line1.get(i+1))); System.out.println("ls1: " + new LineSegment(line1.get(i), line1.get(i+1)));}
        for(int i = 0; i < (line2.size() - 1); i++) { lineSegments2.add(new LineSegment(line2.get(i), line2.get(i+1))); System.out.println("ls2: " + new LineSegment(line2.get(i), line2.get(i+1)));}

        // create list of intersection points
        ArrayList<Entry> intersectionPoints = new ArrayList<>();
        for(int i = 0; i < lineSegments1.size(); i++){ for(int j = 0; j < lineSegments2.size(); j++){
                Entry intersectionPoint = lineSegments1.get(i).findIntersection(lineSegments2.get(j));
                if (intersectionPoint != null && !intersectionPoints.contains(intersectionPoint)){
                    intersectionPoints.add(intersectionPoint);
                    break;
                }
            } }


        for (int i = 0; i < intersectionPoints.size(); i++){
            for (int j = i + 1; j < intersectionPoints.size(); j++) {
                if (intersectionPoints.get(i).equalTo(intersectionPoints.get(j))) {
                    intersectionPoints.remove(j);
                    j--;
                } } }

        System.out.println("intersection points: " + intersectionPoints);

        //System.out.println("intersection points: " + intersectionPoints);



        // create new groups of line segments that incorporate intersection points
        lineSegments1.clear();
        lineSegments2.clear();


        for(int i = 0; i < intersectionPoints.size(); i++){
            line1.add(intersectionPoints.get(i));
            line2.add(intersectionPoints.get(i));
        }

        for (int i = 0; i < line1.size(); i++){
            for (int j = i + 1; j < line1.size(); j++) {
                if (line1.get(i).equalTo(line1.get(j))) {
                    line1.remove(j);
                    j--;
                } } }
        for (int i = 0; i < line2.size(); i++){
            for (int j = i + 1; j < line2.size(); j++) {
                if (line2.get(i).equalTo(line2.get(j))) {
                    line2.remove(j);
                    j--;
                } } }
        Collections.sort(line1, new Comparator<Entry>() {
            @Override
            public int compare(Entry entry, Entry t1) {
                if(entry.getX() == t1.getX()){
                    return 0;
                } else {
                    return entry.getX() < t1.getX() ? -1 : 1;
                }
            }
        });
        Collections.sort(line2, new Comparator<Entry>() {
            @Override
            public int compare(Entry entry, Entry t1) {
                if(entry.getX() == t1.getX()){
                    return 0;
                } else {
                    return entry.getX() < t1.getX() ? -1 : 1;
                }
            }
        });

        //System.out.println("Line 1 w/ intersection points:" + line1);
        //System.out.println("Line 2 w/ intersection points:" + line2);

        for(int i = 0; i < (line1.size() - 1); i++){ lineSegments1.add(new LineSegment(line1.get(i), line1.get(i+1))); }
        for(int i = 0; i < (line2.size() - 1); i++) { lineSegments2.add(new LineSegment(line2.get(i), line2.get(i+1))); }

        Collections.sort(lineSegments1);
        Collections.sort(lineSegments2);

        //System.out.println("new segments 1: " + lineSegments1 + ", new segments2: " + lineSegments2);

        ArrayList<Float> checkpointXVals = new ArrayList<>();

        if(line1.get(0).getX() == line2.get(0).getX()){
            checkpointXVals.add(line1.get(0).getX()); } else {
            checkpointXVals.add(line1.get(0).getX());
            checkpointXVals.add(line2.get(0).getX()); }

        for(int i = 0; i < intersectionPoints.size(); i++){ checkpointXVals.add(intersectionPoints.get(i).getX()); }
        if(lineSegments1.get(0).getStartX() != lineSegments2.get(0).getStartX()){
            checkpointXVals.add(lineSegments1.get(0).getStartX());
            checkpointXVals.add(lineSegments2.get(0).getStartX());
        } else { checkpointXVals.add(lineSegments1.get(0).getStartX()); }

        if(lineSegments1.get(lineSegments1.size()-1).getEndX() != lineSegments2.get(lineSegments2.size()-1).getEndX()){
            checkpointXVals.add(lineSegments1.get(lineSegments1.size()-1).getEndX());
            checkpointXVals.add(lineSegments2.get(lineSegments2.size()-1).getEndX());
        } else { checkpointXVals.add(lineSegments1.get(lineSegments1.size()-1).getEndX()); }

        Set<Float> checkpointsSet = new HashSet<>(checkpointXVals);
        checkpointXVals.clear();
        checkpointXVals.addAll(checkpointsSet);
        Collections.sort(checkpointXVals);
        //System.out.println("Checkpoints (x value): " + checkpointXVals);

        // determine which line to use for each sub-section based on which one is lesser
        List<Subsection> subsections = new ArrayList<>();
        for(int i = 0; i < (checkpointXVals.size()-1); i++){
            Float checkpointStartX = checkpointXVals.get(i);
            Float checkpointEndX = checkpointXVals.get(i+1);
            Float y1 = null;
            Float y2 = null;
            for (LineSegment ls : lineSegments1){
                if (((Float)ls.getStartX()).equals(checkpointStartX)){
                    y1 = ls.getAverageY();
                }
            }
            for (LineSegment ls : lineSegments2){
                if (((Float)ls.getStartX()).equals(checkpointStartX)){
                    y2 = ls.getAverageY();
                }
            }

            if(y2 == null || y1 < y2){
                subsections.add(new Subsection(checkpointStartX, checkpointEndX, 1));
            } else {
                subsections.add(new Subsection(checkpointStartX, checkpointEndX, 2));
            }
        }

        //System.out.println("Subsections: " + subsections);
        //System.out.println("Line 1 w/ intersection points:" + line1);
        //System.out.println("Line 2 w/ intersection points:" + line2);

        // assemble the new line based on what line to use for each subsection
        // for each subsection

        float offset = (float) 0;
        for(int i = 0; i < subsections.size(); i++){
            boolean firstPlaced = false;
            // if the lower line is line 1
            if(subsections.get(i).getLine() == 1){
                List<Entry> sectionValues = new ArrayList<>();
                for (int j = 0; j < line1.size(); j++){
                    if(i == 0 && !firstPlaced){
                        //System.out.println("adding initial entry: " + line1.get(j) + ", subsection: " + i + ", startx: " + subsections.get(i).getStartX() + ", endx: " + subsections.get(i).getEndX());
                        Entry point = line1.get(j);
                        point.setY(point.getY() + offset);
                        sectionValues.add(point);
                        firstPlaced = true;
                    }
                    if(line1.get(j).getX() <= subsections.get(i).getEndX() && line1.get(j).getX() > subsections.get(i).getStartX()){
                        //System.out.println("adding entry: " + line1.get(j) + ", subsection: " + i + ", startx: " + subsections.get(i).getStartX() + ", endx: " + subsections.get(i).getEndX());
                        Entry point = line1.get(j);
                        point.setY(point.getY() + offset);
                        sectionValues.add(point);
                    }
                    if (line1.get(j).getX() == subsections.get(i).getEndX()){
                        break;
                    } }
                subsections.get(i).setValues(sectionValues);
            }
            if(subsections.get(i).getLine() == 2){
                List<Entry> sectionValues = new ArrayList<>();
                for (int j = 0; j < line2.size(); j++){
                    if(i == 0 && !firstPlaced){
                        //System.out.println("adding initial entry: " + line2.get(j) + ", subsection: " + i + ", startX: " + subsections.get(i).getStartX() + ", endX: " + subsections.get(i).getEndX());
                        Entry point = line2.get(j);
                        point.setY((float) (point.getY() + offset));
                        sectionValues.add(point);
                        firstPlaced = true;
                    }
                    if(line2.get(j).getX() <= subsections.get(i).getEndX() && line2.get(j).getX() > subsections.get(i).getStartX()){
                        //System.out.println("adding entry: " + line2.get(j) + ", subsection: " + i + ", startX: " + subsections.get(i).getStartX() + ", endX: " + subsections.get(i).getEndX());
                        Entry point = line2.get(j);
                        point.setY((float) (point.getY() + offset));
                        sectionValues.add(point);
                    }
                    if (line2.get(j).getX() == subsections.get(i).getEndX()){
                        break;
                    } }
                subsections.get(i).setValues(sectionValues);
            }
        }
        //System.out.println("Lowest Line: ");
        return subsections;

    }

    public static List<Entry> genTestLine(){
        List<Entry> dataList = new ArrayList<>();
        dataList.add(new Entry(1, 49));
        dataList.add(new Entry((float) 1.5, 74));
        dataList.add(new Entry(2, 74));
        dataList.add(new Entry((float) 2.5, 74));
        dataList.add(new Entry(3, 74));
        dataList.add(new Entry(4, 74));
        dataList.add(new Entry(5, 74));

        return dataList;
    }

    public static List<Subsection> getSubdividedDataSet(LineDataSet dataSet, List<Subsection> referenceLine){

        List<Entry> dataSetValues = dataSet.getValues();
        //System.out.println("DataSet values: " + dataSetValues);
        List<Float> referenceXValues = new ArrayList<>();

        for(Subsection ss : referenceLine){
            List<Entry> subsectionValues = ss.getValues();
            //System.out.println("subsection values: " + subsectionValues);
            for (Entry e : ss.getValues()){
                referenceXValues.add(e.getX());
            }
        }

        //System.out.println("reference x values: " + referenceXValues);
        int[] isXValArray = new int[referenceXValues.size()];
        for(int i = 0; i<isXValArray.length; i++){
            isXValArray[i] = 0;
        }
        float refXVal;
        for(int i = 0; i < referenceXValues.size(); i++){
            refXVal = referenceXValues.get(i);
            for (int j = 0; j<dataSetValues.size(); j++){
                if(dataSetValues.get(j).getX() == (float) referenceXValues.get(i)){
                    isXValArray[i] = 1;
                    break;
                }
            }
        }

        // if the x value is in the original data set, leave it. If not, interpolate
        List<Entry> completeDataValues = new ArrayList<>();
        for(int i = 0; i < referenceXValues.size(); i++){
            if(isXValArray[i] == 1){
                completeDataValues.add(dataSetValues.get(getEntryIndexByX(dataSetValues, referenceXValues.get(i))));
            } else {
                LineSegment ls = new LineSegment(dataSetValues.get(getEntryIndexByX(dataSetValues, referenceXValues.get(getLastRefXInValuesIndex(i, isXValArray)))),
                        dataSetValues.get(getEntryIndexByX(dataSetValues, referenceXValues.get(getNextRefXInValuesIndex(i, isXValArray)))));
                //System.out.println("Line Segment: " + ls);
                float refX = referenceXValues.get(i);
                float pointY = ls.getSlope() * refX + ls.getIntercept();
                completeDataValues.add(new Entry(refX, pointY));
            }
        }
        // organize data values into subsections
        List<Subsection> subsections = new ArrayList<>();
        int totalEntriesAdded = 0;
        //System.out.println("CompleteDataValues: " + completeDataValues);
        //System.out.println("Num Sub Sections: " + referenceLine.size());
        for (int i = 0; i < referenceLine.size(); i++){
            int count = referenceLine.get(i).getValues().size();
            int limit = count + totalEntriesAdded;
            List<Entry> sectionEntries = new ArrayList<>();
            for (int j = totalEntriesAdded; j < limit; j++){
                sectionEntries.add(completeDataValues.get(j));
                totalEntriesAdded++;
            }
            //System.out.println("subsection entries:" + sectionEntries);
            subsections.add(new Subsection(sectionEntries, referenceLine.get(i).getLine()));
        }
        return subsections;
    }

    public static int getEntryIndexByX(List<Entry> entries, float targetX){
        int returnIndex = -1;
        for(int i = 0; i < entries.size(); i++){
            if(entries.get(i).getX() == targetX){
                returnIndex = i;
                break;
            }
        }
        return returnIndex;
    }
    public static int getNextRefXInValuesIndex(int refXIndex, int[] isXValArray){
        for(int i = refXIndex; i < isXValArray.length; i++){
            if(isXValArray[i] == 1){
                //System.out.println("nextIndex: " + i);
                return i;
            }
        } return -1;
    }

    public static int getLastRefXInValuesIndex(int refXIndex, int[] isXValArray){
        for(int i = refXIndex; i >= 0; i--){
            if(isXValArray[i] == 1){
                //System.out.println("lastIndex: " + i);
                return i;
            }
        } return -1;
    }

}
