package com.smithboys.acdaddy.tabs.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.smithboys.acdaddy.R;

public class SliderFragment extends Fragment {

    SeekBar seekBar;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.slider_tab_layout, container, false);

        seekBar = v.findViewById(R.id.seek_bar);
        textView = v.findViewById(R.id.temp_number);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int degrees = (progress + 60000) / 1000;
                textView.setText(degrees + "°");

                if (progress < 500 || progress > 19500) {
                    seekBar.setThumb(getResources().getDrawable(R.drawable.slider_empty_thumb));
                } else {
                    seekBar.setThumb(getResources().getDrawable(R.drawable.slider_thumb));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return v;
    }

    public static SliderFragment newInstance(){
        SliderFragment f = new SliderFragment();
        return f;
    }


}
