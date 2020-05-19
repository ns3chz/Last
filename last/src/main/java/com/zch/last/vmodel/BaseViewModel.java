package com.zch.last.vmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseViewModel<VDB extends ViewDataBinding> {
    @NonNull
    public final VDB dataBinding;
    @NonNull
    public final Context context;
    @NonNull
    public final LayoutInflater layoutInflater;

    public BaseViewModel(@NonNull Context context, @NonNull View view) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);

        VDB bind = DataBindingUtil.bind(view);
        if (bind == null) {
            throw new IllegalArgumentException("dataBinding is NULL !!!");
        }
        dataBinding = bind;
        init(view);
        initListener(view);
        initData(view);
    }

    public abstract void init(@NonNull View root);

    public abstract void initListener(@NonNull View root);

    public abstract void initData(@NonNull View root);
}
