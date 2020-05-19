package com.zch.test;

public class Test {
    public static void main(String[] a) {
        String regx = "(\\(老版本可用\\)){3,}";
        String text = "(老版本可用)(老版本可用)(老版本可用)(老版本可用)(老版本可用)(老版本可用)";
        System.out.println(text.matches(regx));

    }

    public static void get(int i) {
        i = 3;
    }
}
