package com.zch.last.bluetooth.interf;


import android.bluetooth.BluetoothDevice;

import io.reactivex.annotations.Nullable;

public interface OnBTStatusChangedImp {
    /**
     * 开关已打开
     */
    void stateOn();

    /**
     * 开关已关闭
     */
    void stateOFF();

    /**
     * 开关正在打开
     */
    void stateTurnOn();

    /**
     * 开关正在关闭
     */
    void stateTurnOFF();

    /**
     * 已连接
     */
    void stateACLConnected(@Nullable BluetoothDevice device);

    /**
     * 已断开连接
     */
    void stateACLDisconnected(@Nullable BluetoothDevice device);


}
