package com.smithboys.acdaddy.tabs.main;

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
import com.smithboys.acdaddy.util.graphs.DataUtil;

import java.util.ArrayList;

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

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("Message");
        myRef.setValue("hello");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                dataText.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dataText.setText("Failed to read value");
            }
        });

        lineChart = v.findViewById(R.id.chart);
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);

        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(1, 50));
        values.add(new Entry(2, 100));

        ArrayList<Entry> values2 = new ArrayList<>();
        values2.add(new Entry(1, 75));
        values2.add(new Entry(2, 75));

        DataUtil.displayLineChart(lineChart, values, values2, this.getContext());



        return v;
    }

    public static DataFragment newInstance() {
        DataFragment f = new DataFragment();
        return f;
    }
}

