package com.zch.last.view.recycler.callback.listener;


import androidx.recyclerview.widget.RecyclerView;

public abstract class OnDragListener {
    public abstract void onStart(RecyclerView.ViewHolder viewHolder, int startPosition);

    public abstract void onDragging(int startPosition, int curPosition);

    public abstract void onEnd(int startPosition, int endPosition);

}
