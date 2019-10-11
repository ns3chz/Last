package com.zch.last.widget.dialog;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.zch.last.R;

public class ProgressDialog extends BaseAlertDialog {
    private TextView titleTv;
    private ProgressBar progressbar;
    private Button cancelBtn;
    private View.OnClickListener onCancelListener;

    public ProgressDialog(Context context) {
        super(context);
    }

    @Override
    public int setRes() {
        return R.layout.dialog_download_progress;
    }

    @Override
    public void buildParam(@NonNull AlertDialog.Builder builder, @NonNull AlertDialog alertDialog, @NonNull View view) {
        initView(view);
    }

    @Override
    public void resetWindowParam(@NonNull Window window, @NonNull WindowManager windowManager, @NonNull WindowManager.LayoutParams windowParam) {
        super.resetWindowParam(window, windowManager, windowParam);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        windowParam.width = (int) (0.5f * point.x);
        windowParam.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private void initView(View view) {
        titleTv = view.findViewById(R.id.title);
        progressbar = view.findViewById(R.id.progressbar);
        cancelBtn = view.findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissmiss();
                if (onCancelListener != null) {
                    onCancelListener.onClick(v);
                }
            }
        });
    }

    public void setTitle(String title) {
        if (titleTv != null) {
            titleTv.setText(title);
        }
    }

    public void setProgress(int max, int frac) {
        setProgress(max, frac, 0);
    }

    public void setProgress(int max, int frac, int sec) {
        if (progressbar == null) return;
        progressbar.setMax(max);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressbar.setProgress(frac, false);
        } else {
            progressbar.setProgress(frac);
        }
        progressbar.setSecondaryProgress(sec);
    }

    public void setOnCancelListener(View.OnClickListener listener) {
        onCancelListener = listener;
    }

    public View.OnClickListener getOnCancelListener() {
        return onCancelListener;
    }
}
