package com.smithboys.acdaddy.util;

import java.util.TimerTask;

public class StartACTask extends TimerTask {
    @Override
    public void run() {
        TalkToDevice.talkToDevice(true);
    }
}
