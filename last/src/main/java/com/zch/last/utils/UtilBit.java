package com.zch.last.utils;

import androidx.annotation.NonNull;

public class UtilBit {
    public enum SIZE {
        B,
        KB,
        MB,
        GB,
        TB
    }

    public static double format(long bytes, @NonNull SIZE type) {
        if (SIZE.B.equals(type)) {
            return bytes;
        }
        float kb = bytes / 1024f;
        if (SIZE.KB.equals(type)) {
            return kb;
        }
        float mb = kb / 1024f;
        if (SIZE.MB.equals(type)) {
            return mb;
        }
        float gb = mb / 1024f;
        if (SIZE.GB.equals(type)) {
            return gb;
        }
        return gb / 1024f;
    }

    public static void main(String[] a) {

    }
}
