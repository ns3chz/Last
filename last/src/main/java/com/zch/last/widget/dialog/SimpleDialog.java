package com.zch.last.widget.dialog;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class SimpleDialog extends BaseAlertDialog {
    private float mWidth ;
    private float mHeight;


    public SimpleDialog(int res, Context context, @FloatRange(from = 0, to = 1) float width, @FloatRange(from = 0, to = 1) float height) {
        super(res, context);
        this.mWidth = width;
        this.mHeight = height;
    }

    @Override
    public int setRes() {
        return 0;
    }

    @Override
    public void buildParam(@NonNull AlertDialog.Builder alertDialogBuilder, @NonNull AlertDialog alertDialog, @NonNull View parent) {

    }

    @Override
    public void resetWindowParam(@NonNull Window window, @NonNull WindowManager windowManager, @NonNull WindowManager.LayoutParams windowParam) {
        super.resetWindowParam(window, windowManager, windowParam);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        if (mWidth == 0) {
            windowParam.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else if (mWidth == 1) {
            windowParam.width = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            windowParam.width = (int) (mWidth * point.x);
        }
        if (mHeight == 0) {
            windowParam.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else if (mHeight == 1) {
            windowParam.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            windowParam.height = (int) (mHeight * point.y);
        }

    }
}
