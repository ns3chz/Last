package com.zch.last.utils;

import android.Manifest;
import android.content.Context;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.zch.last.model.clickview.TextViewClick;
import com.zch.last.widget.alert.GlobalAlertAsk;

public class UtilAlert {

    public static boolean checkGlobalAlertPermission(Context context) {
        if (context == null) return false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            return UtilPermission.hasPermission(context, Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
    }

    public static boolean showGlobalAlert(Context context, View view, WindowManager.LayoutParams params) {
        if (context == null) return false;
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.addView(view, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static GlobalAlertAsk globalAlertAsk;

    @Nullable
    public static GlobalAlertAsk showGlobalAlertDefaultAsk(Context context,String title, String content, boolean cancelable, TextViewClick... viewClicks) {
        if (context == null) return null;
        if (globalAlertAsk == null) {
            globalAlertAsk = new GlobalAlertAsk(context);
        }
        globalAlertAsk.viewModel.setTitle(title);
        globalAlertAsk.viewModel.setContent(content);
        globalAlertAsk.viewModel.setButtons(viewClicks);
        globalAlertAsk.cancelable = cancelable;
        globalAlertAsk.show();
        return globalAlertAsk;
    }
}
