package com.zch.last.widget.statusbar;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.zch.last.R;
import com.zch.last.widget.statusbar.tools.StatusbarTools;

import java.lang.reflect.Field;



/**
 * 4.4以上，5.0以下
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
final class Statusbar_kitkat {
    private static final String TAG_FAKE_STATUS_BAR_VIEW = "fakeStatusBarView";
    private static final String TAG_FAKE_NAVIGATION_BAR_VIEW = "fakeNavigationBarView";

    /**
     * @param statusbarColor  状态栏颜色
     * @param navigationColor 导航栏颜色
     * @param belowSta        内容在状态栏下方
     * @param belowNav        内容在导航栏下方
     */
    public static void setColor(@NonNull final Window window, @ColorInt Integer statusbarColor, @ColorInt Integer navigationColor, Boolean belowSta, Boolean belowNav) {
        int flags = window.getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if ((flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) != WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View decorChild = decorView.getChildAt(0);
        decorChild.setTag(R.id.tag_decor_child_statusbar_below, belowSta);
        decorChild.setTag(R.id.tag_decor_child_navigation_below, belowNav);
        decorChild.setTag(R.id.tag_decor_child_statusbar_color, statusbarColor);
        decorChild.setTag(R.id.tag_decor_child_navigation_color, navigationColor);
        //TODO
        refreshDecorLayout(window);
        View contentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        contentView.setFitsSystemWindows(false);
    }

    /**
     * 刷新 decorView
     *
     * @param window
     */
    public static void refreshDecorLayout(@NonNull final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View decorChild = decorView.getChildAt(0);
        Boolean belowStaObj = (Boolean) decorChild.getTag(R.id.tag_decor_child_statusbar_below);
        Boolean belowNavObj = (Boolean) decorChild.getTag(R.id.tag_decor_child_navigation_below);
        Integer colorStaObj = (Integer) decorChild.getTag(R.id.tag_decor_child_statusbar_color);
        Integer colorNavObj = (Integer) decorChild.getTag(R.id.tag_decor_child_navigation_color);
        //
        boolean[] systemUiVisible = isSystemUiVisible(window);
        //状态栏隐藏时一定是沉浸
        if (!systemUiVisible[0]) {
            belowStaObj = true;
        }
        //导航栏隐藏时一定是沉浸
        if (!systemUiVisible[1]) {
            belowNavObj = true;
        }
        decorChild.setTag(R.id.tag_decor_child_statusbar_below, belowStaObj);
        decorChild.setTag(R.id.tag_decor_child_navigation_below, belowNavObj);
        //
        setStatusBarView(window, colorStaObj, belowStaObj, systemUiVisible[0]);
        setNavigationBarView(window, colorNavObj, belowNavObj, systemUiVisible[1]);
    }

    /**
     * @param statusColor     状态栏颜色
     * @param below           被状态栏压住
     * @param statusbarIsShow 状态栏是否显示
     */
    private static void setStatusBarView(@NonNull Window window, @Nullable @ColorInt Integer statusColor, @Nullable Boolean below, boolean statusbarIsShow) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        //添加statusbar背景View和设置颜色
        int statusBarHeight = StatusbarTools.getStatusBarHeight(window.getContext());
        if (fakeStatusBarView == null) {
            if (statusbarIsShow) {
                fakeStatusBarView = new View(window.getContext());
                FrameLayout.LayoutParams fakeStatusbarLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 0);
                fakeStatusbarLayoutParams.gravity = Gravity.TOP;
                fakeStatusbarLayoutParams.height = statusBarHeight;
                fakeStatusBarView.setLayoutParams(fakeStatusbarLayoutParams);
                decorView.addView(fakeStatusBarView);
                fakeStatusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);
            }
        } else {
            if (!statusbarIsShow) {
                if (fakeStatusBarView.getVisibility() != View.GONE) {
                    fakeStatusBarView.setVisibility(View.GONE);
                }
            } else if (fakeStatusBarView.getVisibility() != View.VISIBLE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
        }
        if (fakeStatusBarView != null && statusColor != null) {
            fakeStatusBarView.setBackgroundColor(statusColor);
        }
        //设置decorChild 的 margin
        View decorChild = decorView.getChildAt(0);
        if (decorChild == null) {
            return;
        }
        FrameLayout.LayoutParams decorChildLayoutParams = (FrameLayout.LayoutParams) decorChild.getLayoutParams();
        if (below != null) {
            if (below && decorChildLayoutParams.topMargin != 0) {
                decorChildLayoutParams.topMargin = 0;
                decorChild.setLayoutParams(decorChildLayoutParams);
            } else if (!below && decorChildLayoutParams.topMargin != statusBarHeight) {
                decorChildLayoutParams.topMargin = statusBarHeight;
                decorChild.setLayoutParams(decorChildLayoutParams);
            }
        }

    }

    private static void setNavigationBarView(@NonNull Window window, @Nullable @ColorInt Integer navigaColor, @Nullable Boolean below, boolean navigaIsShow) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeNavigationBarView = decorView.findViewWithTag(TAG_FAKE_NAVIGATION_BAR_VIEW);
        //添加statusbar背景View和设置颜色
        int navigationHeight = 0;
        if (fakeNavigationBarView == null) {
            if (navigaIsShow) {
                navigationHeight = StatusbarTools.getNavigationHeight(window.getContext());
                fakeNavigationBarView = new View(window.getContext());
                FrameLayout.LayoutParams fakeNavigationbarLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 0);
                fakeNavigationbarLayoutParams.gravity = Gravity.BOTTOM;
                fakeNavigationbarLayoutParams.height = navigationHeight;
                fakeNavigationBarView.setLayoutParams(fakeNavigationbarLayoutParams);
                decorView.addView(fakeNavigationBarView);
                fakeNavigationBarView.setTag(TAG_FAKE_NAVIGATION_BAR_VIEW);
            }
        } else {
            if (!navigaIsShow) {
                if (fakeNavigationBarView.getVisibility() != View.GONE) {
                    fakeNavigationBarView.setVisibility(View.GONE);
                }
            } else if (fakeNavigationBarView.getVisibility() != View.VISIBLE) {
                fakeNavigationBarView.setVisibility(View.VISIBLE);
            }
        }
        if (fakeNavigationBarView != null && navigaColor != null) {
            fakeNavigationBarView.setBackgroundColor(navigaColor);
        }
        //设置decorChild 的 margin
        View decorChild = decorView.getChildAt(0);
        if (decorChild == null) {
            return;
        }
        FrameLayout.LayoutParams decorChildLayoutParams = (FrameLayout.LayoutParams) decorChild.getLayoutParams();
        if (below != null) {
            if (below) {
                if (decorChildLayoutParams.bottomMargin != 0) {
                    decorChildLayoutParams.bottomMargin = 0;
                    decorChild.setLayoutParams(decorChildLayoutParams);
                }
            } else {
                if (navigationHeight == 0) {
                    navigationHeight = StatusbarTools.getNavigationHeight(window.getContext());
                }
                if (decorChildLayoutParams.bottomMargin != navigationHeight) {
                    decorChildLayoutParams.bottomMargin = navigationHeight;
                    decorChild.setLayoutParams(decorChildLayoutParams);
                }
            }
        }

    }

    public static boolean[] isSystemUiVisible(Window window) {
        boolean[] result = new boolean[]{false, false};
        if (window == null) {
            return result;
        }
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (attributes != null) {
            result[0] = (attributes.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN;
            //
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            result[1] = (((attributes.systemUiVisibility | decorView.getWindowSystemUiVisibility()) &
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                result[1] = result[1] && (attributes.flags & WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) != 0;
            }
        }
        //
        Object decorViewObj = window.getDecorView();
        Class<?> decorViewClass = decorViewObj.getClass();
        int mLastBottomInset = 0, mLastRightInset = 0, mLastLeftInset = 0;
        try {
            Field mLastBottomInsetField = decorViewClass.getDeclaredField("mLastBottomInset");
            mLastBottomInsetField.setAccessible(true);
            mLastBottomInset = mLastBottomInsetField.getInt(decorViewObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Field mLastRightInsetField = decorViewClass.getDeclaredField("mLastRightInset");
            mLastRightInsetField.setAccessible(true);
            mLastRightInset = mLastRightInsetField.getInt(decorViewObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Field mLastLeftInsetField = decorViewClass.getDeclaredField("mLastLeftInset");
            mLastLeftInsetField.setAccessible(true);
            mLastLeftInset = mLastLeftInsetField.getInt(decorViewObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isNavBarToRightEdge = mLastBottomInset == 0 && mLastRightInset > 0;
        int size = isNavBarToRightEdge ? mLastRightInset : (mLastBottomInset == 0 && mLastLeftInset > 0 ? mLastLeftInset : mLastBottomInset);
        result[1] = result[1] && size > 0;
        return result;
    }
    //----------------------------------------------------------------------------------------------

    public static void setFullscreen(@NonNull Window window, boolean isFull) {
        if (isFull) {
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            //隐藏手动添加的状态栏背景
            View fakeStatusBarView = decorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
            if (fakeStatusBarView != null && fakeStatusBarView.getVisibility() == View.VISIBLE) {
                fakeStatusBarView.setVisibility(View.GONE);
            }
            View child = decorView.getChildAt(0);
            if (child == null) {
                return;
            }
            //恢复布局margin
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) child.getLayoutParams();
            if (layoutParams.topMargin != 0) {
                layoutParams.topMargin = 0;
                child.setLayoutParams(layoutParams);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            //显示手动添加的状态栏背景
            View fakeStatusBarView = decorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
            if (fakeStatusBarView != null && fakeStatusBarView.getVisibility() != View.VISIBLE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

}
