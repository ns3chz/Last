package com.zch.last.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.RequiresPermission;

import com.zch.last.bluetooth.interf.OnBTStatusChangedListener;
import com.zch.last.core.ActivityResult;
import com.zch.last.utils.UtilLogger;
import com.zch.last.utils.UtilObject;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * 蓝牙相关状态监听
 */
public class BTStatus {
    private static BTStatus INSTANCE = null;

    public static BTStatus get() {
        if (INSTANCE == null) {
            INSTANCE = new BTStatus();
        }
        return INSTANCE;
    }

    private static final int REQUEST_CODE_OPEN_BLUETOOTH = 1;//打开蓝牙请求码

    @NonNull
    private final Set<OnBTStatusChangedListener> onBTStatusChangedListeners;

    private BTStatus() {
        onBTStatusChangedListeners = new CopyOnWriteArraySet<>();
    }


    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void open(@NonNull Context context, @Nullable final OnBTOpenListener runnable) {
        open(context, 10000, runnable);
    }

    /**
     * 打开蓝牙并执行任务
     *
     * @param context 仅当context为Activity时，
     *                且在{@link Activity *onActivityResult(int, int, Intent)}中
     *                回调{@link ActivityResult handleActivityResult(int, int, Intent)}方法，
     *                才可以监听到用户取消操作
     * @param timeout 当context不为Activity时，超时后不回调方法
     */
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void open(@NonNull Context context, int timeout, @Nullable final OnBTOpenListener runnable) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter.isEnabled()) {
            if (runnable != null) {
                runnable.opened();
            }
            return;
        }
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (context instanceof Activity) {
            ActivityResult.listen((Activity) context, new ActivityResult.OnActivityResultListener() {
                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    if (requestCode != REQUEST_CODE_OPEN_BLUETOOTH) return;
                    if (resultCode == Activity.RESULT_OK) {
                        if (runnable != null) {
                            runnable.opened();
                        }
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        if (runnable != null) {
                            runnable.openCancelled("取消了蓝牙打开");
                        }
                    }
                }
            });
            ((Activity) context).startActivityForResult(enableBtIntent, REQUEST_CODE_OPEN_BLUETOOTH);
        } else {
            if (runnable != null) {
                OnBTStatusChangedListener listener = new OnBTStatusChangedListener("openBT") {

                    @Override
                    public void stateOn() {
                        shouldRecycle = true;
                        runnable.opened();
                    }

                };
                listener.timeOut = timeout;
                observe(context, listener);
            }
            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(enableBtIntent);
        }
    }

    /**
     * 监听蓝牙状态
     */
    public void observe(@NonNull Context context, @NonNull OnBTStatusChangedListener listener) {
        for (OnBTStatusChangedListener scl : onBTStatusChangedListeners) {
            if (scl.tag.equals(listener.tag)) {
                onBTStatusChangedListeners.remove(scl);
            }
        }
        onBTStatusChangedListeners.add(listener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        context.registerReceiver(btStatusOpenObserver, intentFilter);
    }

    /**
     * 解除监听
     */
    public void unobserve(String tag) {
        for (OnBTStatusChangedListener listener : onBTStatusChangedListeners) {
            if (UtilObject.equals(listener.tag, tag)) {
                onBTStatusChangedListeners.remove(listener);
            }
        }
    }

    /**
     * 解除监听
     */
    public void unobserve(OnBTStatusChangedListener listener) {
        try {
            onBTStatusChangedListeners.remove(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 状态广播监听
     */
    private BroadcastReceiver btStatusOpenObserver = new BroadcastReceiver() {

        private String action;

        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = null;
            try {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            action = intent.getAction();
            if (action == null) return;
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    dispatchStatus(state + "", device);
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    dispatchStatus(BluetoothDevice.ACTION_ACL_CONNECTED, device);

                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    dispatchStatus(BluetoothDevice.ACTION_ACL_DISCONNECTED, device);
                    break;
            }
            //取消监听
            if (onBTStatusChangedListeners.size() == 0) {
                try {
                    context.unregisterReceiver(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            UtilLogger.logV("btStatusOpenObserver", "ACTION : " + action +
                    "\nDEVICE : " + (device == null ? null : device.toString()));
        }
    };

    /**
     * 分发状态
     */
    private void dispatchStatus(@NonNull String status, @Nullable BluetoothDevice device) {
        try {
            for (OnBTStatusChangedListener listener : onBTStatusChangedListeners) {
                try {
                    if (listener.isTimeOut()) {
                        listener.shouldRecycle = true;
                    } else {
                        switch (status) {
                            case BluetoothAdapter.STATE_TURNING_ON + "":
                                listener.stateTurnOn();
                                break;
                            case BluetoothAdapter.STATE_ON + "":
                                listener.stateOn();
                                break;
                            case BluetoothAdapter.STATE_TURNING_OFF + "":
                                listener.stateTurnOFF();
                                break;
                            case BluetoothAdapter.STATE_OFF + "":
                                listener.stateOFF();
                                break;
                            case BluetoothDevice.ACTION_ACL_CONNECTED:
                                listener.stateACLConnected(device);
                                break;
                            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                                listener.stateACLDisconnected(device);
                                break;
                            default:
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //监听到了状态，且不再需要监听
                if (listener.shouldRecycle) {
                    onBTStatusChangedListeners.remove(listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 蓝牙打开回调
     */
    public interface OnBTOpenListener {
        void opened();

        void openCancelled(@Nullable String msg);
    }

}
