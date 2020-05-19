package com.zch.last.widget.alert;


import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.zch.last.utils.UtilReflect;
import com.zch.last.utils.UtilThread;
import com.zch.last.vmodel.BaseViewModel;
import com.zch.last.widget.lifecycle.Java8Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @param <VM> 显示在应用上层的弹框，需要权限
 */
public abstract class GlobalAlert<VM extends BaseViewModel> {

    @Nullable
    private WindowManager.LayoutParams windowManagerLayoutParams;
    @Nullable
    private WindowManager windowManager;
    @NonNull
    public final VM viewModel;
    @Nullable
    private Set<ViewObserverRunnable> globalLayoutRunnables;
    @Nullable
    private View.OnClickListener onCancelListener;
    @Nullable
    private LifecycleObserver lifecycleObserver;
    public boolean cancelable = true;
    /**
     * 是否已经显示
     */
    private boolean isShown = false;
    /**
     * 是否要设置window的layoutParams
     */
    private boolean shouldResetWindowParams = true;

    /**
     * 系统窗口
     */
    @Nullable
    private Window phoneWindow;
    /**
     * 系统窗口的layoutParams，不可去修改参数
     */
    @Nullable
    private WindowManager.LayoutParams phoneWindowLayoutParams;

    /**
     * 可用随意修改参数
     */
    @Nullable
    private WindowManager.LayoutParams myWindowLayoutParams;

    public GlobalAlert(@NonNull VM viewModel) {
        this.viewModel = viewModel;
        setLifecycleObserver(true);
    }

    /**
     * 应当先判断权限{@link com.zch.last.utils.UtilAlert#checkGlobalAlertPermission(Context)}
     * 显示弹框
     */
    public void show() {

        try {

            if (windowManagerLayoutParams == null) {
                windowManagerLayoutParams = new WindowManager.LayoutParams();
                windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                windowManagerLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                windowManagerLayoutParams.format = PixelFormat.RGBA_8888;
                windowManagerLayoutParams.gravity = Gravity.CENTER;
//                windowManagerLayoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    windowManagerLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    windowManagerLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                } else {
//                    windowManagerLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                    windowManagerLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                }
            }

            if (windowManager == null) {
                windowManager = (WindowManager) viewModel.context.getSystemService(Context.WINDOW_SERVICE);
            }
            //设置window Params
            resetWindow(windowManager);

            handleViewParams(windowManagerLayoutParams);

            View root = viewModel.dataBinding.getRoot();
            root.setOnClickListener(rootViewClickListener);

            if (!isShown) {
                windowManager.addView(root, windowManagerLayoutParams);
            }
            isShown = true;
            //
            viewTreeObserver();

        } catch (Exception e) {
            e.printStackTrace();
            isShown = true;
            dissmiss();
        }
    }

    /**
     * 设置window参数
     */
    private void resetWindow(@NonNull WindowManager windowManager) {
        if (shouldResetWindowParams) {
            if (phoneWindow == null) {
                //Android 9.0以下的版本才能通过反射找到mParentWindow
                phoneWindow = UtilReflect.findField(windowManager, new UtilReflect.SearchComparable<Field>(true) {
                    @Override
                    public boolean findOut(@NonNull Field obj) {
                        int modifiers = obj.getModifiers();
                        if ((modifiers & Modifier.PRIVATE) == 0) return false;
                        if ("mParentWindow".equalsIgnoreCase(obj.getName())) {
                            return true;
                        }
                        Class<?> type = obj.getType();
                        return type == Window.class;
                    }
                });
            }
            if (phoneWindow != null) {
                WindowManager.LayoutParams params = phoneWindow.getAttributes();

                //复制一份phoneWindow参数（不能直接引用），以便恢复系统窗口的状态
                if (phoneWindowLayoutParams == null) {
                    phoneWindowLayoutParams = new WindowManager.LayoutParams();
                    phoneWindowLayoutParams.copyFrom(params);
                }
                if (myWindowLayoutParams == null) {
                    myWindowLayoutParams = new WindowManager.LayoutParams();
                    myWindowLayoutParams.copyFrom(params);
                }
                //操作windowLayout参数
                handleWindowLayoutParams(myWindowLayoutParams);
            }
        }
        //设置成自己的参数
        if (phoneWindow != null) {
            if (myWindowLayoutParams != null) {
                if (UtilThread.isMainthread()) {
                    phoneWindow.setAttributes(myWindowLayoutParams);
                } else {
                    phoneWindow.getDecorView().post(new Runnable() {
                        @Override
                        public void run() {
                            phoneWindow.setAttributes(myWindowLayoutParams);
                        }
                    });
                }
            }
        }
        shouldResetWindowParams = false;
    }

