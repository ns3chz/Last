package com.zch.last.view.recycler.adapter.listener;

import android.view.View;

public abstract class OnViewClickListener<VH, DATA> {
    public abstract void click(View view, VH holder, DATA data, int pos);
}
