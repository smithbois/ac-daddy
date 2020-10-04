package com.smithboys.acdaddy.util;

import java.util.TimerTask;

public class StopACTask extends TimerTask {
    @Override
    public void run() {
        TalkToDevice.talkToDevice(false);
    }
}