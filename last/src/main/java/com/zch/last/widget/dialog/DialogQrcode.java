package com.zch.last.widget.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.zch.last.R;
import com.zch.last.model.QrCodeBuilder;
import com.zch.last.utils.UtilObject;
import com.zch.last.utils.UtilThread;
import com.zch.last.utils.UtilView;
import com.zch.last.utils.UtileQrcode;
import com.zch.last.view.scale.ScaleImageView;

public class DialogQrcode extends BaseAlertDialog {

    private TextView tvTitle;
    private ScaleImageView ivQrcode;
    private String titleText;
    private String qrcodeText;
    private String curQrcodeText;

    public DialogQrcode(Context context) {
        super(context);
    }

    @Override
    public int setRes() {
        return R.layout.dialog_layout_qrcode;
    }

    @Override
    public void buildParam(@NonNull AlertDialog.Builder alertDialogBuilder, @NonNull AlertDialog alertDialog, @NonNull View parent) {
        initView(parent);
    }

    @Override
    public void resetWindowParam(@NonNull Window window, @NonNull WindowManager windowManager, @NonNull WindowManager.LayoutParams windowParam) {
        super.resetWindowParam(window, windowManager, windowParam);
        windowParam.x = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParam.y = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private void initView(View parent) {
        tvTitle = parent.findViewById(R.id.tv_title);
        ivQrcode = parent.findViewById(R.id.iv_qrcode);
        int h = (int) (0.5f * displaySize.y);
        ViewGroup.LayoutParams layoutParams = ivQrcode.getLayoutParams();
        layoutParams.width = h;
        layoutParams.height = h;
        ivQrcode.setLayoutParams(layoutParams);
    }

    @Override
    public void show() {
        super.show();
        checkTitleShow();
        refreshQrcode();
    }


    private void checkTitleShow() {
        boolean showTitle = titleText != null && titleText.length() != 0;
        UtilView.setText(tvTitle, titleText);
        UtilView.setViewVisibility(tvTitle, showTitle ? View.VISIBLE : View.GONE);
    }

    private void refreshQrcode() {
        if (UtilObject.equals(qrcodeText, curQrcodeText)) {
            return;
        }
        qrcodeText = curQrcodeText;
        curQrcodeText = null;
        UtilThread.runOnUiThread(500, new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = UtileQrcode.encodeQRCode(qrcodeText,
                        QrCodeBuilder.newBuilder()
                                .setWith(ivQrcode.getWidth())
                                .setHeight(ivQrcode.getHeight())
                                .setPadding(20));
                UtilView.setBitmap(ivQrcode, bitmap);
            }
        });
    }

    public void setTitleText(String text) {
        this.titleText = text;
    }

    public void setQrcodeText(String text) {
        this.curQrcodeText = text;
    }
}
