package com.zch.test;

import android.view.View;

import java.util.Arrays;

public class Test2 {
    public static void main(String[] a) {
//        String html = "news feed\" href=\"http://m.qpic.cn/psc?feeds.qzone.qq.com/cgi-bin/cgi_rss_out?uin=726711645viewer_4\" />\n";
////        String html ="http://feeds.qzone.qq.com/cgi-bin/cgi_rss_out?uin=726711645\"";
//        Pattern compile = Pattern.compile("http://m.qpic.cn/psc\\?.*viewer.*\"");
//        Matcher matcher = compile.matcher(html);
//        boolean b = matcher.find();
//        System.out.println(b);

        String regx = "^[jJ][pP][tT]-[pP][aA][yY]-.*";
        System.out.println("jPT-PAY-123456fsd".matches(regx));
    }
}
