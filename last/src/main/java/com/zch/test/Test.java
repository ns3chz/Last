package com.zch.test;

public class Test {
    public static void main(String[] a) {

    }

    /**
     * @return 从url中获取文件名
     */
    public static String getNameFromUrl(String url) {
        if (url == null || url.length() == 0) {
            return "" + System.currentTimeMillis();
        }
        int indexGang = url.lastIndexOf("/") + 1;
        int indexOfPoint = url.lastIndexOf(".");
        if (indexOfPoint < indexGang) {
            indexOfPoint = url.length();
        }
        return url.substring(indexGang, indexOfPoint);
    }

}







