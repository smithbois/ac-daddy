package com.smithboys.acdaddy.util;

public class TalkToDevice {

    public static void talkToDevice(boolean on) {
        // on = true means turn ac on AND update temp value from DesiredTempUtil.desiredTemp, on = false means turn ac off
        // DesiredTempUtil.desiredTemp is the int value for temperature

        // TODO @ joe

        if (on) {
            // turn on AC
            // update temp value in database
            System.out.println("AC ON");
        }
        else {
            // turn off AC
            // no need to update temp value in database
            System.out.println("AC OFF");
        }
    }

}
