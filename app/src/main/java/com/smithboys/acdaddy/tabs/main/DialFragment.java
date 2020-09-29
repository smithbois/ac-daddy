package com.smithboys.acdaddy.tabs.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.smithboys.acdaddy.R;

public class DialFragment extends Fragment {

    public TextView textView;
    /*
        This class represents the activity for the dial tab.
        Use it to load the layout for the tab and run any code.
         */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dial_tab_layout, container, false);
        textView = v.findViewById(R.id.dial_text);
        return v;
    }

    public static DialFragment newInstance(){
        DialFragment f = new DialFragment();
        return f;
    }


}
