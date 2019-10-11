package com.zch.last.utils;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;

/**
 * Object工具
 */
public class UtilObject {


    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @NonNull
    public static String byte2hex(byte... bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (i != 0) {
                sb.append(":");
            }
            sb.append(String.format("%02X", bytes[i]));
        }
        return sb.toString();
    }

    @NonNull
    public static String byte2ASCLL(byte... b) {
        if (b == null || b.length == 0) {
            return "";
        }
        StringBuilder hs = new StringBuilder();
        for (int n = 0; n < b.length; n++) {
            hs.append((char) b[n]);
        }
        return hs.toString();
    }


}
