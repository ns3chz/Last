package com.zch.last.vmodel;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.R;
import com.zch.last.databinding.LayoutAlertDefaultAskBinding;
import com.zch.last.model.clickview.TextViewClick;
import com.zch.last.utils.UtilCom;

public class ViewModelDefaultDialogAsk extends BaseLayoutModel<LayoutAlertDefaultAskBinding> {
    @ColorInt
    private int lineColor;
    /**
     * 是否应该刷新按钮间的中竖线UI
     */
    private boolean shouldRefreshButtonLines = false;
    @Nullable
    public TextViewClick[] btnViewClicks;

    public ViewModelDefaultDialogAsk(@NonNull Context context) {
        super(context, R.layout.layout_alert_default_ask);
        lineColor = UtilCom.getColor(context, R.color.gray_line);
    }


    @Override
    public void init(@NonNull View root) {

    }

    @Override
    public void initListener(@NonNull View root) {

    }

    @Override
    public void initData(@NonNull View root) {

    }

    public void setContent(String content) {
        dataBinding.tvContent.setText(content);
    }

    public void setLineColor(@ColorInt int color) {
        lineColor = color;
        dataBinding.lineTitle.setBackgroundColor(color);
        dataBinding.lineContent.setBackgroundColor(color);
    }

    public void setTitle(String text) {
        dataBinding.tvTitle.setText(text);
        if (text == null || text.length() == 0) {
            dataBinding.tvTitle.setVisibility(View.GONE);
            dataBinding.lineTitle.setVisibility(View.GONE);
        } else {
            dataBinding.tvTitle.setVisibility(View.VISIBLE);
            dataBinding.lineTitle.setVisibility(View.VISIBLE);
        }
    }

    public void setButtons(TextViewClick... itemClicks) {
        btnViewClicks = itemClicks;
        dataBinding.containerBtn.removeAllViews();
        if (itemClicks == null || itemClicks.length == 0) {
            dataBinding.containerBtn.setVisibility(View.GONE);
            dataBinding.lineContent.setVisibility(View.GONE);
        } else {
            shouldRefreshButtonLines = true;
            dataBinding.containerBtn.setVisibility(View.VISIBLE);
            dataBinding.lineContent.setVisibility(View.VISIBLE);
            TextViewClick itemClick;

            //layoutParams参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            layoutParams.leftMargin = 2;
            layoutParams.rightMargin = 2;
            layoutParams.topMargin = 2;
            layoutParams.bottomMargin = 2;

            for (int i = 0; i < itemClicks.length; i++) {
                itemClick = itemClicks[i];
                itemClick.index = i;

                TextView textView = (TextView) layoutInflater.inflate(R.layout.view_text_small, dataBinding.containerBtn, false);
                textView.setText(itemClick.text);
                textView.setLayoutParams(layoutParams);
                dataBinding.containerBtn.addView(textView);
                itemClick.prepare(textView);
                //设置点击
                textView.setOnClickListener(itemClick);
            }
        }
    }

    /**
     * 刷新btn line
     */
    public void refreshButtonLines() {
        if (!shouldRefreshButtonLines) return;
        if (dataBinding.containerBtn.getVisibility() != View.VISIBLE) return;
        int childCount = dataBinding.containerBtn.getChildCount();
        if (childCount < 2) return;
        int maxHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childAt = dataBinding.containerBtn.getChildAt(i);
            int height = childAt.getHeight();
            if (height > maxHeight) {
                maxHeight = height;
            }
        }
        //设置中竖线
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1, maxHeight);
        layoutParams.topMargin = 4;
        layoutParams.bottomMargin = 4;

        layoutParams.height -= layoutParams.topMargin + layoutParams.bottomMargin;
        for (int i = 0; i < childCount - 1; i++) {
            View view = layoutInflater.inflate(R.layout.view, dataBinding.containerBtn, false);
            view.setBackgroundColor(lineColor);
            view.setLayoutParams(layoutParams);
            dataBinding.containerBtn.addView(view, 2 * i + 1);
        }

        shouldRefreshButtonLines = false;
    }

}
