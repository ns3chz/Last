package com.zch.last.bluetooth.interf;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.ParcelUuid;

import androidx.annotation.RequiresPermission;

import com.zch.last.utils.UtilLogger;

import java.util.Arrays;

import io.reactivex.annotations.NonNull;

public class OnBTDiscoveryListener implements OnBTDiscoveryImp {
    private static final String TAG = "OnBTDiscoveryListener";
    /**
     * 搜索取消原因,蓝牙未打开
     */
    public static final int CANCEL_RESON_BT_UNENABLE = 1;
    public static final int CANCEL_RESON_PERMISSION_LOCATION_DENIED = 2;

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    @Override
    public void deviceFound(@NonNull BluetoothDevice device, short rssi) {
        //蓝牙rssi参数，代表蓝牙强度
        String name = device.getName();
        int bondState = device.getBondState();
        int type = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            type = device.getType();
        }
        String address = device.getAddress();
        ParcelUuid[] uuids = new ParcelUuid[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            uuids = device.getUuids();
        }

        UtilLogger.logV(TAG, "发现新设备: \n" +
                "rssi : " + rssi +
                "\nname : " + name +
                "\nbondState : " + bondState +
                "\ntype : " + type +
                "\naddress : " + address +
                "\nuuids : " + Arrays.toString(uuids));
    }

    @Override
    public void deviceFoundFinish() {
        UtilLogger.logV(TAG, "bluetooth discovery is finished ...");
    }

    @Override
    public void deviceFoundCancel(int reason) {
        UtilLogger.logV(TAG, "bluetooth discovery is cancel (" + reason + ") ...");
    }
}
