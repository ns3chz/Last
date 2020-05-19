package com.zch.last.widget.lifecycle;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.zch.last.utils.UtilLogger;

public class Java8Observer implements DefaultLifecycleObserver {
    private static final String TAG = "Java8Observer";

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        UtilLogger.logV(TAG, "onCreate");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        UtilLogger.logV(TAG, "onStart");
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        UtilLogger.logV(TAG, "onResume");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        UtilLogger.logV(TAG, "onPause");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        UtilLogger.logV(TAG, "onStop");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        UtilLogger.logV(TAG, "onDestroy");
    }
}
