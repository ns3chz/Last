package com.zch.last.core;

import android.app.Application;

import com.zch.last.constanse.DeviceParams;

public class LastApplication extends Application {

    public static LastApplication INSTANCE;


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        DeviceParams.loadDeviceParams(this);

        registerActivityLifecycleCallbacks(ActivityLifecycleManager.getInstance());
    }
}
