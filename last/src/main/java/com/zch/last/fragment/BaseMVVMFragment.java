package com.zch.last.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseMVVMFragment<T extends ViewDataBinding> extends BaseLastFragment {

    protected T viewDataBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutRes(), container, false);
        initView();
        initListener();
        initData();
        return viewDataBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            viewDataBinding.unbind();
        } catch (Exception ignored) {
        }
    }

}
