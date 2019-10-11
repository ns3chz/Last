package com.zch.last.view.scale;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.zch.last.R;


public class ScaleLinearLayout extends LinearLayout {
    @NonNull
    public ManagerScale managerScale;

    public ScaleLinearLayout(Context context) {
        this(context, null);
    }

    public ScaleLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        managerScale = new ManagerScale();
        managerScale.params(context, attrs, R.styleable.ScaleLinearLayout, R.styleable.ScaleLinearLayout_scale, R.styleable.ScaleLinearLayout_known);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measure = managerScale.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(measure[0], measure[1]);
    }

}
