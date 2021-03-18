package com.smithboys.acdaddy.tabs.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smithboys.acdaddy.R;
import com.smithboys.acdaddy.util.DataHandler;
import com.smithboys.acdaddy.util.graphs.DataUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataFragment extends Fragment {

    /*
    This class represents the activity for the data tab.
    Use it to load the layout for the tab and run any code.
     */
    LineChart lineChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_tab_layout, container, false);
        final TextView dataText = v.findViewById(R.id.data_text);

        final Context context = this.getContext();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("timedata");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap actualTempMap = DataHandler.getTimeActualMap((HashMap) dataSnapshot.getValue());
                HashMap desiredTempMap = DataHandler.getTimeDesiredMap((HashMap) dataSnapshot.getValue());
                ArrayList<Entry> values = new ArrayList<>();
                for (Iterator<Map.Entry<Date, Float>> entries = actualTempMap.entrySet().iterator(); entries.hasNext(); ) {
                    Map.Entry<Date, Float> entry = entries.next();
                    Entry value = new Entry(entry.getKey().getTime(), entry.getValue());
                    int index = DataUtil.getEntryIndexByX(values, value.getX());
                    //System.out.println("adding point at index: " + index);
                    if(index == -1){
                        values.add(value);
                    }
                }
                System.out.println("values: " + values);
                ArrayList<Entry> values2 = new ArrayList<>();
                for (Iterator<Map.Entry<Date, Float>> entries = desiredTempMap.entrySet().iterator(); entries.hasNext(); ) {
                    Map.Entry<Date, Float> entry = entries.next();
                    Entry value2 = new Entry(entry.getKey().getTime(), entry.getValue());
                    int index = DataUtil.getEntryIndexByX(values2, value2.getX());
                    System.out.println("adding point " + value2 + " at: " + index);
                    if(index == -1){
                        values2.add(value2);
                    }
                }
                System.out.println("values2: " + values2);
                //DataUtil.resetChart(lineChart);
                System.out.println("chart values: " + values);
                DataUtil.displayLineChart(lineChart, values, values2, context);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read data");
            }
        });
        //dataText.setText(myRef.);
        //myRef;

        lineChart = v.findViewById(R.id.chart);
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);

//        ArrayList<Entry> values = new ArrayList<>();
//        values.add(new Entry(1, 1));
//        values.add(new Entry(2, 2));
//        values.add(new Entry(3, 3));
//        values.add(new Entry(4, 4));
//        values.add(new Entry(5, 5));
//
//        ArrayList<Entry> values2 = new ArrayList<>();
//        values2.add(new Entry(1, 3));
//        values2.add(new Entry(2, 3));
//        values2.add(new Entry(3, 3));
//        values2.add(new Entry(4, 3));
//        values2.add(new Entry(5, 3));
//
//        DataUtil.displayLineChart(lineChart, values, values2, this.getContext());



        return v;
    }

    public static DataFragment newInstance() {
        DataFragment f = new DataFragment();
        return f;
    }
}

