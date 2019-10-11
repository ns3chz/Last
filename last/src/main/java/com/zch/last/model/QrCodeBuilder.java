package com.zch.last.model;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class QrCodeBuilder {
    public static QrCodeBuilder newBuilder() {
        return new QrCodeBuilder();
    }

    private QrCodeBuilder() {
    }

    private int with = 50;//宽
    private int height = 50;//高
    @ColorInt
    private int codeColor = Color.BLACK;//二维码颜色
    @ColorInt
    private int bgColor = Color.WHITE;//背景色
    private int padding_left = 10;//左边距
    private int padding_top = 10;//上边距
    private int padding_right = 10;//右边距
    private int padding_bot = 10;//下边距

    @Nullable
    private Bitmap.Config bitmapConfig;//图片质量

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getWith() {
        return with;
    }

    public QrCodeBuilder setWith(@IntRange(from = 0, to = Integer.MAX_VALUE) int with) {
        this.with = with;
        return this;
    }

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getHeight() {
        return height;
    }

    public QrCodeBuilder setHeight(@IntRange(from = 0, to = Integer.MAX_VALUE) int height) {
        this.height = height;
        return this;
    }

    @ColorInt
    public int getCodeColor() {
        return codeColor;
    }

    public QrCodeBuilder setCodeColor(@ColorInt int codeColor) {
        this.codeColor = codeColor;
        return this;
    }

    @ColorInt
    public int getBgColor() {
        return bgColor;
    }

    public QrCodeBuilder setBgColor(@ColorInt int bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getPadding_left() {
        return padding_left;
    }

    public QrCodeBuilder setPadding_left(@IntRange(from = 0, to = Integer.MAX_VALUE) int padding_left) {
        this.padding_left = padding_left;
        return this;
    }

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getPadding_top() {
        return padding_top;
    }

    public QrCodeBuilder setPadding_top(@IntRange(from = 0, to = Integer.MAX_VALUE) int padding_top) {
        this.padding_top = padding_top;
        return this;
    }

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getPadding_right() {
        return padding_right;
    }

    public QrCodeBuilder setPadding_right(@IntRange(from = 0, to = Integer.MAX_VALUE) int padding_right) {
        this.padding_right = padding_right;
        return this;
    }

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getPadding_bot() {
        return padding_bot;
    }

    public QrCodeBuilder setPadding_bot(@IntRange(from = 0, to = Integer.MAX_VALUE) int padding_bot) {
        this.padding_bot = padding_bot;
        return this;
    }

    @NonNull
    public Bitmap.Config getBitmapConfig() {
        return bitmapConfig == null ? Bitmap.Config.RGB_565 : bitmapConfig;
    }

    public QrCodeBuilder setBitmapConfig(@NonNull Bitmap.Config bitmapConfig) {
        this.bitmapConfig = bitmapConfig;
        return this;
    }

    public QrCodeBuilder setPadding(@IntRange(from = 0, to = Integer.MAX_VALUE) int padding) {
        setPadding_left(padding);
        setPadding_top(padding);
        setPadding_right(padding);
        setPadding_bot(padding);
        return this;
    }
}
