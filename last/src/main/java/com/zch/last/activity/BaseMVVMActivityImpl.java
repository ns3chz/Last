package com.zch.last.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

interface BaseMVVMActivityImpl{

    void beforeSupterCreate(@Nullable Bundle savedInstanceState);

    @LayoutRes
    int initLayoutRes();

    void initIntent(@NonNull Intent intent);

    void initView();

    void initListener();

    void initData();
}
