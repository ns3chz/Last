package com.zch.last.core;

import android.app.Application;
import android.os.Handler;

import com.zch.last.constanse.DeviceParams;

public class LastApplication extends Application {

    public static LastApplication INSTANCE;
    public static Handler HANDLER;


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        HANDLER = new Handler();
        DeviceParams.loadDeviceParams(this);

        registerActivityLifecycleCallbacks(ActivityLifecycleManager.getInstance());
        UncaughtExceptionHandler.catchCrash(this);
    }
}
