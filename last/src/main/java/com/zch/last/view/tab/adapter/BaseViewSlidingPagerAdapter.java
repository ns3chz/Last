package com.zch.last.view.tab.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public abstract class BaseViewSlidingPagerAdapter<DATA> extends WrapSlidingPagerAdapter<DATA,Object> {

    @NonNull
    private final SparseArray<View> views;

    public BaseViewSlidingPagerAdapter(Context mContext) {
        super(mContext);
        views = new SparseArray<>(3);
    }

    @Override
    public Object getItem(@NonNull ViewGroup container, int position) {
        View view = views.get(position);
        if (view == null) {
            view = mInflater.inflate(onLayoutRes(position), container, false);
            views.put(position, view);
            onBindView(view, getData(position), position);
        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        views.delete(position);
    }

    @LayoutRes
    public abstract int onLayoutRes(int position);

    public abstract void onBindView(@NonNull View view, DATA data, int position);

}
