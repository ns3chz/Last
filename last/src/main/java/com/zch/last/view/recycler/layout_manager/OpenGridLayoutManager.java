//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zch.last.view.recycler.layout_manager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

public class OpenGridLayoutManager extends GridLayoutManager {
    private Check canScrollVer;
    private Check canScrollHor;

    public OpenGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.canScrollVer = Check.UNK;
        this.canScrollHor = Check.UNK;
    }

    public OpenGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        this.canScrollVer = Check.UNK;
        this.canScrollHor = Check.UNK;
    }

    public OpenGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        this.canScrollVer = Check.UNK;
        this.canScrollHor = Check.UNK;
    }

    public OpenGridLayoutManager setCanScrollVer(Check check) {
        this.canScrollVer = check;
        return this;
    }

    public OpenGridLayoutManager setCanScrollHor(Check check) {
        this.canScrollHor = check;
        return this;
    }

    public OpenGridLayoutManager setCanScroll(Check check) {
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
