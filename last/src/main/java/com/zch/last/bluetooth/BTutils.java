package com.zch.last.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

import androidx.annotation.RequiresPermission;

import java.util.Arrays;

public class BTutils {
    public static boolean isSupportedBluetooth() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        return defaultAdapter != null;
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public static boolean isTheSameDevice(BluetoothDevice old, BluetoothDevice news) {
        if (old == null && news == null) return true;
        if (old == null ^ news == null) return false;
        String oldAddress = old.getAddress();
        String newsAddress = news.getAddress();
        try {
            if (!oldAddress.equalsIgnoreCase(newsAddress)) {
                return false;
            }
        } catch (Exception ignored) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            ParcelUuid[] oldUuids = old.getUuids();
            ParcelUuid[] newsUuids = news.getUuids();
            return Arrays.equals(oldUuids, newsUuids);
        }
        return true;
    }
}
