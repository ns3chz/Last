package com.zch.last.model.clickview;

import android.view.View;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public abstract class AbsViewClick<V extends View> implements InterfViewClick, View.OnClickListener {
    public int index = -1;
    @NonNull
    public final Map<String, Object> extras;

    public AbsViewClick() {
        extras = new HashMap<>();
    }

    /**
     * 准备前的参数设置工作
     */
    public abstract void prepare(V view);
}
