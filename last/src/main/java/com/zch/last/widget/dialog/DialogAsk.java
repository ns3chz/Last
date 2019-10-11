package com.zch.last.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

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

    public void setTitleText(String ti) {
        titleText = ti;
        if (titleText == null) {
            titleText = "";
        }
        title.setText(titleText);
    }

    public void setButtonsText(String no, String yes) {
        if (no != null) {
            btnNo.setText(no);
        }
        if (yes != null) {
            btnYes.setText(yes);
        }
    }

    public interface OnYesClickListener {
        public void click();

        public void cancle();
    }

    public void setOnYesClickListener(OnYesClickListener onYesClickListener) {
        this.onYesClickListener = onYesClickListener;
    }
}
