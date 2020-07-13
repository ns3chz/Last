package com.zch.last.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Object工具
 */
public class UtilObject {


    public static boolean equals(Object a, Object b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
//        return (a == b) || (a != null && a.equals(b));
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

    public static boolean equalsValue(byte[] a, byte[] b) {
        if (a == null && b == null) return true;
        if (a == null ^ b == null) return false;
        if (a.length != b.length) return false;

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 找到数据中的报头或报尾
     *
     * @param data     数据
     * @param find     要找的数据
     * @param findHead true：find数据作为报头，false：报尾
     * @param expect   当未找到时，是否保留当前数据并期望之后能找到
     * @return 0:返回数据；1:判断是否找到，null:未找到,byte[0]:找到了
     */
    @NonNull
    public static byte[][] findHeadTail(@Nullable byte[] data, @Nullable byte[] find, boolean findHead, boolean expect) {
        if (data == null || data.length == 0) {
            return new byte[][]{data, null};
        }
        if (find == null || find.length == 0) {
            if (expect) {
                return new byte[][]{data, null};
            } else {
                return new byte[][]{null, null};
            }
        }
        int removeIndex = 0;
        int dIndex = 0;
        int fIndex = 0;
        byte d, f;//
        boolean findout = true;
        while (dIndex < data.length) {
            findout = true;
            d = data[dIndex];
            while (fIndex < find.length) {

                f = find[fIndex];

                if (d == f) {
                    if (dIndex + fIndex + 1 >= data.length) {
                        if (fIndex + 1 < find.length) {
                            findout = expect;
                        }
                        break;
                    }
                    d = data[dIndex + fIndex + 1];
                    fIndex++;
                } else {
                    findout = false;
                    break;
                }
            }
            fIndex = 0;
            dIndex++;
            if (findout) {
                break;
            }
            removeIndex = dIndex;
        }
        if (!expect && !findout) {
            return new byte[][]{null, null};
        }

        int start, length;
        if (findHead) {
            length = data.length - removeIndex;
            if (length < 0) {
                length = 0;
            }
            start = removeIndex;
        } else {
            length = removeIndex + find.length;
            if (length > data.length) {
                length = data.length;
            }
            start = 0;
        }
        if (length == 0) {
            return new byte[][]{null, null};
        }
        byte[] res = new byte[length];
        System.arraycopy(data, start, res, 0, length);
        return new byte[][]{res, findout ? new byte[0] : null};
    }
}
