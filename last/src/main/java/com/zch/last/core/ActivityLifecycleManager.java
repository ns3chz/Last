package com.zch.last.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.constanse.Logger;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * activity生命周期管理
 */
public class ActivityLifecycleManager implements Application.ActivityLifecycleCallbacks {
    private static int instanceInt = 0;
    @NonNull
    private static final LinkedList<WeakReference<Activity>> activityLinkedList = new LinkedList<>();//保存activity的数组
    private static final Object SYN_ACT_LIST = new Object();//lock

    private static class Holder {
        static ActivityLifecycleManager INSTANCE = new ActivityLifecycleManager();
    }

    @NonNull
    public static ActivityLifecycleManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * @return 返回当前的activity
     */
    @Nullable
    public static Activity getCurrent() {
        int index = activityLinkedList.size();
        WeakReference<Activity> last = null;
        Activity activity = null;
        while (index > 0 && (last == null || (activity = last.get()) == null)) {
            index--;
            last = activityLinkedList.get(index);
        }
        return activity;
    }

    /**
     * 退出app
     */
    public static void exitApp() {
        int index = 0;
        WeakReference<Activity> last;
        Activity activity;
        while (index < activityLinkedList.size()) {
            try {
                last = activityLinkedList.get(index);
                if (last != null) {
                    activity = last.get();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            index--;
        }
    }

    /**
     * 打印activity列表信息
     */
    public static void printLink() {
        StringBuilder msg = new StringBuilder("[");
        for (int i = 0; i < activityLinkedList.size(); i++) {
            WeakReference<Activity> activityWeakReference = activityLinkedList.get(i);
            if (activityWeakReference == null) {
                msg.append("null,");
            } else {
                Activity activity = activityWeakReference.get();
                if (activity == null) {
                    msg.append("null,");
                } else {
                    msg.append(activity.hashCode()).append(",");
                }
            }
        }
        msg.append("]");
        Logger.logFrame("ActivityLifecycleManager", msg.toString());
    }

    private ActivityLifecycleManager() {
        Logger.logFrame(getClass().getName(), "INSTANCE CREATE : " + (++instanceInt));
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        addActivity(activity);
        Logger.logFrame(getClass().getName(), "onActivityCreated:" + activity.hashCode());
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Logger.logFrame(getClass().getName(), "onActivityStarted:" + activity.hashCode());
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        moveActivityTop(activity);
        Logger.logFrame(getClass().getName(), "onActivityResumed:" + activity.hashCode());
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Logger.logFrame(getClass().getName(), "onActivityPaused:" + activity.hashCode());
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        Logger.logFrame(getClass().getName(), "onActivityStopped:" + activity.hashCode());
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
        Logger.logFrame(getClass().getName(), "onActivitySaveInstanceState:" + activity.hashCode());
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        removeActivityFromLink(activity);
        Logger.logFrame(getClass().getName(), "onActivityDestroyed:" + activity.hashCode());
    }

    //-------------------------------------manage method--------------------------------------------

    private void addActivity(@NonNull Activity activity) {
        synchronized (SYN_ACT_LIST) {
            activityLinkedList.addLast(new WeakReference<>(activity));
        }
    }

    private void moveActivityTop(@NonNull Activity activity) {
        synchronized (SYN_ACT_LIST) {
            WeakReference<Activity> activityWeakReference;
            for (int i = activityLinkedList.size() - 1; i >= 0; i--) {
                activityWeakReference = activityLinkedList.get(i);
                if (activityWeakReference == null) {
                    activityLinkedList.remove(i);
                    continue;
                }
                Activity act = activityWeakReference.get();
                if (act == null) {
                    activityLinkedList.remove(i);
                    continue;
                }
                if (activity.equals(act)) {
                    if (i == activityLinkedList.size() - 1) {
                        return;
                    }
                    activityLinkedList.remove(i);
                    break;
                }
            }
            activityLinkedList.addLast(new WeakReference<>(activity));
        }
    }

    private void removeActivityFromLink(@NonNull Activity activity) {
        synchronized (SYN_ACT_LIST) {
            WeakReference<Activity> activityWeakReference;
            for (int i = activityLinkedList.size() - 1; i >= 0; i--) {
                activityWeakReference = activityLinkedList.get(i);
                if (activityWeakReference == null) {
                    activityLinkedList.remove(i);
                    continue;
                }
                Activity act = activityWeakReference.get();
                if (act == null) {
                    activityLinkedList.remove(i);
                    continue;
                }
                if (activity.equals(act)) {
                    activityLinkedList.remove(i);
                    break;
                }
            }
        }
    }
}
