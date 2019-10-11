package com.zch.last.view.recycler.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(@NonNull Context context, @LayoutRes int layRes, @NonNull ViewGroup parent) {
        this(LayoutInflater.from(context).inflate(layRes, parent, false));
    }

    public BaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
