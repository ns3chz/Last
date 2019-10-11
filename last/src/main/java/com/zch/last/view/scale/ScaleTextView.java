package com.zch.last.view.scale;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.zch.last.R;


public class ScaleTextView extends AppCompatTextView {
    @NonNull
    public ManagerScale managerScale;

    public ScaleTextView(Context context) {
        this(context, null);
    }

    public ScaleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        managerScale = new ManagerScale();
        managerScale.params(context, attrs, R.styleable.ScaleTextView, R.styleable.ScaleView_scale, R.styleable.ScaleTextView_known);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measure = managerScale.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(measure[0], measure[1]);
    }

}
