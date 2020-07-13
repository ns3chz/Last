package com.zch.last.bluetooth.interf;

import android.bluetooth.BluetoothDevice;

import io.reactivex.annotations.NonNull;

public interface OnBTDiscoveryImp {
    void deviceFound(@NonNull BluetoothDevice device, short rssi);

    void deviceFoundFinish();

    void deviceFoundCancel(int reason);
}
