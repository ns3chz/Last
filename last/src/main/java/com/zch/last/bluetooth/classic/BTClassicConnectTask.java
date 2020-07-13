package com.zch.last.bluetooth.classic;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class BTClassicConnectTask extends AsyncTask<BluetoothSocket, Integer, BluetoothSocket> {
    private OnCallBack onCallBack;

    BTClassicConnectTask(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    @Override
    protected BluetoothSocket doInBackground(BluetoothSocket... bluetoothSockets) {
        if (bluetoothSockets == null || bluetoothSockets.length == 0) return null;
        BluetoothSocket socket = bluetoothSockets[0];
        Exception error = null;
        int count = 0;
        while (count < 3) {
            try {
                socket.connect();
                error = null;
                break;
            } catch (Exception e) {
                count++;
                error = e;
                e.printStackTrace();
            }
        }


        if (this.onCallBack != null) {
            if (error == null) {
                this.onCallBack.call(socket);
            } else {
                this.onCallBack.error(socket, error);
            }
        }
        return socket;
    }

    public interface OnCallBack {
        void call(@NonNull BluetoothSocket socket);

        void error(@Nullable BluetoothSocket socket, @NonNull Exception e);
    }
}
