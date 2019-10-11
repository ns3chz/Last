package com.zch.last.view.recycler.adapter.listener;

import android.view.View;

public abstract class OnViewLongClickListener<VH, DATA> {
    public boolean onViewLoneClick(View v, VH viewholder, DATA data, int position) {
        return true;
    }

}
