package com.zch.last.widget.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
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
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.zch.last.R;
import com.zch.last.utils.UtilCom;
import com.zch.last.utils.UtilDevice;
import com.zch.last.utils.UtilSystem;
import com.zch.last.utils.UtilView;

import java.util.Arrays;


public class DetailsDialog extends BaseAlertDialog<DetailsDialog> {

    public View lineOnBtn;
    public ImageView titleImg;
    public TextView titleText;
    public LinearLayout containerTitle;
    public LinearLayout containerBody;
    public TextView contentText;
    public EditText contentEdit;
    public LinearLayout containerBtn;
    public LinearLayout containerDialog;

    private ButtonCreator buttonCreator;
    //


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

    @Override
    public void show() {
        super.show();
        if (contentEdit.getVisibility() == View.VISIBLE) {
            contentEdit.requestFocus();
        }
    }

    @Override
    public void dissmiss() {
        super.dissmiss();
        if (contentEdit.getVisibility() == View.VISIBLE) {
            contentEdit.clearFocus();
            UtilSystem.showKeyboard(contentEdit, false);
        }

    }

    private void initView(View parent) {
        lineOnBtn = parent.findViewById(R.id.line_on_btn);
        titleImg = parent.findViewById(R.id.title_img);
        titleText = parent.findViewById(R.id.title_text);
        containerTitle = parent.findViewById(R.id.container_title);
        containerBody = parent.findViewById(R.id.container_body);
        contentText = parent.findViewById(R.id.content_text);
        contentEdit = parent.findViewById(R.id.content_edit);
        containerBtn = parent.findViewById(R.id.container_btn);
        containerDialog = parent.findViewById(R.id.container_dialog);
    }

    public ButtonCreator createButtons() {
        if (buttonCreator == null) {
            buttonCreator = new ButtonCreator();
        }
        return buttonCreator;
    }

    public class ButtonCreator {
        private static final int DEF_TEXT_SIZE = 20;
        @ColorInt
        private static final int DEF_TEXT_COLOR = Color.BLACK;
        @DrawableRes
        private final int DEF_BACKGROUND = R.drawable.selector_press_trans_2_light;


        private String[] buttons;
        private float[] buttonTextSize = null;//
        private ColorStateList[] buttonTextColorStateList = null;
        @DrawableRes
        private int[] buttonBackground = null;
        private OnButtonClickListener onButtonClickListener;

        public DetailsDialog build() {
            containerBtn.removeAllViews();
            boolean show = buttons != null && buttons.length != 0;
            containerBtn.setVisibility(show ? View.VISIBLE : View.GONE);
            lineOnBtn.setVisibility(show ? View.VISIBLE : View.GONE);
            if (show) {
                UtilView.setViewVisibility(containerBtn, View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                for (int i = 0; i < buttons.length; i++) {
                    String btnText = buttons[i];
                    TextView btn = new TextView(mContext);
                    //text size
                    if (buttonTextSize == null || buttonTextSize.length == 0) {
                        btn.setTextSize(DEF_TEXT_SIZE);
                    } else {
                        if (i > buttonTextSize.length - 1) {
                            btn.setTextSize(buttonTextSize[buttonTextSize.length - 1]);
                        } else {
                            btn.setTextSize(buttonTextSize[i]);
                        }
                    }
                    //text color
                    if (buttonTextColorStateList == null || buttonTextColorStateList.length == 0) {
                        btn.setTextColor(DEF_TEXT_COLOR);
                    } else {
                        if (i > buttonTextColorStateList.length - 1) {
                            btn.setTextColor(buttonTextColorStateList[buttonTextColorStateList.length - 1]);
                        } else {
                            btn.setTextColor(buttonTextColorStateList[i]);
                        }
                    }
                    //background
                    if (buttonBackground == null || buttonBackground.length == 0) {
                        btn.setBackgroundResource(DEF_BACKGROUND);
                    } else {
                        if (i > buttonBackground.length - 1) {
                            btn.setBackgroundResource(buttonBackground[buttonBackground.length - 1]);
                        } else {
                            btn.setBackgroundResource(buttonBackground[i]);
                        }
                    }
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
                UtilView.setViewVisibility(containerBtn, View.GONE);
            }
            return DetailsDialog.this;
        }

        public ButtonCreator setButtons(@Nullable String... btns) {
            if (Arrays.equals(btns, buttons)) return this;
            this.buttons = btns;
            return this;
        }

        public ButtonCreator setButtonTextSize(float... size) {
            buttonTextSize = size;
            return this;
        }

        public ButtonCreator setButtonTextSizeRes(@DimenRes int... size) {
            if (size == null || size.length == 0) {
                buttonTextSize = null;
                return this;
            }
            buttonTextSize = new float[size.length];
            for (int i = 0; i < buttonTextSize.length; i++) {
                float dimension = mContext.getResources().getDimension(size[i]);
                buttonTextSize[i] = UtilDevice.px2sp(mContext, dimension);
            }
            return this;
        }

        public ButtonCreator setButtonTextColor(@ColorInt int... color) {
            if (color == null || color.length == 0) {
                this.buttonTextColorStateList = null;
                return this;
            }
            this.buttonTextColorStateList = new ColorStateList[color.length];
            for (int i = 0; i < this.buttonTextColorStateList.length; i++) {
                this.buttonTextColorStateList[i] = new ColorStateList(new int[][]{}, new int[]{color[i]});
            }
            return this;
        }

        public ButtonCreator setButtonTextColorRes(@ColorRes int... color) {
            if (color == null || color.length == 0) {
                this.buttonTextColorStateList = null;
                return this;
            }
            this.buttonTextColorStateList = new ColorStateList[color.length];
            for (int i = 0; i < this.buttonTextColorStateList.length; i++) {
                this.buttonTextColorStateList[i] = UtilCom.getColorStateList(mContext, color[i]);
            }
            return this;
        }

        public ButtonCreator setButtonBackground(@DrawableRes int... buttonBackground) {
            this.buttonBackground = buttonBackground;
            return this;
        }


        public ButtonCreator setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
            this.onButtonClickListener = onButtonClickListener;
            return this;
        }


    }

