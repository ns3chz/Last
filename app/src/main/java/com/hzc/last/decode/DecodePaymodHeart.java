package com.hzc.last.decode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 支付模块心跳
 */
public class DecodePaymodHeart implements DecodeImp {
    @Nullable
    private OnCallback callback;


    public DecodePaymodHeart() {
    }

    /**
     * @param data 固定32长度，包含指令码
     */
    @Override
    public void decode(@Nullable byte[] data) {
        if (callback == null) return;
        if (data == null || data.length != 32) return;
        //imei
        StringBuilder imei = new StringBuilder();
        for (int i = 0; i < 19; i++) {
            imei.append((char) data[i]);
        }
        //网络状态
        int state = getPosiByte(data[19]);
        //剩余时长
        int spareTime = getPosiByte(data[20]) << 24 +
                getPosiByte(data[21]) << 16 +
                getPosiByte(data[22]) << 8 +
                getPosiByte(data[23]);
        //当前时间
        int[] curTime = new int[6];
        for (int i = 0; i < curTime.length; i++) {
            curTime[i] = getPosiByte(data[24 + i]);
        }

        callback.receive(imei.toString(), state, spareTime, curTime);
    }

    private int getPosiByte(byte b) {
        if (b < 0) {
            return b + 256;
        }
        return b;
    }

    public void setCallback(@Nullable OnCallback callback) {
        this.callback = callback;
    }

    public interface OnCallback {
        /**
         * @param imei      imei
         * @param state     网络状态
         * @param spareTime 剩余时间
         * @param curTime   当前时间,年月日时分秒
         */
        void receive(@NonNull String imei, int state, int spareTime, @NonNull int[] curTime);
    }
}
