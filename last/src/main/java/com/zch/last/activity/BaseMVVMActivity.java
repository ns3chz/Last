package com.zch.last.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.zch.last.utils.UtilPermission;

public abstract class BaseMVVMActivity<T extends ViewDataBinding> extends Activity implements BaseMVVMActivityImpl {
    protected T viewDataBinding;

    /**
     * 不让子类继承
     */
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, initLayoutRes());
        Intent intent = getIntent();
        if (intent != null) {
            initIntent(intent);
        }
        //
        initView();
        initListener();
        initData();
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UtilPermission.listen(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    //**********************************************************************************************
    //**************************************_SELF_************************************************
    //**********************************************************************************************

    @Override
    public void beforeSupterCreate(@Nullable Bundle savedInstanceState) {
    }
}
