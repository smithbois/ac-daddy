package com.smithboys.acdaddy.util;


import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class OnOffAtTime {

    static Timer timer;

    public static void setSchedule(int hour, int minute, boolean startAC) {
        Calendar today = Calendar.getInstance();
        if (today.get(Calendar.HOUR_OF_DAY) * 100 + today.get(Calendar.MINUTE) < hour * 100 + minute) {
            today.set(Calendar.HOUR_OF_DAY, hour);
            today.set(Calendar.MINUTE, minute);
            today.set(Calendar.SECOND, 0);

            if (startAC) {
                timer = new Timer();
                timer.schedule(new StartACTask(), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
            } else {
                timer = new Timer();
                timer.schedule(new StopACTask(), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
            }
        }
    }

    public static void resetSchedule() {
        try {
            timer.cancel();
        } catch (NullPointerException ignored) {}
    }
}
