package com.zch.last.widget.statusbar;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.zch.last.R;
import com.zch.last.widget.statusbar.tools.StatusbarTools;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 5.0以上
 * 通过padding、margin来改变上下高度
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
final class Statusbar_lollipop {

    /**
     * @param statusbarColor  状态栏颜色
     * @param navigationColor 导航栏颜色
     * @param belowSta        内容在状态栏下方
     * @param belowNav        内容在导航栏下方
     */
    public static void setColor(@NonNull Window window, @ColorInt Integer statusbarColor, @ColorInt Integer navigationColor, Boolean belowSta, Boolean belowNav) {
        int flags = window.getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) !=
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if ((flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) ==
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if ((flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) ==
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        if (statusbarColor != null && statusbarColor != window.getStatusBarColor()) {
            window.setStatusBarColor(statusbarColor);
        }
        if (navigationColor != null && navigationColor != window.getNavigationBarColor()) {
            window.setNavigationBarColor(navigationColor);
        }

        //延时重复检查decorChildView的paddingTop,paddingBot,marginTop,marginBot为0
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        final View decorChild = decorView.getChildAt(0);
        if (belowSta != null) {
            decorChild.setTag(R.id.tag_decor_child_statusbar_below, belowSta);
        }
        if (belowNav != null) {
            decorChild.setTag(R.id.tag_decor_child_navigation_below, belowNav);
        }
        refreshDecorChildLayout(window);
    }

    /**
     * 刷新statusbar、navigationbar , 延迟重复检查设置 decorChild 的padding、margin
     * <ul>
     * <li>在设置 decorChild Tag 后</li>
     * <li>在使用 setStatusBarColor 后</li>
     * <li>在使用 setNavigationBarColor 后</li>
     * </ul>
     *
     * @param window
     */
    public static void refreshDecorChildLayout(@NonNull final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        final View decorChild = decorView.getChildAt(0);
        Object checkRetryObj = decorChild.getTag(R.id.tag_decor_child_check_retry);
        Disposable checkRetry;
        if (checkRetryObj != null && checkRetryObj instanceof Disposable) {
            checkRetry = (Disposable) checkRetryObj;
            if (!checkRetry.isDisposed()) {
                checkRetry.dispose();
            }
        }
        Boolean belowSta = (Boolean) decorChild.getTag(R.id.tag_decor_child_statusbar_below);
        Boolean belowNav = (Boolean) decorChild.getTag(R.id.tag_decor_child_navigation_below);
//        //全屏切换时状态栏会被影响而导航栏不会，所以全屏时belowSta不能为false,会留出状态栏高度的空白，而belowNav随意
        boolean[] systemUiVisible = isSystemUiVisible(window);
        if (!systemUiVisible[0]) {
            belowSta = true;
        }
        if (!systemUiVisible[1]) {
            belowNav = true;
        }
        decorChild.setTag(R.id.tag_decor_child_statusbar_below, belowSta);
        decorChild.setTag(R.id.tag_decor_child_navigation_below, belowNav);
        /*  使用setStatusBarColor,setNavigationBarColor方法后，立即设置padding或设置margin没有作用，
         *  需要延时设置
         */
        final Boolean belowStaFin = belowSta;
        final Boolean belowNavFin = belowNav;
        checkRetry = Observable.interval(50, 50, TimeUnit.MILLISECONDS)//检查间隔
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(10)//重复检查次数
                .subscribe(new Consumer<Long>() {
                    private boolean refreshPadding = false;

                    @Override
                    public void accept(Long aLong) throws Exception {
                        refreshPadding = false;
                        int paddingTop = decorChild.getPaddingTop();
                        int paddingBottom = decorChild.getPaddingBottom();

                        //statusbar
                        if (belowStaFin != null) {
                            if (belowStaFin && paddingTop != 0) {
                                paddingTop = 0;
                                refreshPadding = true;
                            } else if (!belowStaFin) {
                                int statusBarHeight = StatusbarTools.getStatusBarHeight(window.getContext());
                                if (paddingTop != statusBarHeight) {
                                    paddingTop = statusBarHeight;
                                    refreshPadding = true;
                                }
                            }
                        }
                        //navigation
                        if (belowNavFin != null) {
                            if (belowNavFin && paddingBottom != 0) {
                                paddingBottom = 0;
                                refreshPadding = true;
                            } else if (!belowNavFin) {
                                int navigationHeight = StatusbarTools.getNavigationHeight(window.getContext());
                                if (paddingBottom != navigationHeight) {
                                    paddingBottom = navigationHeight;
                                    refreshPadding = true;
                                }
                            }
                        }
                        if (refreshPadding) {
                            decorChild.setPadding(decorChild.getPaddingLeft(), paddingTop, decorChild.getPaddingRight(), paddingBottom);
                        }
                        //setMargin
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) decorChild.getLayoutParams();
                        if (layoutParams.topMargin != 0 || layoutParams.bottomMargin != 0) {
                            layoutParams.topMargin = 0;
                            layoutParams.bottomMargin = 0;
                            decorChild.setLayoutParams(layoutParams);
                        }
//                        if (!decorChild.getFitsSystemWindows()) {
//                            decorChild.setFitsSystemWindows(false);
//                        }

                    }
                });
        decorChild.setTag(R.id.tag_decor_child_check_retry, checkRetry);
    }

