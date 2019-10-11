package com.zch.last.view.tab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


public abstract class WrapSlidingPagerAdapter<DATA,PAGE> extends BaseSlidingPagerAdapter<DATA,PAGE> {
    protected LayoutInflater mInflater;

    public WrapSlidingPagerAdapter(Context mContext) {
        super(mContext);
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return getItem(container, position);
    }

    /**
     * Return the Object associated with a specified position.
     */
    public abstract Object getItem(@NonNull ViewGroup container, int position);
}
