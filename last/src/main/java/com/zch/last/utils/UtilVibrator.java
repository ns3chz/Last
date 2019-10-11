package com.zch.last.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Service;
import android.content.pm.PackageManager;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.core.LastApplication;

/**
 * Created by Administrator on 2017/6/30.
 */

public class UtilVibrator {
    @Nullable
    private static UtilVibrator vibratorTools;

    @NonNull
    public static UtilVibrator get() {

        if (vibratorTools == null) {
            synchronized (UtilVibrator.class) {
                if (vibratorTools == null) {
                    vibratorTools = new UtilVibrator(LastApplication.INSTANCE);
                }
            }
        }
        return vibratorTools;
    }
    //----------------------------------------------------------------------------------

    @Nullable
    private Vibrator vibrator;

    public UtilVibrator(Application application) {
        if (vibrator == null && hasVibratePermission(application)) {
            synchronized (UtilVibrator.class) {
                if (vibrator == null) {
                    vibrator = (Vibrator) application.getSystemService(Service.VIBRATOR_SERVICE);
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    public void vibrate(long milliseconds) {
        if (vibrator != null) {
            vibrator.vibrate(milliseconds);
        }
    }

    @SuppressLint("MissingPermission")
    public void vibrate(long[] pattern, int repeat) {
        if (vibrator != null) {
            vibrator.vibrate(pattern, repeat);
        }
    }

    @SuppressLint("MissingPermission")
    public void stop() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    private static boolean hasVibratePermission(Application application) {
        PackageManager pm = application.getPackageManager();
        int hasPerm = pm.checkPermission(android.Manifest.permission.VIBRATE, application.getPackageName());
        return hasPerm == PackageManager.PERMISSION_GRANTED;
    }
}
