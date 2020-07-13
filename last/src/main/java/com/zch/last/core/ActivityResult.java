package com.zch.last.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ActivityResult {
    private static class Listener {
        @NonNull
        public static final ConcurrentHashMap<Integer, ActivityResult> MAP = new ConcurrentHashMap<>();
    }

    public static void listen(@Nullable Activity activity, @Nullable OnActivityResultListener listener) {
        Collection<ActivityResult> values = Listener.MAP.values();
        for (ActivityResult value : values) {
            value.checkShouldRemove();
        }

        if (activity == null || listener == null) {
            return;
        }
        int hashCode = activity.hashCode();
        ActivityResult activityResult;
        if (Listener.MAP.containsKey(hashCode)) {
            activityResult = Listener.MAP.get(hashCode);
        } else {
            activityResult = new ActivityResult(activity);
        }
        if (activityResult != null) {
            if (!activityResult.checkShouldRemove()) {
                activityResult.addListen(listener);
                Listener.MAP.put(hashCode, activityResult);
            }
        }
    }

    @NonNull
    public final WeakReference<Activity> wrActivity;
    public final int activityHashCode;
    @NonNull
    private final Set<OnActivityResultListener> onActivityResultListeners;

    public ActivityResult(Activity activity) {
        this.activityHashCode = activity.hashCode();
        this.wrActivity = new WeakReference<>(activity);
        this.onActivityResultListeners = new CopyOnWriteArraySet<>();
    }

    /**
     * 检查是否应当去掉监听
     */
    private boolean checkShouldRemove() {
        boolean shouldRemove;
        Activity activity = wrActivity.get();
        if (activity == null) {
            shouldRemove = false;
        } else if (activity.isFinishing()) {
            shouldRemove = true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            shouldRemove = activity.isDestroyed();
        } else {
            shouldRemove = false;
        }
        if (shouldRemove) {
            Listener.MAP.remove(this.activityHashCode);
        }
        return shouldRemove;
    }

    public void addListen(OnActivityResultListener listener) {
        if (listener == null) return;
        onActivityResultListeners.add(listener);
    }

    /**
     * 在{@link Activity onActivityResult(int, int, Intent)}中添加
     */
    public static void handleActivityResult(@Nullable Activity activity, int requestCode, int resultCode, Intent data) {
        if (activity == null) return;
        if (activity.isFinishing()) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) return;
        }
        //
        ActivityResult activityResult = Listener.MAP.get(activity.hashCode());
        if (activityResult == null) return;
        for (OnActivityResultListener listener : activityResult.onActivityResultListeners) {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface OnActivityResultListener {

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
