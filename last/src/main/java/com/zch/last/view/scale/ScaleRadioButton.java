package com.zch.last.view.scale;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.zch.last.R;


public class ScaleRadioButton extends AppCompatRadioButton {
    @NonNull
    public ManagerScale managerScale;

    public ScaleRadioButton(Context context) {
        this(context, null);
    }

    public ScaleRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        managerScale = new ManagerScale();
        managerScale.params(context, attrs, R.styleable.ScaleRadioButton, R.styleable.ScaleRadioButton_scale, R.styleable.ScaleRadioButton_known);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measure = managerScale.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(measure[0], measure[1]);
    }

}
