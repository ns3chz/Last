package com.zch.last.model.clickview;

import android.widget.TextView;

import androidx.annotation.Nullable;

public abstract class TextViewClick extends AbsViewClick<TextView> {
    @Nullable
    public String text;

    public TextViewClick(@Nullable String text) {
        this.text = text;
    }

    @Override
    public void prepare(TextView view) {

    }
}
