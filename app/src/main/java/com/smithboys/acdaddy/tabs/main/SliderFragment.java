package com.smithboys.acdaddy.tabs.main;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smithboys.acdaddy.MainActivity;
import com.smithboys.acdaddy.R;
import com.smithboys.acdaddy.util.DesiredTempUtil;

public class SliderFragment extends Fragment {

    SeekBar seekBar;
    TextView textView;
    Switch acSwitch;
    TextView currentNumber;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.slider_tab_layout, container, false);


        seekBar = v.findViewById(R.id.seek_bar);
        textView = v.findViewById(R.id.temp_number);
        acSwitch = v.findViewById(R.id.toggle_ac);
        context = this.getContext();
        currentNumber = v.findViewById(R.id.current_number);

        //seekBar.setColo

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("config");

        myRef.child("desiredTemp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DesiredTempUtil.desiredTemp = snapshot.getValue(int.class);
                seekBar.setProgress(DesiredTempUtil.desiredTemp * 1000 - 60000);
                updateTextAndSlider();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        acSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            System.out.println("Is switch checked?" + isChecked);
            if(isChecked){
                myRef.child("onOrOff").setValue("1");
                myRef.child("desiredTemp").setValue(DesiredTempUtil.desiredTemp);
            } else {
                myRef.child("onOrOff").setValue("0");
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DesiredTempUtil.desiredTemp = (progress + 60000) / 1000;
                textView.setText(Integer.toString(DesiredTempUtil.desiredTemp));

                if (progress < 500 || progress > 19500) {
                    seekBar.setThumb(ContextCompat.getDrawable(context, R.drawable.slider_empty_thumb));
                    //seekBar.setThumb(getResources().getDrawable(R.drawable.slider_empty_thumb));
                } else {
                    //seekBar.setThumb(getResources().getDrawable(R.drawable.slider_thumb));
                    seekBar.setThumb(ContextCompat.getDrawable(context, R.drawable.slider_thumb));
                }

                if (acSwitch.isChecked()) {
                    myRef.child("desiredTemp").setValue(DesiredTempUtil.desiredTemp);
                }

                updateTextAndSlider();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return v;
    }
    private void updateTextAndSlider(){
        textView.setText(Integer.toString(DesiredTempUtil.desiredTemp));
        // Change colors here
        MainActivity.toolbar.setBackgroundColor(DesiredTempUtil.getColor());
        MainActivity.toolbar.getNavigationIcon().setColorFilter(DesiredTempUtil.getColorFaded(), PorterDuff.Mode.SRC_IN);
        currentNumber.setTextColor(DesiredTempUtil.getColor());
        MainActivity.tabLayout.setSelectedTabIndicatorColor(DesiredTempUtil.getColor());
        int[][] states = new int[][] {
                new int[] {-android.R.attr.state_checked},
                new int[] {android.R.attr.state_checked},
        };

        int[] trackColors = new int[] {
                //getResources().getColor(R.color.gray),
                DesiredTempUtil.getColor(),
                ContextCompat.getColor(context, R.color.gray),
        };

        int[] fadedColors = new int[] {
                DesiredTempUtil.getColorFaded(),
                //getResources().getColor(R.color.gray)
                ContextCompat.getColor(context, R.color.gray)
        };

        int[] switchColors1 = new int[] {
                ContextCompat.getColor(context, R.color.gray),
                DesiredTempUtil.getColor()
                //getResources().getColor(R.color.gray)
        };

        DrawableCompat.setTintList(DrawableCompat.wrap(acSwitch.getThumbDrawable()), new ColorStateList(states, switchColors1));
        DrawableCompat.setTintList(DrawableCompat.wrap(acSwitch.getTrackDrawable()), new ColorStateList(states, switchColors1));
        LayerDrawable d = (LayerDrawable) (ResourcesCompat.getDrawable(getResources(), R.drawable.slider_track, null));
        assert d != null;
        Drawable progressDrawable = d.getDrawable(1);
        DrawableCompat.setTintList(DrawableCompat.wrap(progressDrawable), new ColorStateList(states, trackColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(d.getDrawable(0)), new ColorStateList(states, fadedColors));
    }
}
