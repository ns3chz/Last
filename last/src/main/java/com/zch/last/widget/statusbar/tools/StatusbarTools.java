package com.zch.last.widget.statusbar.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class StatusbarTools {

    /**
     * return statusBar's Height in pixels
     */
    public static int getStatusBarHeight(@Nullable Context context) {
        if (context == null) return 0;
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId != 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    /**
     * @return 导航栏高度
     */
    public static int getNavigationHeight(@Nullable Context context) {
        if (context == null) return 0;
        int result = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean isActivityCanUse(@Nullable Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isDestroyed()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isFullscreen(@Nullable Window window) {
        if (window == null) return false;
        return (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) ==
                WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

}
