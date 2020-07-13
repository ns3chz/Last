package com.zch.last.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresPermission;

import com.zch.last.bluetooth.interf.OnBTDiscoveryListener;
import com.zch.last.utils.UtilLogger;
import com.zch.last.utils.UtilPermission;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * 蓝牙搜索
 */
public class BTSearch {
    private static BTSearch INSTANCE;
    private static final String TAG = "BTSearch";

    public static BTSearch get() {
        if (INSTANCE == null) {
            INSTANCE = new BTSearch();
        }
        return INSTANCE;
    }

    private static final int REQUEST_CODE_LOCATIOIN = 102;//定位权限请求码

    @NonNull
    private final Set<OnBTDiscoveryListener> onBTDiscoveryListeners;

    private BTSearch() {
        onBTDiscoveryListeners = new CopyOnWriteArraySet<>();
    }

    /**
     * 6.0以上的设备需要定位权限,
     * 在activity中需要使用{@link UtilPermission#listen(Activity, int, String[], int[])}方法
     */
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void search(@NonNull final Context context, @Nullable final OnBTDiscoveryListener listener) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!defaultAdapter.isEnabled()) {
            if (listener != null) {
                listener.deviceFoundCancel(OnBTDiscoveryListener.CANCEL_RESON_BT_UNENABLE);
            }
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startSearch(context, listener);
        } else {
            if (!(context instanceof Activity)) {
                if (listener != null) {
                    listener.deviceFoundCancel(OnBTDiscoveryListener.CANCEL_RESON_PERMISSION_LOCATION_DENIED);
                }
            } else {
                //6.0以上的设备需要定位权限
                UtilLogger.logV(TAG, "SDK_INT >= M(23) , request location permission ing ...");
                UtilPermission.request((Activity) context, REQUEST_CODE_LOCATIOIN,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        new UtilPermission.OnPermissionRequestListener() {
                            @Override
                            @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
                            public void listen(int requestCode, @Nullable String[] requestPermissions, @Nullable List<String> grantedPermissions, @Nullable List<String> deniedPermissions) {
                                if (grantedPermissions != null && grantedPermissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                    startSearch(context, listener);
                                } else {
                                    if (listener != null) {
                                        listener.deviceFoundCancel(OnBTDiscoveryListener.CANCEL_RESON_PERMISSION_LOCATION_DENIED);
                                    }
                                }
                            }
                        });
            }
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    private void startSearch(@NonNull Context context, @Nullable OnBTDiscoveryListener listener) {
        stopDiscovery(context);
        if (listener != null) {
            onBTDiscoveryListeners.add(listener);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(discoverReceiver, filter);
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void stopDiscovery(Context context) {
        try {
            context.unregisterReceiver(discoverReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter.isDiscovering()) {
            defaultAdapter.cancelDiscovery();
        }
    }

    private BroadcastReceiver discoverReceiver = new BroadcastReceiver() {
        @Override
        @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN})
        public void onReceive(Context context, Intent intent) {
            dispatchBroadcastAction(context, intent);
        }

    };

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    private void dispatchBroadcastAction(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        //蓝牙rssi参数，代表蓝牙强度
        short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
        BluetoothDevice device = null;
        try {
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        } catch (Exception e) {
            UtilLogger.logE(TAG, "getParcelableExtra BluetoothDevice Exception ...\n" + e.getMessage());
            e.printStackTrace();
        }
        for (OnBTDiscoveryListener listener : onBTDiscoveryListeners) {
            switch (action) {
                case BluetoothDevice.ACTION_FOUND://发现设备
                    if (device == null) {
                        UtilLogger.logV(TAG, "发现新设备 NULL");
                    } else {
                        listener.deviceFound(device, rssi);
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED://扫描完成
                    listener.deviceFoundFinish();
                    try {
                        onBTDiscoveryListeners.remove(listener);
                    } catch (Exception ignored) {
                    }
                    break;
            }
        }

    }
}
