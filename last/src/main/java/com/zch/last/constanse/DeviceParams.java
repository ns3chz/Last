package com.zch.last.constanse;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.zch.last.utils.UtilSystem;

/**
 *
 */
public class DeviceParams {
    public static float densityDpi;// unit dpi
    public static float density;// densityDpi/160
    public static float scaleDensity;// font size，unit sp
    public static String versionName;
    public static int versionCode;

    /**
     *
     */
    public static void loadDeviceParams(@NonNull Context context) {
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            densityDpi = mDisplayMetrics.densityDpi;
        } else {
            densityDpi = 0;
        }
        density = mDisplayMetrics.density;
        scaleDensity = mDisplayMetrics.scaledDensity;
        //
        PackageInfo packageInfo = UtilSystem.getPackageInfo(context, context.getPackageName());
        if (packageInfo != null) {
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } else {
            versionName = "";
            versionCode = 0;
        }
    }

    /**
     * 得到屏幕宽高
     *
     * @return {宽,高}
     */
    public static int[] getScreenSize(@NonNull Context context) {
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = mDisplayMetrics.widthPixels;
        int heightPixels = mDisplayMetrics.heightPixels;
        return new int[]{widthPixels, heightPixels};
    }
}
