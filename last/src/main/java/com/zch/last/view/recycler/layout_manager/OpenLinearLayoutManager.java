//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zch.last.view.recycler.layout_manager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OpenLinearLayoutManager extends LinearLayoutManager {
    private Check canScrollVer;
    private Check canScrollHor;

    public OpenLinearLayoutManager(Context context) {
        super(context);
        this.canScrollVer = Check.UNK;
        this.canScrollHor = Check.UNK;
    }

    public OpenLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.canScrollVer = Check.UNK;
        this.canScrollHor = Check.UNK;
    }

    public OpenLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.canScrollVer = Check.UNK;
        this.canScrollHor = Check.UNK;
    }

    public OpenLinearLayoutManager setCanScrollVer(Check check) {
        this.canScrollVer = check;
        return this;
    }

    public OpenLinearLayoutManager setCanScrollHor(Check check) {
        this.canScrollHor = check;
        return this;
    }

    public OpenLinearLayoutManager setCanScroll(Check check) {
        this.canScrollHor = check;
        this.canScrollVer = check;
        return this;
    }

    public boolean canScrollVertically() {
        switch (this.canScrollVer) {
            case Y:
                return true;
            case N:
                return false;
            case UNK:
                return super.canScrollVertically();
            default:
                return super.canScrollVertically();
        }
    }

    public boolean canScrollHorizontally() {
        switch (this.canScrollHor) {
            case Y:
                return true;
            case N:
                return false;
            case UNK:
                return super.canScrollVertically();
            default:
                return super.canScrollVertically();
        }
    }
}
