package com.zch.last.view.recycler.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;


public abstract class BaseWarpRecyclerAdapter<VH extends RecyclerView.ViewHolder, DATA> extends BaseRecyclerHFAdapter<VH, DATA> {

    public BaseWarpRecyclerAdapter(Context mContext) {
        super(mContext);
    }
}
