package com.smithboys.acdaddy.data;

import com.github.mikephil.charting.data.LineDataSet;

public class GlobalDataSets {
    public static LineDataSet globalDataSet1;
    public static LineDataSet globalDataSet2;

    public GlobalDataSets(LineDataSet ds1, LineDataSet ds2){
        globalDataSet1 = ds1;
        globalDataSet2 = ds2;
    }

    public static LineDataSet getGlobalDataSet1(){
        return globalDataSet1;
    }

    public static LineDataSet getGlobalDataSet2(){
        return globalDataSet2;
    }

    public static void setGlobalDataSet1(LineDataSet ds){
        globalDataSet1 = ds;
    }

    public static void setGlobalDataSet2(LineDataSet ds){
        globalDataSet2 = ds;
    }
}