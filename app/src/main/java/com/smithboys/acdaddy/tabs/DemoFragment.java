package com.smithboys.acdaddy.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smithboys.acdaddy.R;

/*
This class provides a demo fragment for a view pager adapter.
The fragment shows a text box that displays the position of the tab.
This class should not be modified to create tabs.
New tabs should be created as new fragments and then called in the appropriate view pager adapter.
 */
public class DemoFragment extends Fragment {

    public static final String ARG_COUNT = "param1";
    private Integer counter;

    public DemoFragment(){
        // Required empty public constructor
    }

    public static DemoFragment newInstance(Integer counter){
        DemoFragment fragment = new DemoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            counter = getArguments().getInt(ARG_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.demo_fragment_layout, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        TextView textViewCounter = view.findViewById(R.id.tv_counter);
        textViewCounter.setText("Fragment No " + (counter+1));
    }


}
