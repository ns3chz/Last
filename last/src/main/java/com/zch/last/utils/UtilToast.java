package com.zch.last.utils;

import android.widget.Toast;

import com.zch.last.core.LastApplication;

/**
 * 方便打印toast
 */
public class UtilToast {
    public static void toast(final String text) {
        UtilThread.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LastApplication.INSTANCE, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
