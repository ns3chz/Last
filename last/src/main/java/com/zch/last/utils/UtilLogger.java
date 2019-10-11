package com.zch.last.utils;

import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import com.zch.last.BuildConfig;

public class UtilLogger {
    private static final String TAG = "Logger..";

    public static void logV(@Nullable String tag, @Nullable String msg) {
        log(tag, msg, Log.VERBOSE);
    }

    public static void logD(@Nullable String tag, @Nullable String msg) {
        log(tag, msg, Log.DEBUG);
    }

    public static void logI(@Nullable String tag, @Nullable String msg) {
        log(tag, msg, Log.INFO);
    }

    public static void logW(@Nullable String tag, @Nullable String msg) {
        log(tag, msg, Log.WARN);
    }

    public static void logE(@Nullable String tag, @Nullable String msg) {
        log(tag, msg, Log.ERROR);
    }


    private static void log(@Nullable String tag, @Nullable String msg, @IntRange(from = 2, to = 6) int level) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        if (tag == null || tag.length() == 0) {
            tag = TAG;
        }
        if (msg == null) {
            msg = "null";
        }
        switch (level) {
            case Log.VERBOSE:
                Log.v(tag, msg);
                break;
            case Log.DEBUG:
                Log.d(tag, msg);
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            case Log.ERROR:
                Log.e(tag, msg);
                break;
        }
    }
}
