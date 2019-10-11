package com.zch.last.view.scale;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.zch.last.R;


public class ScaleImageView extends AppCompatImageView {
    @NonNull
    public ManagerScale managerScale;

    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        managerScale = new ManagerScale();
        managerScale.params(context, attrs, R.styleable.ScaleImageView, R.styleable.ScaleImageView_scale, R.styleable.ScaleImageView_known);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measure = managerScale.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(measure[0], measure[1]);
    }

}
