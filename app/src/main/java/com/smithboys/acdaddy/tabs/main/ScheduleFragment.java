package com.smithboys.acdaddy.tabs.main;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.smithboys.acdaddy.R;
import com.smithboys.acdaddy.util.OnOffAtTime;
import com.smithboys.acdaddy.util.TalkToDevice;

import java.util.Calendar;

public class ScheduleFragment extends Fragment {

    // Variables
    TextView timerText1, timerText2;
    int hour1 = 12, minute1 = 0, hour2 = 12, minute2 = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_tab_layout, container, false);
        Switch onSwitch = v.findViewById(R.id.toggle_schedule_on);
        Switch offSwitch = v.findViewById(R.id.toggle_schedule_off);

        timerText1 = v.findViewById(R.id.time_on);
        timerText1.setOnClickListener(v12 -> {
            TimePickerDialog timePicker = new TimePickerDialog(
                    getContext(),
                    (view, hourOfDay, minute) -> {
                        hour1 = hourOfDay;
                        minute1 = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, hour1, minute1);
                        timerText1.setText(DateFormat.format("hh:mm aa", calendar));

                        if (onSwitch.isChecked()) {
                            OnOffAtTime.resetSchedule();
                            OnOffAtTime.setSchedule(hour1, minute1, true);
                            if (offSwitch.isChecked()) {
                                offSwitch.setChecked(false);
                                offSwitch.setChecked(true);
                            }
                        }
                    }, 12, 0, false
            );
            timePicker.updateTime(hour1, minute1);
            timePicker.show();
        });

        timerText2 = v.findViewById(R.id.time_off);
        timerText2.setOnClickListener(v1 -> {
            TimePickerDialog timePicker = new TimePickerDialog(
                    getContext(),
                    (view, hourOfDay, minute) -> {
                        hour2 = hourOfDay;
                        minute2 = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, hour2, minute2);
                        timerText2.setText(DateFormat.format("hh:mm aa", calendar));

                        if (offSwitch.isChecked()) {
                            OnOffAtTime.resetSchedule();
                            OnOffAtTime.setSchedule(hour2, minute2, true);
                            if (onSwitch.isChecked()) {
                                onSwitch.setChecked(false);
                                onSwitch.setChecked(true);
                            }
                        }
                    }, 12, 0, false
            );
            timePicker.updateTime(hour2, minute2);
            timePicker.show();
        });

        onSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                OnOffAtTime.setSchedule(hour1, minute1, true);
            } else {
                OnOffAtTime.resetSchedule();
                if (offSwitch.isChecked()) {
                    offSwitch.setChecked(false);
                    offSwitch.setChecked(true);
                }
            }
        });
        offSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                OnOffAtTime.setSchedule(hour2, minute2, false);
            } else {
                OnOffAtTime.resetSchedule();
                if (onSwitch.isChecked()) {
                    onSwitch.setChecked(false);
                    onSwitch.setChecked(true);
                }
            }
        });

        return v;
    }
}