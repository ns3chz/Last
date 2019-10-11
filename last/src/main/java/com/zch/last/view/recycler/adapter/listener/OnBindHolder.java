package com.zch.last.view.recycler.adapter.listener;


import androidx.recyclerview.widget.RecyclerView;

public abstract class OnBindHolder<VH extends RecyclerView.ViewHolder, Data> {
    public abstract void onBind(VH viewholder, Data data, int position);
}
