package com.zch.last.view.scale;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.zch.last.R;


public class ScaleFramelayout extends FrameLayout {
    @NonNull
    public ManagerScale managerScale;

    public ScaleFramelayout(Context context) {
        this(context, null);
    }

    public ScaleFramelayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleFramelayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        managerScale = new ManagerScale();
        managerScale.params(context, attrs, R.styleable.ScaleFramelayout, R.styleable.ScaleFramelayout_scale, R.styleable.ScaleFramelayout_known);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measure = managerScale.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(measure[0], measure[1]);
    }

}
