package com.zch.last.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.utils.UtilLogger;
import com.zch.last.utils.UtilPermission;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.Unbinder;

class BaseActivityHolder {
    @NonNull
    String TAG = "BaseActivityHolder";
    @NonNull
    private BaseActivityImpl IMPL;
    @NonNull
    private WeakReference<Activity> ACTIVITY;
    @Nullable
    private Unbinder unbinder;

    private int aHash;

    BaseActivityHolder(@NonNull Activity activity, @NonNull BaseActivityImpl IMPL) {
        this.IMPL = IMPL;
        aHash = activity.hashCode();
        this.ACTIVITY = new WeakReference<>(activity);
    }

    final void onCreate(@Nullable Bundle savedInstanceState) {
        Activity activity = ACTIVITY.get();
        log("Activity onCreate : " + aHash);

        IMPL.setContentView(savedInstanceState);
        if (IMPL.useButterKnife()) {
            unbinder = ButterKnife.bind(activity);
        }
        Intent intent = activity.getIntent();
        if (intent != null) {
            IMPL.initIntent(intent);
        }
        //
        IMPL.initView();
        IMPL.initListener();
        IMPL.initData();
    }

    void createBefore(@Nullable Bundle savedInstanceState) {
        log("Activity createBefore : " + aHash);
    }

    void createAfter(@Nullable Bundle savedInstanceState) {
        log("Activity createAfter : " + aHash);
    }

    void onDestroy() {
        log("Activity onDestroy : " + aHash);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        log("Activity onRequestPermissionsResult : " + aHash +
                "\nrequestCode = " + requestCode +
                "\npermissions = " + Arrays.toString(permissions) +
                "\ngrantResults = " + Arrays.toString(grantResults));
        Activity activity = ACTIVITY.get();
        if (activity != null) {
            UtilPermission.listen(activity, requestCode, permissions, grantResults);
        }
    }

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        log("Activity onActivityResult : " + aHash +
                "\nrequestCode = " + requestCode +
                "\nresultCode = " + resultCode +
                "\ndata = " + (data == null ? "NULL" : data.toString()));
    }

    private void log(String text) {
        UtilLogger.logV(TAG, text);
    }
}
