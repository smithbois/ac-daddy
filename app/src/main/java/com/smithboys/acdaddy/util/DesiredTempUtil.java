package com.smithboys.acdaddy.util;

import android.graphics.Color;

import androidx.fragment.app.Fragment;

public class DesiredTempUtil extends Fragment {
    public static int desiredTemp = 72;

    public static int getColor() {
        int r = 31 + (desiredTemp - 60) * 11;
        int g = 125 - (desiredTemp - 60) * 6;
        int b = 184 - (desiredTemp - 60) * 5;

        return Color.argb(255, r, g, b);
    }

    // TODO
    public static int getColorFaded() {
        int r = 31 + (desiredTemp - 60) * 11;
        int g = 125 - (desiredTemp - 60) * 6;
        int b = 184 - (desiredTemp - 60) * 5;

        return Color.argb(255, r, g, b);
    }
}
