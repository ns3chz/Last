//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zch.last.view.recycler.adapter.listener;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.zch.last.view.recycler.model.ModelChoose;

import java.util.List;

public class OnRecyclerItemSelectedListener<VH extends RecyclerView.ViewHolder, T> {
    public OnRecyclerItemSelectedListener() {
    }

    public void selected(@Nullable VH holder, @NonNull List<ModelChoose<T>> chooseList, @NonNull List<ModelChoose<T>> cancelList) {
//        Log.d(this.getClass(), "selected position: " + position + " ;data= " + JsonTools.parseObject2JsonString(data), new Object[0]);
    }
}
