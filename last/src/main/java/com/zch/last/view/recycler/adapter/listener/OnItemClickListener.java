package com.zch.last.view.recycler.adapter.listener;

public abstract class OnItemClickListener<VH, DATA> {
    public abstract boolean onItemClick(VH viewholder, DATA data, int position);
}
