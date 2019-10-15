package com.zch.last.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

import com.zch.last.widget.statusbar.Statusbar;


/**
 * 需要设置statusbar颜色时，必须添加id为status_bar的view布局
 * Created by Administrator on 2017/6/20.
 */

public abstract class BaseAlertDialog<D extends BaseAlertDialog> {
    public Context mContext;
    protected AlertDialog.Builder dialogBuilder;
    public AlertDialog alertDialog;
    protected LayoutInflater mLayoutInflater;
    protected boolean shouldReloadWindowParam = true;//重新show时，重新加载一遍屏幕参数，若参数有改变需调用notifyWindowParamChanged()设置为true
    protected boolean shouldReloadWindowScreen = true;//重新计算屏幕宽高
    protected float SCREEN_DIM = 0.4f;//window背景的透明度，0~100%，默认0.4f
    //    protected float DIALOG_WIDTH = 0.8f;//dialog宽度，0~100%，默认0.8f
//    protected float DIALOG_HEIGHT = WindowManager.LayoutParams.WRAP_CONTENT;
    protected float DEF_WIDTH = WindowManager.LayoutParams.WRAP_CONTENT;
    protected float DEF_HEIGHT = WindowManager.LayoutParams.WRAP_CONTENT;
    public float settedWith = DEF_WIDTH;
    public float settedHeight = DEF_HEIGHT;
    //    protected View statusBarView;
    //
    private int statusBarColor;
    protected Point displaySize;

    @LayoutRes
    private int layoutRes;

    public BaseAlertDialog(Context context) {
        this(context, -1);//颜色为-1时不设置状态栏
    }

    public BaseAlertDialog(@LayoutRes int res, Context context) {
        this(context, Color.TRANSPARENT, res);
    }

    /**
     * @param color 状态栏颜色
     */
    public BaseAlertDialog(Context context, @ColorInt int color) {
        this(context, color, 0);
    }

