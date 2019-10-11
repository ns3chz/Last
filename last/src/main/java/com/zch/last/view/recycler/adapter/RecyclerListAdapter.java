package com.zch.last.view.recycler.adapter;

import android.content.Context;


public class RecyclerListAdapter<T> extends BaseSimpleRecyclerAdapter<T> {

    public RecyclerListAdapter(Context mContext, int layoutRes, String[] fieldNames, int[] ids) {
        super(mContext, layoutRes, fieldNames, ids);
    }

    @Override
    public void onBindsViewHolder(DataViewHolder holder, int position) {
        T data = dataList.get(position);
        holder.setData(data);
    }

}
