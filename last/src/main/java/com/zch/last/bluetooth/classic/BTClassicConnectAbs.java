package com.zch.last.bluetooth.classic;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import androidx.annotation.RequiresPermission;

import com.zch.last.BuildConfig;
import com.zch.last.bluetooth.BTutils;
import com.zch.last.stream.InputStreamTask;
import com.zch.last.stream.OutputStreamTask;
import com.zch.last.stream.listener.OnInputStreamTaskListener;
import com.zch.last.utils.UtilLogger;

import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public abstract class BTClassicConnectAbs implements BTClassicConnectImp {
    protected final String TAG = getClass().getName();

    @Nullable
    protected BluetoothSocket bluetoothSocket;
    @Nullable
    private BTClassicConnectTask mBTClassicConnectTask;
    @NonNull
    public final InputStreamTask mInputStreamTask;
    @NonNull
    public final OutputStreamTask mOutputStreamTask;
    @Nullable
    private BTClassicConnectTask.OnCallBack onConnectCallBack;

    public BTClassicConnectAbs() {
        mInputStreamTask = new InputStreamTask();
        mOutputStreamTask = new OutputStreamTask();
        //
        mInputStreamTask.onTaskListener = new OnInputStreamTaskListener() {
            @Override
            public void onStreamClosed() {
                disconnect();
            }
        };
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    @Override
    public void connect() {
        if (mBTClassicConnectTask != null && mBTClassicConnectTask.getStatus() != AsyncTask.Status.FINISHED) {
            UtilLogger.logV(TAG, "mBTClassicConnectTask is connecting ... ");
            return;
        }
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter.isDiscovering()) {
                defaultAdapter.cancelDiscovery();
            }
        } catch (Exception ignored) {
        }
        boolean connected = isConnected();
        if (connected) {
            UtilLogger.logV(TAG, "bluetoothSocket is connected ...");
            return;
        }
        BluetoothSocket socket = createSocket();
        try {
            if (!BTutils.isTheSameDevice(bluetoothSocket.getRemoteDevice(), socket.getRemoteDevice())) {
                disconnect();
            }
        } catch (Exception ignored) {
        }
        bluetoothSocket = socket;
        if (bluetoothSocket == null) {
            UtilLogger.logV(TAG, "bluetoothSocket create is null !!!");
            if (onConnectCallBack != null) {
                onConnectCallBack.error(null, new Exception("bluetoothSocket create is null !!!"));
            }
            return;
        }
        mBTClassicConnectTask = new BTClassicConnectTask(new BTClassicConnectTask.OnCallBack() {
            @Override
            public void call(@NonNull BluetoothSocket socket) {
                UtilLogger.logV(TAG, "BTClassicConnectTask.OnCallBack ... call ");
                InputStream inputStream;
                OutputStream outputStream;
                try {
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                } catch (Exception e) {
                    inputStream = null;
                    outputStream = null;
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace();
                    }
                }

                mInputStreamTask.setInputStream(inputStream);
                mOutputStreamTask.setOutputStream(outputStream);
                if (onConnectCallBack != null) {
                    onConnectCallBack.call(socket);
                }
            }

            @Override
            public void error(@NonNull BluetoothSocket socket, Exception e) {
                UtilLogger.logV(TAG, "BTClassicConnectTask.OnCallBack ... error: " + (e == null ? "" : e.getMessage()));
                mInputStreamTask.setInputStream(null);
                mOutputStreamTask.setOutputStream(null);
                if (onConnectCallBack != null) {
                    onConnectCallBack.error(socket, e);
                }
            }
        });
        mBTClassicConnectTask.execute(bluetoothSocket);
    }

    /**
     * @return NULL:bluetoothSocket==null
     */
    public boolean isConnected() {
        if (bluetoothSocket == null) {
            UtilLogger.logV(TAG, "when use isConnected(),bluetoothSocket = null .");
            return false;
        }
        if (bluetoothSocket.isConnected()) {
            UtilLogger.logV(TAG, "when use isConnected(),bluetoothSocket isConnected .");
            return true;
        }
        UtilLogger.logV(TAG, "when use isConnected(),bluetoothSocket isDisconnected .");
        return false;
    }

    @Override
    public void disconnect() {
        stopRead();
        stopWrite();
        if (mBTClassicConnectTask != null) {
            if (!AsyncTask.Status.FINISHED.equals(mBTClassicConnectTask.getStatus())) {
                mBTClassicConnectTask.cancel(true);
            }
        }
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void startRead() {
        mInputStreamTask.startRead();
    }

    @Override
    public void stopRead() {
        mInputStreamTask.stopRead();
    }

    @Override
    public void write(@Nullable byte[] data) {
        mOutputStreamTask.write(data);
    }


    @Override
    public void stopWrite() {
        mOutputStreamTask.stopWrite();
    }

    public void setOnConnectCallBack(BTClassicConnectTask.OnCallBack onConnectCallBack) {
        this.onConnectCallBack = onConnectCallBack;
    }
}
