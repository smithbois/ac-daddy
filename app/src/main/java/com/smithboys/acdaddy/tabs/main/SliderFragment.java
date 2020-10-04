package com.smithboys.acdaddy.tabs.main;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.shapes.Shape;
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

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.smithboys.acdaddy.R;
import com.smithboys.acdaddy.util.DesiredTempUtil;

public class SliderFragment extends Fragment {

    SeekBar seekBar;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.slider_tab_layout, container, false);

        seekBar = v.findViewById(R.id.seek_bar);
        textView = v.findViewById(R.id.temp_number);
        final Switch acSwitch = v.findViewById(R.id.toggle_ac);


        acSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Set on AND update temperature, DesiredTempUtil.desiredTemp
            } else {
                // Set off
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
                    // Send code to update hardware here. Get temp with DesiredTempUtil.desiredTemp
                    // only temperature
                }

                // Change colors here
                int[][] states = new int[][] {
                        new int[] {-android.R.attr.state_checked},
                        new int[] {android.R.attr.state_checked},
                };

                int[] thumbColors = new int[] {
                        getResources().getColor(R.color.gray),
                        DesiredTempUtil.getColor()
                };

                int[] trackColors = new int[] {
                        DesiredTempUtil.getColorFaded(),
                        getResources().getColor(R.color.gray)
                };

                Switch toggleAc = v.findViewById(R.id.toggle_ac);
                DrawableCompat.setTintList(DrawableCompat.wrap(toggleAc.getThumbDrawable()), new ColorStateList(states, thumbColors));
                DrawableCompat.setTintList(DrawableCompat.wrap(toggleAc.getTrackDrawable()), new ColorStateList(states, thumbColors));
                ((TextView)v.findViewById(R.id.current_number)).setTextColor(DesiredTempUtil.getColor());
                LayerDrawable d = (LayerDrawable) (ResourcesCompat.getDrawable(getResources(), R.drawable.slider_track, null));
                assert d != null;
                Drawable progressDrawable = d.getDrawable(1);
                DrawableCompat.setTintList(DrawableCompat.wrap(progressDrawable), new ColorStateList(states, trackColors));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return v;
    }
}
