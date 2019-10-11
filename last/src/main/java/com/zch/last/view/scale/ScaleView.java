package com.zch.last.view.scale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.zch.last.R;


public class ScaleView extends View {
    @NonNull
    public ManagerScale managerScale;

    public ScaleView(Context context) {
        this(context, null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        managerScale = new ManagerScale();
        managerScale.params(context, attrs, R.styleable.ScaleView, R.styleable.ScaleView_scale, R.styleable.ScaleView_known);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measure = managerScale.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(measure[0], measure[1]);
    }

}
