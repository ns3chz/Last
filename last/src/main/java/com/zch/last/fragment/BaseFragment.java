package com.zch.last.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends BaseLastFragment {

    protected View rootView;
    private Unbinder bind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(layoutRes(), null, false);
        if (rootView == null) {
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        }
        if (useButterKnife()) {
            bind = ButterKnife.bind(this, rootView);
        }
        initView();
        initListener();
        initData();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }
    }


    /**
     * @return 开启ButterKnife添加控件
     */
    protected boolean useButterKnife() {
        return true;
    }

}
