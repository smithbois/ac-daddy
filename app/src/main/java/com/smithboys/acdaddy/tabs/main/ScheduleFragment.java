package com.smithboys.acdaddy.tabs.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.smithboys.acdaddy.R;

public class ScheduleFragment extends Fragment {

    /*
    This class represents the activity for the schedule tab.
    Use it to load the layout for the tab and run any code.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_tab_layout, container, false);
        return v;
    }

    public static ScheduleFragment newInstance(String text) {
        ScheduleFragment f = new ScheduleFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
}