package com.zch.last.bluetooth.classic;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import androidx.annotation.RequiresPermission;

import com.zch.last.stream.adapter.ReadAdapterAbs;
import com.zch.last.utils.UtilLogger;

import java.util.Locale;
import java.util.UUID;

import io.reactivex.annotations.Nullable;

public class BTClassicClient<IN extends ReadAdapterAbs<IN>> extends BTClassicConnectAbs<IN> {
    @Nullable
    private BluetoothDevice remoteDevice = null;
    @Nullable
    private UUID uuid;
    @Nullable
    private UUID createduuid;
    /**
     * 是否建立安全连接
     */
    private boolean isSecureRfcomm = false;

    public BTClassicClient() {
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public BTClassicClient(@Nullable byte[] address) {
        this(address, null);
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public BTClassicClient(@Nullable String address) {
        this(address, null);
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public BTClassicClient(@Nullable byte[] address, @Nullable UUID uuid) {
        this(address == null || address.length != 6 ? null :
                        String.format(Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X",
                                address[0], address[1], address[2], address[3], address[4], address[5]),
                uuid);
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public BTClassicClient(@Nullable String address, @Nullable UUID uuid) {
        setAddress(address, uuid);
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    @Nullable
    @Override
    public BluetoothSocket createSocket() {

        BluetoothSocket create = null;
        if (remoteDevice == null || uuid == null) {
            UtilLogger.logV(TAG, "init - remoteDevice is " + (remoteDevice == null ? null : "not null") +
                    " , uuid is " + (uuid == null ? null : "not null ."));
        } else {
            //
            try {
                if (isSecureRfcomm) {
                    create = remoteDevice.createRfcommSocketToServiceRecord(uuid);
                } else {
                    create = remoteDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                }
                createduuid = uuid;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return create;
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public void setAddress(String address) {
        setAddress(address, uuid);
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public void setAddress(String address, UUID uuid) {
        this.uuid = uuid;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (address == null || address.length() == 0) {
            remoteDevice = null;
        } else {
            remoteDevice = defaultAdapter.getRemoteDevice(address);
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public void setDevice(@Nullable BluetoothDevice device) {
        setDevice(device, uuid);
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public void setDevice(@Nullable BluetoothDevice device, UUID uuid) {
        this.remoteDevice = device;
        this.uuid = uuid;
    }

    public BluetoothDevice getRemoteDevice() {
        return remoteDevice;
    }

    public void setSecureRfcomm(boolean secureRfcomm) {
        isSecureRfcomm = secureRfcomm;
    }
}
