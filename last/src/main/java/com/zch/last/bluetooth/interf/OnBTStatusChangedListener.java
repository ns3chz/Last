package com.zch.last.bluetooth.interf;

import android.bluetooth.BluetoothDevice;

import io.reactivex.annotations.Nullable;

public abstract class OnBTStatusChangedListener implements OnBTStatusChangedImp {
    /**
     * 唯一标识
     */
    public final String tag;
    public boolean shouldRecycle;
    private final long createTime;
    /**
     * 蓝牙打开超时时间，毫秒
     */
    public int timeOut = 20000;

    public OnBTStatusChangedListener(String tag) {
        this.tag = tag;
        this.createTime = System.nanoTime();
    }

    /**
     * @return 是否超时
     */
    public boolean isTimeOut() {
        if (timeOut <= 0) return false;
        long nanoTime = System.nanoTime();
        return nanoTime - createTime > timeOut * 1000000;
    }


    @Override
    public void stateOn() {

    }

    @Override
    public void stateOFF() {

    }

    @Override
    public void stateTurnOn() {

    }

    @Override
    public void stateTurnOFF() {

    }

    @Override
    public void stateACLConnected(@Nullable BluetoothDevice device) {

    }

    @Override
    public void stateACLDisconnected(@Nullable BluetoothDevice device) {

    }
}
