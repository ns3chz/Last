package com.zch.last.view.recycler.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2017/3/30.
 */

public class VerticalRecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private int spaceHeight = 0;
    private Drawable divider;

    public VerticalRecyclerItemDecoration(@ColorInt int color, int space) {
        this.spaceHeight = space;
        this.divider = new ColorDrawable(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (spaceHeight <= 0) {
            super.onDraw(c, parent, state);
            return;
        }
        int left, right, top, bot;
        //
        left = parent.getPaddingLeft();
        right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getBottom() + params.bottomMargin;
            bot = top + spaceHeight;
            divider.setBounds(left, top, right, bot);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (spaceHeight > 0) {
            outRect.bottom = spaceHeight;
        }

    }

}
