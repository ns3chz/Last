package com.zch.last.vmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.zch.last.activity.ImpActivityLifeCycle;

import java.lang.ref.WeakReference;

public abstract class BaseViewModel implements ImpActivityLifeCycle {
    @NonNull
    protected final WeakReference<Activity> wrActivity;

    public BaseViewModel(@NonNull Activity activity) {
        this.wrActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
