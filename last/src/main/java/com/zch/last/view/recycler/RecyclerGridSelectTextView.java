//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zch.last.view.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.zch.last.view.recycler.layout_manager.Check;
import com.zch.last.view.recycler.layout_manager.OpenGridLayoutManager;


public class RecyclerGridSelectTextView<MODEL> extends BaseRecyclerGridSelectTextView<MODEL> {
    private OpenGridLayoutManager gridLayoutManager;
    private final int defCol;

    public RecyclerGridSelectTextView(Context context) {
        this(context, null);
    }

    public RecyclerGridSelectTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerGridSelectTextView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.defCol = 3;
    }

    @Override
    protected void initLayoutManager() {
        this.gridLayoutManager = new OpenGridLayoutManager(this.getContext(), 3, 1, false);
        this.gridLayoutManager.setCanScroll(Check.N);
        this.setLayoutManager(this.gridLayoutManager);
    }

    public int getSpanCount() {
        return this.gridLayoutManager != null ? this.gridLayoutManager.getSpanCount() : -1;
    }


    public OpenGridLayoutManager getLayoutManager() {
        return this.gridLayoutManager;
    }
}
