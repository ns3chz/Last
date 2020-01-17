package com.zch.last.constanse;

import android.util.Log;

import com.zch.last.BuildConfig;

public class Logger {

    public static void logFrame(String tag, String msg) {
//        if (ConfigSwitch.LOG_FRAME) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }
}
