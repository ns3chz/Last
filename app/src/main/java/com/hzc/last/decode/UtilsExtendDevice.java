package com.hzc.last.decode;

import androidx.annotation.NonNull;

/**
 * 扩展设备协议
 */
public class UtilsExtendDevice {


    public static boolean checkoutCHK(@NonNull byte[] data) {
        return checkoutCHK(data, data.length - 1);
    }

    /**
     * @return 检查校验位
     */
    public static boolean checkoutCHK(@NonNull byte[] data, int chkIndex) {
        try {
            //检查校验位
            int chk = data[chkIndex];//校验位
            int cmd = data[7];//指令码
            for (int i = 8; i < chkIndex; i++) {
                cmd ^= data[i];
            }
            return chk == cmd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
