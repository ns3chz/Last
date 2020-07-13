package com.zch.last.bluetooth.classic;

import android.bluetooth.BluetoothSocket;

import io.reactivex.annotations.Nullable;

public interface BTClassicConnectImp {

    @Nullable
    BluetoothSocket createSocket();

    void connect();

    void disconnect();

    void startRead();

    void stopRead();

    void write(@Nullable byte[] data);

    void stopWrite();
}
