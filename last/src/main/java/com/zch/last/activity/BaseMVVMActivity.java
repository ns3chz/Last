package com.zch.last.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseMVVMActivity<T extends ViewDataBinding> extends Activity implements BaseActivityImpl {
    protected T viewDataBinding;

    /**
     * 不让子类继承
     */
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {

        this.createBefore(savedInstanceState);
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, setContentRes());
        this.createAfter(savedInstanceState);

        this.initIntent(getIntent());

        this.initView();

        this.initListener();

        this.initData();
    }

    @Override
    public void onCreated(@Nullable Bundle savedInstanceState) {

    }

    @LayoutRes
    protected abstract int setContentRes();

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    //**********************************************************************************************
    //**************************************_SELF_************************************************
    //**********************************************************************************************


    @Override
    public final boolean useButterKnife() {
        return false;
    }

    protected void createBefore(@Nullable Bundle savedInstanceState) {
    }

    protected void createAfter(@Nullable Bundle savedInstanceState) {
    }

    /**
     * @param tag 设置log的tag
     */
    @Override
    public void setTag(@NonNull String tag) {
    }
}
