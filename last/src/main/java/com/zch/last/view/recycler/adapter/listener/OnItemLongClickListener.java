package com.zch.last.view.recycler.adapter.listener;

public abstract class OnItemLongClickListener<VH, DATA> {
    public abstract boolean onItemLoneClick(VH viewholder, DATA data, int position);

}
