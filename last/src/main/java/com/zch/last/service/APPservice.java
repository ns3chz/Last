package com.zch.last.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class APPservice extends Service {
    private static APPservice apPservice;
    public static APPservice get(Context context) {
        if (apPservice != null) {
            return    apPservice;
        }
        return new APPservice();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    public void showAlert() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = -1;
        params.width = -1;
        params.format = 1;
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        TextView textView = new TextView(this);
        textView.setText("程序崩溃，即将退出");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(50);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        windowManager.addView(textView, params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
