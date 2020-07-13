package com.hzc.last;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hzc.last.decode.DecodePaymodHeart;
import com.zch.last.activity.BaseLastActivity;
import com.zch.last.bluetooth.BTSearch;
import com.zch.last.bluetooth.BTStatus;
import com.zch.last.bluetooth.interf.OnBTDiscoveryListener;
import com.zch.last.core.ActivityResult;
import com.zch.last.utils.UtilLogger;
import com.zch.last.utils.UtilToast;

import java.util.Arrays;
import java.util.UUID;

public class MainActivity extends BaseLastActivity implements View.OnClickListener {

    private final String wakeTag = "com.hzc.last:myWakeLock";
    private UUID BTUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private DecodePaymodHeart decodePaymodHeart;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                BTStatus.get().open(getApplicationContext(), new BTStatus.OnBTOpenListener() {
                    @Override
                    public void opened() {
                        UtilToast.toast("打开了蓝牙");
                    }

                    @Override
                    public void openCancelled(@Nullable String msg) {
                        UtilToast.toast("取消了蓝牙打开");
                    }
                });
                break;
            case R.id.button1:
                BTSearch.get().search(this, new OnBTDiscoveryListener() {
                    @Override
                    public void deviceFound(@NonNull BluetoothDevice device, short rssi) {
                        super.deviceFound(device, rssi);
                        UtilToast.toast("找到设备");
                    }

                    @Override
                    public void deviceFoundCancel(int reason) {
                        super.deviceFoundCancel(reason);
                        UtilToast.toast("取消发现");
                    }

                    @Override
                    public void deviceFoundFinish() {
                        super.deviceFoundFinish();
                        UtilToast.toast("发现完成");
                    }
                });
                break;
            case R.id.button2:
                break;
            case R.id.button3:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResult.handleActivityResult(this, requestCode, resultCode, data);

    }

    @Override
    public boolean useButterKnife() {
        return true;
    }

    @Override
    public void onCreated(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initIntent(@NonNull Intent intent) {
    }

    @Override
    public void initView() {
        decodePaymodHeart = new DecodePaymodHeart();
        decodePaymodHeart.setCallback(new DecodePaymodHeart.OnCallback() {
            @Override
            public void receive(@NonNull String imei, int state, int spareTime, @NonNull int[] curTime) {
                UtilLogger.logD("DecodePaymodHeart",
                        " " +
                                "\nimei : " + imei +
                                "\nstate : " + state +
                                "\nspareTime : " + spareTime +
                                "\ncurTime : " + Arrays.toString(curTime)
                );
            }
        });

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {

    }


    private final int EXPDECP_A = 0x82;
    private final byte EXPDECP_B = 0x4C;
    private final int EXPDECP_C = 0xAF;

    private void expdDecryptData(byte[] data) {
        if (data == null) return;
        if (data.length < 7) return;
        int d3 = getPositive(data[3]);
        int d4 = getPositive(data[4]) << 8;
        int d5 = getPositive(data[5]) << 16;
        int d6 = getPositive(data[6]) << 24;
        long key = d3 + d4 + d5 + d6;
        for (int i = 7; i < data.length; i++) {
            key = ((key >> 8) | (key & 0xFF) << 24);
            long keya = key * EXPDECP_A;
            long keyb = key % EXPDECP_B;
            key = keya + keyb + EXPDECP_C;
            data[i] = (byte) ((key & 0xFF) ^ data[i]);
            key = key & 0xFFFFFFFFL;
        }
    }

    private int getPositive(byte b) {
        if (b < 0) {
            return b + 256;
        }
        return b;
    }
}
