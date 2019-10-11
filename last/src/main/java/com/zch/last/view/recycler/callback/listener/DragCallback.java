package com.zch.last.view.recycler.callback.listener;


import androidx.recyclerview.widget.RecyclerView;

public interface DragCallback extends BaseCallback {

    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);

    public boolean isLongPressDragEnabled();
}
