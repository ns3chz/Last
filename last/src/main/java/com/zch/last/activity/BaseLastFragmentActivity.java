package com.zch.last.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseLastFragmentActivity extends FragmentActivity implements BaseActivityImpl {

    public void replace(@IdRes int containerViewId, @NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment).commit();
    }

    private BaseActivityHolder activityHolder;

    /**
     * 不让子类继承
     */
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        activityHolder = new BaseActivityHolder(this, this);

        this.createBefore(savedInstanceState);
        super.onCreate(savedInstanceState);
        this.createAfter(savedInstanceState);

        activityHolder.onCreate(savedInstanceState);
    }

    /**
     * 不让子类继承
     */
    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityHolder != null) {
            activityHolder.onDestroy();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (activityHolder != null) {
            activityHolder.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (activityHolder != null) {
            activityHolder.onActivityResult(requestCode, resultCode, data);
        }
    }
    //**********************************************************************************************
    //**************************************_SELF_************************************************
    //**********************************************************************************************

    public boolean useButterKnife() {
        return false;
    }

    protected void createBefore(@Nullable Bundle savedInstanceState) {
        if (activityHolder != null) {
            activityHolder.createBefore(savedInstanceState);
        }
    }

    protected void createAfter(@Nullable Bundle savedInstanceState) {
        if (activityHolder != null) {
            activityHolder.createAfter(savedInstanceState);
        }
    }

    /**
     * @param tag 设置log的tag
     */
    @Override
    public void setTag(@NonNull String tag) {
        if (activityHolder != null) {
            activityHolder.TAG = tag;
        }
    }
}