    public DetailsDialog setDialogBackgroundRes(@DrawableRes int res) {
        containerDialog.setBackgroundResource(res);
        return this;
    }

    public DetailsDialog setTitleText(String text) {
        titleText.setText(text);
        UtilView.setViewVisibility(titleText, View.VISIBLE);
        UtilView.setViewVisibility(containerTitle, View.VISIBLE);
        return this;
    }

    public DetailsDialog setTitleTextColorRes(@ColorRes int ids) {
        int color;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = mContext.getColor(ids);
        } else {
            color = mContext.getResources().getColor(ids);

        }
        titleText.setTextColor(color);
        return this;
    }

    public DetailsDialog setTitleTextColor(@ColorInt int color) {
        titleText.setTextColor(color);
        return this;

    }

    public DetailsDialog setTitleImg(@DrawableRes int res) {
        titleImg.setImageResource(res);
        UtilView.setViewVisibility(titleImg, res == 0 ? View.GONE : View.VISIBLE);
        return this;
    }

    public DetailsDialog setTitleImg(Drawable res) {
        titleImg.setImageDrawable(res);
        UtilView.setViewVisibility(titleImg, res == null ? View.GONE : View.VISIBLE);
        return this;
    }

    public DetailsDialog setTitleImgDimenWH(@DimenRes int w, @DimenRes int h) {
//        int width = UtilDevice.px2dp(mContext, mContext.getResources().getDimension(w));
//        int height = UtilDevice.px2dp(mContext, mContext.getResources().getDimension(h));
        int width = mContext.getResources().getDimensionPixelOffset(w);
        int height = mContext.getResources().getDimensionPixelOffset(h);
        return setTitleImgWH(width, height);
    }

    public DetailsDialog setTitleImgWH(int w, int h) {
        ViewGroup.LayoutParams layoutParams = titleImg.getLayoutParams();
        layoutParams.width = w;
        layoutParams.height = h;
        titleImg.setLayoutParams(layoutParams);
        return this;
    }

    public DetailsDialog setTitleBackgroundRes(@DrawableRes int ids) {
        containerTitle.setBackgroundResource(ids);
        return this;
    }

    public DetailsDialog setTitleBackgroundColor(@ColorInt int color) {
        containerTitle.setBackgroundColor(color);
        return this;
    }

    public DetailsDialog setTitleBackgroundColorRes(@ColorRes int color) {
        int colorInt;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            colorInt = mContext.getColor(color);
        } else {
            colorInt = mContext.getResources().getColor(color);
        }
        containerTitle.setBackgroundColor(colorInt);
        return this;
    }

    public DetailsDialog setBodyBackgroundRes(@DrawableRes int ids) {
        containerBody.setBackgroundResource(ids);
        return this;
    }

    public DetailsDialog setContentText(String text) {
        contentText.setText(text);
        UtilView.setViewVisibility(contentText, text == null ? View.GONE : View.VISIBLE);
        return this;
    }

    public DetailsDialog setEditHint(String text) {
        contentEdit.setHint(text);
        UtilView.setViewVisibility(contentEdit, text == null ? View.GONE : View.VISIBLE);
        return this;
    }

    public DetailsDialog hasEdit(boolean flag) {
        UtilView.setViewVisibility(contentEdit, flag ? View.VISIBLE : View.GONE);
        return this;
    }

    public String getEditContent() {
        return contentEdit.getText().toString();
    }

    public void reset() {
//        setContentText(null);
//        setTitleImg(null);
//        showEdit(false);
//        setOnButtonClickListener(null);
//        setCancelable(true);
//        setButtons();
    }

    public interface OnButtonClickListener {
        public void onClick(View v, int position);
    }
}
