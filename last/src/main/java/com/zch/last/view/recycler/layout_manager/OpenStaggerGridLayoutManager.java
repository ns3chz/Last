//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zch.last.view.recycler.layout_manager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class OpenStaggerGridLayoutManager extends StaggeredGridLayoutManager {
    private Check canScrollVer;
    private Check canScrollHor;

    public OpenStaggerGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.canScrollVer = Check.UNK;
        this.canScrollHor = Check.UNK;
    }

    public OpenStaggerGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
        this.canScrollVer = Check.UNK;
        this.canScrollHor = Check.UNK;
    }

    public OpenStaggerGridLayoutManager setCanScrollVer(Check check) {
        this.canScrollVer = check;
        return this;
    }

    public OpenStaggerGridLayoutManager setCanScrollHor(Check check) {
        this.canScrollHor = check;
        return this;
    }

    public OpenStaggerGridLayoutManager setCanScroll(Check check) {
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
