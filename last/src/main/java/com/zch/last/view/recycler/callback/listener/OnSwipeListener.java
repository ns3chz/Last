package com.zch.last.view.recycler.callback.listener;


import androidx.recyclerview.widget.RecyclerView;

public abstract class OnSwipeListener {
    public abstract void onStart(RecyclerView.ViewHolder viewHolder);

    public abstract void onSwiping();

    public abstract void onEnd();

}
