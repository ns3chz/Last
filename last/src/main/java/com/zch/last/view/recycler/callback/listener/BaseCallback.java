package com.zch.last.view.recycler.callback.listener;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;

public interface BaseCallback {
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);

    public void onSelectChanged(RecyclerView.ViewHolder viewHolder, int actionState);

    public boolean onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive);

    public boolean onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive);

    public boolean clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);

}
