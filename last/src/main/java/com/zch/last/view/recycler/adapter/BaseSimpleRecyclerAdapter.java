package com.zch.last.view.recycler.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zch.last.utils.UtilReflect;

import java.util.Map;

public abstract class BaseSimpleRecyclerAdapter<T> extends BaseWarpRecyclerAdapter<BaseSimpleRecyclerAdapter<T>.DataViewHolder, T> {
    @LayoutRes
    protected int layoutRes;
    protected String[] fieldNames;//参数名字，赋值给对应id的控件
    @IdRes
    protected int[] ids;//

    public BaseSimpleRecyclerAdapter(Context mContext, int layoutRes, String[] fieldNames, int[] ids) {
        super(mContext);
        this.layoutRes = layoutRes;
        this.fieldNames = fieldNames;
        this.ids = ids;
    }

    @Override
    public DataViewHolder onCreateViewHolders(ViewGroup parent, View view, int viewType) {
        return new DataViewHolder(view);
    }

    @Override
    public int onCreateViewRes(@NonNull ViewGroup parent, int viewType) {
        return layoutRes;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        public final View[] views;

        public DataViewHolder(View itemView) {
            super(itemView);
            if (ids != null) {
                views = new View[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    int id = ids[i];
                    views[i] = itemView.findViewById(id);
                }
            } else {
                views = null;
            }
        }

        public void setData(T data) {
            if (ids == null || ids.length == 0) {
                return;
            }
            for (int i = 0; i < ids.length; i++) {
                Object fieldValue = null;
                if (data != null) {
                    if (data instanceof CharSequence) {
                        fieldValue = data;
                    } else if (data instanceof Map) {
                        if (fieldNames != null && i < fieldNames.length) {
                            fieldValue = ((Map) data).get(fieldNames[i]);
                        }
                    } else {
                        if (fieldNames != null && i < fieldNames.length) {
                            fieldValue = UtilReflect.getField(data, fieldNames[i],true);
                        }
                    }
                }
                View view = views[i];
                if (view == null) continue;
                ToolsAdapter.setData2View(view, fieldValue);
            }
        }
    }

}
