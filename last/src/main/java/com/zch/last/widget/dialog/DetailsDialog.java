package com.zch.last.widget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.zch.last.R;

import java.util.Arrays;


public class DetailsDialog extends BaseAlertDialog {

    private View lineOnBtn;
    public ImageView titleImg;
    public TextView titleText;
    public LinearLayout containerTitle;
    public TextView contentText;
    public EditText contentEdit;
    public LinearLayout containerBtn;
    public LinearLayout containerDialog;
    //
    private String[] buttons;
    private float buttonTextSize = 20;//
    @ColorInt
    private int buttonTextColor = Color.WHITE;
    @DrawableRes
    private int buttonBackground = R.drawable.selector_press_trans_2_light;
    private OnButtonClickListener onButtonClickListener;

    public DetailsDialog(Context context) {
        super(context);
    }

    @Override
    public int setRes() {
        return R.layout.dialog_details_layout;
    }


    @Override
    public void buildParam(@NonNull AlertDialog.Builder alertDialogBuilder, @NonNull AlertDialog alertDialog, @NonNull View parent) {
        initView(parent);
    }

    private void initView(View parent) {
        lineOnBtn = parent.findViewById(R.id.line_on_btn);
        titleImg = parent.findViewById(R.id.title_img);
        titleText = parent.findViewById(R.id.title_text);
        containerTitle = parent.findViewById(R.id.container_title);
        contentText = parent.findViewById(R.id.content_text);
        contentEdit = parent.findViewById(R.id.content_edit);
        containerBtn = parent.findViewById(R.id.container_btn);
        containerDialog = parent.findViewById(R.id.container_dialog);
    }

    public void refreshButtons() {
        boolean show = buttons != null && buttons.length != 0;
        containerBtn.setVisibility(show ? View.VISIBLE : View.GONE);
        lineOnBtn.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            for (int i = 0; i < buttons.length; i++) {
                String btnText = buttons[i];
                TextView btn = new TextView(mContext);
                btn.setTextSize(buttonTextSize);
                btn.setTextColor(buttonTextColor);
                btn.setBackgroundResource(buttonBackground);
                btn.setGravity(Gravity.CENTER);
                btn.setLayoutParams(params);
                btn.setText(btnText);
                containerBtn.addView(btn);
                //设置点击事件
                final int position = i;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onButtonClickListener != null) {
                            onButtonClickListener.onClick(v, position);
                        }
                    }
                });
                if (i != buttons.length - 1) {
                    View line = new View(mContext);
                    LinearLayout.LayoutParams lineparams = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                    line.setBackgroundResource(android.R.color.darker_gray);
                    line.setLayoutParams(lineparams);
                    containerBtn.addView(line);
                }
            }
        } else {
            containerBtn.removeAllViews();
        }
    }

    public void setTitleImg(@DrawableRes int res) {
        titleImg.setImageResource(res);
        titleImg.setVisibility(res == 0 ? View.GONE : View.VISIBLE);
    }

    public void setTitleImg(Drawable res) {
        titleImg.setImageDrawable(res);
        titleImg.setVisibility(res == null ? View.GONE : View.VISIBLE);
    }

    public void setContentText(String text) {
        contentText.setText(text);
        contentText.setVisibility(text == null ? View.GONE : View.VISIBLE);
    }

    public void setButtons(@Nullable String... btns) {
        if (Arrays.equals(btns, buttons)) return;
        this.buttons = btns;
        refreshButtons();
    }

    public void reset() {
        setContentText(null);
        setTitleImg(null);
        showEdit(false);
        setOnButtonClickListener(null);
        setCancelable(true);
        setButtons();
    }

    public void showEdit(boolean flag) {
        contentEdit.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    public void setButtonTextSize(float size) {
        buttonTextSize = size;
    }

    public void setButtonTextColor(int buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public void setButtonBackground(int buttonBackground) {
        this.buttonBackground = buttonBackground;
    }


    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {
        public void onClick(View v, int position);
    }
}
