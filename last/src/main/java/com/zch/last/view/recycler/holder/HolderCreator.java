package com.zch.last.view.recycler.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

public class HolderCreator extends BaseRecyclerViewHolder {

    public static HolderCreator create(Context context, ViewGroup parent, @LayoutRes int layoutId) {
        return new HolderCreator(context, parent, layoutId);
    }

    public static HolderCreator create(View itemView) {
        return new HolderCreator(itemView);
    }

    /**
     * @param parent needs recyclerView
     */
    public HolderCreator(Context context, ViewGroup parent, @LayoutRes int layoutId) {
        this(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    public HolderCreator(View itemView) {
        super(itemView);
    }
}