    /**
     * 在这里不能用内容高度和屏幕真实高度作对比来判断。
     * 这里只适用于21以后的版本，方法是从DecorView源码中来的，
     * 测试了模拟器21版本，和我自己手机Android 8.1.0都是有效的
     * api min is 21 version
     * 0:statusbar is visible
     * 1:navigation is visible
     *
     * @return statusbar, navigation是否可见
     */
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
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) && (attributes.flags & WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) != 0;
        }
        //
        Object decorViewObj = window.getDecorView();
        Class<?> clazz = decorViewObj.getClass();
        int mLastBottomInset = 0, mLastRightInset = 0, mLastLeftInset = 0;
        try {
            Field mLastBottomInsetField = clazz.getDeclaredField("mLastBottomInset");
            mLastBottomInsetField.setAccessible(true);
            mLastBottomInset = mLastBottomInsetField.getInt(decorViewObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Field mLastRightInsetField = clazz.getDeclaredField("mLastRightInset");
            mLastRightInsetField.setAccessible(true);
            mLastRightInset = mLastRightInsetField.getInt(decorViewObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Field mLastLeftInsetField = clazz.getDeclaredField("mLastLeftInset");
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
    //---------------------------------------------------------------------------------------

    /**
     * 主界面是否被状态栏和导航栏覆盖
     *
     * @return int[], int[0] = statusbar below ,int[1] = navigation below;
     */
    public static boolean[] isContentBelow(@NonNull Window window) {
        boolean[] result = new boolean[]{false, false};
        boolean fullscreen = StatusbarTools.isFullscreen(window);
        if (fullscreen) return result;
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View decorChild = decorView.getChildAt(0);
        Object statusBelow = decorChild.getTag(R.id.tag_decor_child_statusbar_below);
        Object navigationBelow = decorChild.getTag(R.id.tag_decor_child_navigation_below);
        if (statusBelow != null && statusBelow instanceof Boolean) {
            result[0] = (boolean) statusBelow;
        }
        if (navigationBelow != null && navigationBelow instanceof Boolean) {
            result[1] = (boolean) navigationBelow;
        }
        return result;
    }


    public static void setFullscreen(@NonNull Window window, boolean isFull) {
        if (isFull) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        refreshDecorChildLayout(window);
    }

    public static boolean isFullscreen(@NonNull Window window) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        return attributes != null && (attributes.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) ==
                WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    //--------------------------------------------------------------------------------------------

}

