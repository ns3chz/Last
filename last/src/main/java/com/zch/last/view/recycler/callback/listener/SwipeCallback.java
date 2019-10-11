package com.zch.last.view.recycler.callback.listener;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.zch.last.view.recycler.callback.Swiper;


public interface SwipeCallback extends BaseCallback {

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    public boolean isItemViewSwipeEnabled();

    public View getDirectionView(RecyclerView.ViewHolder holder, @Swiper.Direction int direction);

}
