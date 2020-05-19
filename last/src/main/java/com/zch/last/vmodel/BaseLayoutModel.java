package com.zch.last.vmodel;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

public abstract class BaseLayoutModel<VDB extends ViewDataBinding> extends BaseViewModel<VDB> {

    public BaseLayoutModel(@NonNull Context context, int res) {
        super(context, LayoutInflater.from(context).inflate(res, null, false));

    }

}
