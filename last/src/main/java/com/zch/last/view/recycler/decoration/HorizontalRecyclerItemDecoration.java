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

public class HorizontalRecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private int spaceWidth;
    private Drawable divider;

    public HorizontalRecyclerItemDecoration(@ColorInt int color, int space) {
        this.spaceWidth = space;
        this.divider = new ColorDrawable(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (spaceWidth <= 0) {
            super.onDraw(c, parent, state);
            return;
        }
        int left, right, top, bot;
        //
        top = parent.getPaddingTop();
        bot = parent.getHeight()-parent.getPaddingBottom();
//        left = parent.getPaddingLeft();
//        right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            left = child.getRight()+params.rightMargin;
            right = left+spaceWidth;
//            top = child.getBottom() + params.bottomMargin;
//            bot = top + spaceHeight;
            divider.setBounds(left, top, right, bot);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (spaceWidth > 0) {
            outRect.right = spaceWidth;
        }

    }

}
