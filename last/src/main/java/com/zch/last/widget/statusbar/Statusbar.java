package com.zch.last.widget.statusbar;

import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public final class Statusbar {
    private static int DEFAULT_IMMERSIVE_FLAGS = -1;

    public static void setStatusbarColor(Window window, @ColorInt Integer statusbarColor, Boolean belowSta) {
        setColor(window, statusbarColor, null, belowSta, null);
    }

    public static void setNavigationColor(Window window, @ColorInt Integer navigationColor, Boolean belowNav) {
        setColor(window, null, navigationColor, null, belowNav);
    }

    public static void setColor(Window window, Boolean belowSta, Boolean belowNav) {
        setColor(window, null, null, belowSta, belowNav);
    }

    public static void setColor(Window window, @ColorInt Integer statusbarColor, @ColorInt Integer navigationColor) {
        setColor(window, statusbarColor, navigationColor, null, null);
    }

    /**
     * @param statusbarColor  set statusbar color
     * @param navigationColor set navigation color
     */
    public static void setColor(@Nullable Window window, @ColorInt Integer statusbarColor, @ColorInt Integer navigationColor, Boolean belowSta, Boolean belowNav) {
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Statusbar_lollipop.setColor(window, statusbarColor, navigationColor, belowSta, belowNav);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Statusbar_kitkat.setColor(window, statusbarColor, navigationColor, belowSta, belowNav);
        }
    }

    /**
     * api >= 14
     */
    private static int getDefaultImmersiveFlags() {
        if (DEFAULT_IMMERSIVE_FLAGS == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                DEFAULT_IMMERSIVE_FLAGS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                DEFAULT_IMMERSIVE_FLAGS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
        }
        return DEFAULT_IMMERSIVE_FLAGS;
    }

    /**
     * api >= 14
     */
    public static void showNavigation(Window window, boolean show) {
        if (window == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            View decorView = window.getDecorView();
            int siv = decorView.getSystemUiVisibility();
            if (show) {
                decorView.setSystemUiVisibility(siv & ~getDefaultImmersiveFlags());
            } else {
                decorView.setSystemUiVisibility(siv | getDefaultImmersiveFlags());
            }
        }
    }

    /**
     * api min is 14 version
     * 0:statusbar is visible
     * 1:navigation is visible
     *
     * @return statusbar, navigation是否可见
     */
    public static boolean[] isSystemUiVisible(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Statusbar_lollipop.isSystemUiVisible(window);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Statusbar_kitkat.isSystemUiVisible(window);
        }
        return new boolean[]{false, false};
    }

}
