package com.smithboys.acdaddy.tabs.main;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.smithboys.acdaddy.R;
import com.smithboys.acdaddy.util.DesiredTempUtil;

public class SliderFragment extends Fragment {

    SeekBar seekBar;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.slider_tab_layout, container, false);

        seekBar = v.findViewById(R.id.seek_bar);
        textView = v.findViewById(R.id.temp_number);
        final Switch acSwitch = v.findViewById(R.id.toggle_ac);


        acSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Code to set AC to the current slider value
                } else {
                    // Code to turn AC off until turned back on through switch or schedule
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DesiredTempUtil.desiredTemp = (progress + 60000) / 1000;
                textView.setText(Integer.toString(DesiredTempUtil.desiredTemp));

                if (progress < 500 || progress > 19500) {
                    seekBar.setThumb(getResources().getDrawable(R.drawable.slider_empty_thumb));
                } else {
                    seekBar.setThumb(getResources().getDrawable(R.drawable.slider_thumb));
                }

                if (acSwitch.isChecked()) {
                    // Send code to update hardware here, using var degrees
                }

                // Change colors here
                int c = DesiredTempUtil.getColor();
                int[][] states = new int[][] {
                        new int[] {-android.R.attr.state_checked},
                        new int[] {android.R.attr.state_checked},
                };

                int[] thumbColors = new int[] {
                        getResources().getColor(R.color.gray),
                        c
                };

                int[] trackColors = new int[] {
                        getResources().getColor(R.color.gray),
                        DesiredTempUtil.getColorFaded()
                };

                Switch toggleAc = v.findViewById(R.id.toggle_ac);
                DrawableCompat.setTintList(DrawableCompat.wrap(toggleAc.getThumbDrawable()), new ColorStateList(states, thumbColors));
                DrawableCompat.setTintList(DrawableCompat.wrap(toggleAc.getTrackDrawable()), new ColorStateList(states, thumbColors));
                ((TextView)v.findViewById(R.id.current_number)).setTextColor(c);
                // TODO Drawable d = getResources().getDrawable(R.drawable.slider_track);

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
