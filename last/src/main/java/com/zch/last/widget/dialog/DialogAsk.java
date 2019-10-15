package com.zch.last.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.zch.last.R;

public class DialogAsk extends BaseAlertDialog {
    private TextView btnNo;
    private TextView btnYes;
    private TextView title;
    private String titleText;
    private OnYesClickListener onYesClickListener;

    public DialogAsk(Context context) {
        super(context);
    }

    public DialogAsk(Context context, String ti) {
        super(context);
        setTitleText(ti);
    }

    @Override
    public int setRes() {
        return R.layout.dialog_layout_ask;
    }

    @Override
    public void buildParam(@NonNull AlertDialog.Builder builder, @NonNull AlertDialog alertDialog, @NonNull View view) {
        btnNo = view.findViewById(R.id.btn_no);
        btnYes = view.findViewById(R.id.btn_yes);
        title = view.findViewById(R.id.title);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissmiss();
                if (onYesClickListener != null) {
                    onYesClickListener.cancle();
                }
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissmiss();
                if (onYesClickListener != null) {
                    onYesClickListener.click();
                }
            }
        });
    }

    public DialogAsk setTitleBackgroundRes(@DrawableRes int ids) {
        title.setBackgroundResource(ids);
        return this;
    }

    public DialogAsk setTitleBackgroundColor(@ColorInt int color) {
        title.setBackgroundColor(color);
        return this;
    }

    public DialogAsk setButtonBackgroundRes(@DrawableRes int yesId, @DrawableRes int noId) {
        btnYes.setBackgroundResource(yesId);
        btnNo.setBackgroundResource(noId);
        return this;
    }

    public DialogAsk setButtonBackgroundColor(@ColorInt int yesId, @ColorInt int noId) {
        btnYes.setBackgroundColor(yesId);
        btnNo.setBackgroundColor(noId);
        return this;
    }

    public DialogAsk setTitleText(String ti) {
        titleText = ti;
        if (titleText == null) {
            titleText = "";
        }
        title.setText(titleText);
        return this;
    }

    public DialogAsk setTitleTextColor(@ColorInt int color) {
        title.setTextColor(color);
        return this;
    }

    public DialogAsk setButtonsText(String no, String yes) {
        if (no != null) {
            btnNo.setText(no);
        }
        if (yes != null) {
            btnYes.setText(yes);
        }
        return this;
    }

    public DialogAsk setButtonsTextColorRes(@ColorRes int... color) {
        if (color == null || color.length == 0) return this;
        int color1, color2 = -1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color1 = mContext.getResources().getColor(color[0], mContext.getTheme());
            if (color.length > 1) {
                color2 = mContext.getResources().getColor(color[1], mContext.getTheme());
            }
        } else {
            color1 = mContext.getResources().getColor(color[0]);
            if (color.length > 1) {
                color2 = mContext.getResources().getColor(color[1]);
            }
        }
        btnNo.setTextColor(color1);
        btnYes.setTextColor(color2 == -1 ? color1 : color2);
        return this;
    }

    public DialogAsk setButtonsTextColor(@ColorInt int... color) {
        if (color == null || color.length == 0) return this;
        btnNo.setTextColor(color[0]);
        btnYes.setTextColor(color.length > 1 ? color[1] : color[0]);
        return this;
    }

    public interface OnYesClickListener {
        public void click();

        public void cancle();
    }

    public DialogAsk setOnYesClickListener(OnYesClickListener onYesClickListener) {
        this.onYesClickListener = onYesClickListener;
        return this;
    }
}