    /**
     * 观察view完成渲染
     */
    private void viewTreeObserver() {
        if (globalLayoutRunnables != null) {
            for (final ViewObserverRunnable runnable : globalLayoutRunnables) {
                runnable.VIEW.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            runnable.VIEW.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            runnable.VIEW.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        try {
                            runnable.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * 关闭弹框
     */
    public void dissmiss() {
        if (isShown) {
            try {
                if (windowManager == null) {
                    windowManager = (WindowManager) viewModel.context.getSystemService(Context.WINDOW_SERVICE);
                }
                windowManager.removeView(viewModel.dataBinding.getRoot());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (phoneWindow != null) {
                if (phoneWindowLayoutParams != null) {
                    phoneWindow.setAttributes(phoneWindowLayoutParams);
                }
            }
        }
        isShown = false;
    }

    /**
     * context生命周期观察
     */
    private class LifecycleObserver extends Java8Observer {
        private boolean shouldAutoShow = false;

        @Override
        public void onStart(@NonNull LifecycleOwner owner) {
            super.onStart(owner);
            if (shouldAutoShow && !isShown) {
                shouldAutoShow = false;
                show();
            }
        }

        @Override
        public void onStop(@NonNull LifecycleOwner owner) {
            super.onStop(owner);
            shouldAutoShow = isShown;
            if (isShown) {
                dissmiss();
            }
        }

    }

    /**
     * @param set 设置是否跟随生命周期显示与隐藏
     */
    public void setLifecycleObserver(boolean set) {
        if (viewModel.context instanceof LifecycleOwner) {
            if (set) {
                if (lifecycleObserver == null) {
                    lifecycleObserver = new LifecycleObserver();
                    ((LifecycleOwner) viewModel.context).getLifecycle().addObserver(lifecycleObserver);
                }
            } else {
                if (lifecycleObserver != null) {
                    ((LifecycleOwner) viewModel.context).getLifecycle().removeObserver(lifecycleObserver);
                    lifecycleObserver = null;
                }
            }
        }

    }

    public static abstract class ViewObserverRunnable implements Runnable {
        @NonNull
        public View VIEW;

        public ViewObserverRunnable(@NonNull View VIEW) {
            this.VIEW = VIEW;
        }
    }

    /**
     * @param runnable 添加绘制完成时任务
     */
    public void addGlobalLayoutRunnable(ViewObserverRunnable runnable) {
        try {
            if (globalLayoutRunnables == null) {
                globalLayoutRunnables = new HashSet<>();
            }
            globalLayoutRunnables.add(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param runnable 移除绘制完成时任务
     */
    public void removeGlobalLayoutRunnable(ViewObserverRunnable runnable) {
        try {
            if (globalLayoutRunnables != null) {
                globalLayoutRunnables.remove(runnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param onCancelListener 取消
     */
    public void setOnCancelListener(@Nullable View.OnClickListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    private View.OnClickListener rootViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cancelable) {
                dissmiss();
                if (onCancelListener != null) {
                    onCancelListener.onClick(v);
                }
            }
        }
    };

    public static final View.OnClickListener EMPTY_ONCLICKLISTENER = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    /**
     * @param layoutParams 布局根节点的layoutParams参数
     */
    public void handleViewParams(@NonNull WindowManager.LayoutParams layoutParams) {
    }

    /**
     * Android 9.0以下的版本才能通过反射找到mParentWindow
     *
     * @param layoutParams 设置window参数
     */
    public void handleWindowLayoutParams(@NonNull final WindowManager.LayoutParams layoutParams) {
    }

}
