package com.zch.last.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.utils.UtilPermission;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseLastActivity extends Activity implements BaseActivityImpl {

    private Unbinder unbinder;

    /**
     * 不让子类继承
     */
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayoutRes());
        if (useButterKnife()) {
            unbinder = ButterKnife.bind(this);
        }
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
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UtilPermission.listen(this,requestCode, permissions, grantResults);
    }

    //**********************************************************************************************
    //**************************************_SELF_************************************************
    //**********************************************************************************************

    public boolean useButterKnife() {
        return false;
    }

    @Override
    public void beforeSupterCreate(@Nullable Bundle savedInstanceState) {
    }
}