    public BaseAlertDialog(Context context, @ColorInt int color, @LayoutRes int res) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
        this.statusBarColor = color;
        this.layoutRes = res;
        build();
        create();
    }

    /**
     * 创建Builder
     */
    private void build() {
        this.dialogBuilder = new AlertDialog.Builder(this.mContext, setStyle());
    }

    /**
     * 创建AlertDialog
     */
    private void create() {
        this.alertDialog = this.dialogBuilder.create();
        if (this.alertDialog == null) return;
        //根View
        Window window = getWindow();
        if (window == null) return;
        reloadScreenParams();
        View rootView = setView();
        if (rootView == null) {
            int res = this.layoutRes == 0 ? setRes() : this.layoutRes;
            if (res != 0) {
                rootView = this.mLayoutInflater.inflate(res, null);
            }
        }
//设置状态栏颜色
        if (statusBarColor != -1) {
            Statusbar.setColor(window, statusBarColor, Color.BLACK);
        }
        //
        if (rootView != null) {
            buildParam(this.dialogBuilder, this.alertDialog, rootView);//向外提供builder、dialog与rootview
            this.alertDialog.setView(rootView);//填充view'

        } else {
            Log.e(getClass().getName(), "rootView is NULL .");
        }
//        this.alertDialog.setContentView(rootView);

    }

    /**
     * 设置dialog参数
     */
    private void resetWindowParam() {
//        if (this.shouldReloadWindowParam) {
//            this.shouldReloadWindowParam = false;
        Window window = this.alertDialog.getWindow();
        if (window == null) return;
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        WindowManager windowManager = window.getWindowManager();
        if (layoutParams == null || windowManager == null) return;
        this.resetWindowParam(window, windowManager, layoutParams);//设置屏幕参数
        window.setAttributes(layoutParams);//将修改重新设置到屏幕
//        }
    }

    private void reloadScreenParams() {
//        if (this.shouldReloadWindowScreen) {
        WindowManager windowManager = getWindowManager();
        if (windowManager != null) {
//            this.shouldReloadWindowScreen = false;
            displaySize = new Point();
            windowManager.getDefaultDisplay().getSize(displaySize);
        }
//        }
    }

    /**
     * 显示dialog
     */
    public void show() {
        if (this.alertDialog == null) return;
        reloadScreenParams();
        this.showBefore();
        this.alertDialog.show();
        //重新加载屏幕参数
        resetWindowParam();
    }

    /**
     * 关闭dialog
     */
    public void dissmiss() {
        if (this.alertDialog == null) return;
        this.alertDialog.dismiss();
    }

    /**
     * 重新显示会再次加载屏幕参数，走setWindowParam(windowManager,windowParam);
     */
    public void notifyWindowParamChanged() {
        this.shouldReloadWindowParam = true;
    }

    /**
     * 重新计算屏幕宽度
     */
    public void notifyWindowScreenChanged() {
        this.shouldReloadWindowScreen = true;
    }


    /**
     * dialog的风格
     *
     * @return style id
     */
    @StyleRes
    protected int setStyle() {
        //主要是noActionBar属性，若没有这个属性，设置dialog位置为top时会留出actionbar的距离
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return android.R.style.Theme_Material_Dialog_NoActionBar;
//        } else {
//            return android.R.style.Theme_Holo_NoActionBar_Fullscreen;
        return android.R.style.Theme_DeviceDefault_Dialog_NoActionBar;
//        }
    }

    /**
     * 调用show方法前做的操作
     */
    public void showBefore() {

    }

    /**
     * 布局id
     *
     * @return layout id
     */
    @LayoutRes
    public abstract int setRes();

    public View setView() {
        return null;
    }

    /**
     * 添加布局前，设置参数
     *
     * @param alertDialogBuilder builder
     * @param alertDialog        dialog
     * @param parent             dialog根view
     */
    public abstract void buildParam(@NonNull AlertDialog.Builder alertDialogBuilder, @NonNull AlertDialog alertDialog, @NonNull View parent);

    /**
     * 管理屏幕
     *
     * @param windowManager 屏幕管理
     * @param windowParam   屏幕参数对象
     */
    public void resetWindowParam(@NonNull Window window, @NonNull WindowManager windowManager, @NonNull WindowManager.LayoutParams windowParam) {
        //
        windowParam.gravity = Gravity.CENTER;
//         window.setDimAmount(SCREEN_DIM);//设置dialog外背景的透明度
        windowParam.dimAmount = SCREEN_DIM;//亦可设置dialog外背景的透明度
        //
        window.setBackgroundDrawableResource(android.R.color.transparent);  //设置背景为透明，否则有黑底
    }

    //----------------------------------------------setter-------------------------------------------------

    /**
     * @param flag 点击弹框外隐藏
     */
    public final D setCancelable(boolean flag) {
        if (alertDialog != null) {
            alertDialog.setCancelable(flag);
        }
        return (D) this;
    }

    /**
     * @param SCREEN_DIM //内容外的屏幕透明度，0~100%，默认0.4f
     */
    public final D setScreenDim(@FloatRange(from = 0f, to = 1f) float SCREEN_DIM) {
        this.SCREEN_DIM = SCREEN_DIM;
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = getWindowLayoutParams();
        if (window != null && layoutParams != null) {
            layoutParams.dimAmount = SCREEN_DIM;
        }
        return (D) this;
    }

    /**
     * @param width  //dialog宽度,0~1时为百分比
     * @param height //dialog高度
     */
    public final D setDialogWH(float width, float height) {
        if (width < -2 || height < -2) {
            return (D) this;
        }
        settedWith = width;
        settedHeight = height;
        Window window = getWindow();
        WindowManager windowManager = getWindowManager();
        WindowManager.LayoutParams layoutParams = getWindowLayoutParams();
        if (window != null && layoutParams != null && windowManager != null) {
            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            //宽度
            if (width > 1 || width == WindowManager.LayoutParams.WRAP_CONTENT ||
                    width == WindowManager.LayoutParams.MATCH_PARENT) {
                layoutParams.width = (int) width;
            } else if (width >= 0 && width <= 1) {
                layoutParams.width = (int) (width * point.x);
            }
            //高度
            if (height > 1 || height == WindowManager.LayoutParams.WRAP_CONTENT ||
                    height == WindowManager.LayoutParams.MATCH_PARENT) {
                layoutParams.height = (int) height;
            } else if (height >= 0 && height <= 1) {
                layoutParams.height = (int) (height * point.y);
            }
        }
        return (D) this;
    }

    public void refreshWindowLayoutParams() {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = getWindowLayoutParams();
        if (window != null && layoutParams != null) {
            window.setAttributes(layoutParams);
        }
    }

    //----------------------------------------------getter-------------------------------------------------


    /**
     *
     */
    @Nullable
    public Window getWindow() {
        if (alertDialog != null) {
            return alertDialog.getWindow();
        }
        return null;
    }

    /**
     *
     */
    @Nullable
    public WindowManager getWindowManager() {
        Window window = getWindow();
        if (window != null) {
            return window.getWindowManager();
        }
        return null;
    }

    /**
     *
     */
    @Nullable
    public WindowManager.LayoutParams getWindowLayoutParams() {
        Window window = getWindow();
        if (window != null) {
            return window.getAttributes();
        }
        return null;
    }

    /**
     * 是否显示
     */
    public boolean isShown() {
        if (alertDialog != null) {
            return alertDialog.isShowing();
        }
        return false;
    }
    //------------------------------------Tools----------------------------------------------------

    private DisplayMetrics displayMetrics;

    @Nullable
    private DisplayMetrics getDisplayMetrics() {
        if (mContext == null) return null;
        if (displayMetrics == null) {
            displayMetrics = mContext.getResources().getDisplayMetrics();
        }
        return displayMetrics;
    }

    //---------------------------listener--------------------------------------------------

    /**
     * dialog消失监听
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        if (alertDialog != null) {
            alertDialog.setOnDismissListener(onDismissListener);
        }
    }

    /**
     * 确认监听
     */
    public interface OnDialogConfirmListener {
        void confirm();
    }

    public OnDialogConfirmListener onDialogConfirmListener;

    public void setOnDialogConfirmListener(OnDialogConfirmListener listener) {
        this.onDialogConfirmListener = listener;
    }

    /**
     * 取消监听
     */
    public interface OnDialogCancelListener {
        void cancel();
    }

    public OnDialogCancelListener onDialogCancelListener;

    public void setOnDialogCancelListener(OnDialogCancelListener listener) {
        onDialogCancelListener = listener;
    }

    public void recycle() {

    }
}
