package com.zch.last.view.scale;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.zch.last.R;


public class ScaleRelativeLayout extends RelativeLayout {
    @NonNull
    public ManagerScale managerScale;

    public ScaleRelativeLayout(Context context) {
        this(context, null);
    }

    public ScaleRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        managerScale = new ManagerScale();
        managerScale.params(context, attrs, R.styleable.ScaleRelativeLayout, R.styleable.ScaleRelativeLayout_scale, R.styleable.ScaleRelativeLayout_known);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measure = managerScale.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(measure[0], measure[1]);
    }

}
