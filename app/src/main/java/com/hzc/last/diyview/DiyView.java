package com.hzc.last.diyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.utils.UtilLogger;

public class DiyView extends ViewGroup {
    private final String TAG = getClass().getName();
    private View view;
    private Paint paint;

    public DiyView(@NonNull Context context) {
        this(context, null);
        UtilLogger.logV(TAG, "constructor 1");
    }

    public DiyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        UtilLogger.logV(TAG, "constructor 2");
    }

    public DiyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        UtilLogger.logV(TAG, "constructor 3");
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);

        view = new View(getContext());
        addView(view);
        view.setBackgroundColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        UtilLogger.logV(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int wsize = MeasureSpec.getSize(widthMeasureSpec);
//        int hsize = MeasureSpec.getSize(heightMeasureSpec);
//        int wmode = MeasureSpec.getMode(widthMeasureSpec);
//        int hmode = MeasureSpec.getMode(heightMeasureSpec);
//        UtilLogger.logV(TAG, "wsize = " + wsize + " , wmode = " + wmode +
//                "\nhsize = " + hsize + ", hmode = " + hmode);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        UtilLogger.logV(TAG, "onLayout");

        view.layout(left + 100, top + 100, right - 100, bottom - 100);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        UtilLogger.logV(TAG, "onDraw");
        super.onDraw(canvas);
        canvas.drawCircle(100,100,100,paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        UtilLogger.logV(TAG, "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onAttachedToWindow() {
        UtilLogger.logV(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        UtilLogger.logV(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        UtilLogger.logV(TAG, "onFocusChanged");
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        UtilLogger.logV(TAG, "onWindowFocusChanged");
        super.onWindowFocusChanged(hasWindowFocus);
    }
}
