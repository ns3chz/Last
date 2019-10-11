package com.zch.last.view.scale;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;

public class ManagerScale implements ScaleInterface {
    public float scale;//宽/高
    @IntRange(from = 0, to = 2)
    public int known;//0=未知,1=宽已知,2=高已知

    @Override
    public void params(@NonNull Context context, AttributeSet set, @NonNull int[] attrs, int scaleIndex, int knownIndex) {
        TypedArray array = context.obtainStyledAttributes(set, attrs);
        scale = array.getFloat(scaleIndex, 1);
        known = array.getInt(knownIndex, 0);
        array.recycle();
    }

    /**
     * @return width, height
     */
    @Override
    @NonNull
    public int[] measure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;
        if (scale != 0) {
            switch (known) {
                case 1:
                    height = X2Y(width);
                    break;
                case 2:
                    width = Y2X(height);
                    break;
            }
        }
        return new int[]{width, height};
    }

    /**
     * 宽/高，宽已知
     */
    private int X2Y(int widthMeasureSpec) {
        // int width = MeasureSpec.getSize(widthMeasureSpec);
        // float height = width / ratio;
        // return MeasureSpec.makeMeasureSpec((int) height,
        // MeasureSpec.EXACTLY);
        return MeasureSpec.makeMeasureSpec(
                (int) (MeasureSpec.getSize(widthMeasureSpec) / scale), MeasureSpec.EXACTLY);
    }

    /**
     * 宽/高,高已知
     */
    private int Y2X(int heightMeasureSpec) {
        // int height = MeasureSpec.getSize(heightMeasureSpec);
        // float width = height * ratio;
        // return MeasureSpec.makeMeasureSpec((int) width, MeasureSpec.EXACTLY);
        return MeasureSpec.makeMeasureSpec(
                (int) (MeasureSpec.getSize(heightMeasureSpec) * scale), MeasureSpec.EXACTLY);
    }

}

interface ScaleInterface {
    /**
     * @param attrs      attrs集合
     * @param scaleIndex scale id
     * @param knownIndex known id
     */
    void params(@NonNull Context context, AttributeSet set, @NonNull int[] attrs, @StyleableRes int scaleIndex, @StyleableRes int knownIndex);

    int[] measure(int widthMeasureSpec, int heightMeasureSpec);
}