package com.zch.last.constanse;

import android.util.Log;

public class Logger {

    public static void logFrame(String tag, String msg) {
        if (ConfigSwitch.LOG_FRAME) {
            Log.d(tag, msg);
        }
    }
}
