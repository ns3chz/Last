//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zch.last.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class SystemParam {
    public static final int SCREEN_ORIENTATION_VERTICAL = 1;
    public static final int SCREEN_ORIENTATION_HORIZONTAL = 2;
    private static PackageManager packageManager;
    private static ApplicationInfo applicationInfo;
    private static final Map<String, PackageInfo> packageInfoLists = new HashMap<>();
    private static final Map<String, ApplicationInfo> applicationInfoLists = new HashMap<>();
    private static SystemParam param;
    public String androidId;
    public int screenWidth;
    public int screenHeight;
    public float densityDpi;
    public float density;
    public float scaleDensity;
    public @IntRange(from = 1, to = 2)
    int screenOrientation;
    public String versionName;
    public int versionCode;
    private DisplayMetrics mDisplayMetrics;

    private SystemParam(@NonNull Context context) {
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        this.androidId = "android_id";
        this.screenWidth = this.mDisplayMetrics.widthPixels;
        this.screenHeight = this.mDisplayMetrics.heightPixels;
        this.densityDpi = (float) this.mDisplayMetrics.densityDpi;
        this.density = this.mDisplayMetrics.density;
        this.scaleDensity = this.mDisplayMetrics.scaledDensity;
        this.screenOrientation = this.screenHeight >= this.screenWidth ?
                SCREEN_ORIENTATION_VERTICAL : SCREEN_ORIENTATION_HORIZONTAL;
        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName());
        if (packageInfo != null) {
            this.versionName = packageInfo.versionName;
            this.versionCode = packageInfo.versionCode;
        }

        applicationInfo = getApplicationInfo(context, context.getPackageName());
    }

    public static SystemParam get(Context context) {
        if (param == null) {
            param = new SystemParam(context);
        }
        return param;
    }

    public static SystemParam getNew(Context context) {
        param = new SystemParam(context);
        return param;
    }

    private static PackageManager getPackageManager(Context context) {
        if (packageManager == null) {
            packageManager = context.getPackageManager();
        }

        return packageManager;
    }

    public static PackageInfo getPackageInfo(Context context, String packageName) {
        synchronized (packageInfoLists) {
            if (!packageInfoLists.containsKey(packageName)) {
                PackageInfo packageInfo;
                try {
                    packageInfo = getPackageManager(context).getPackageInfo(packageName, 0);
                    packageInfoLists.put(packageName, packageInfo);
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                    packageInfo = null;
                }

                return packageInfo;
            } else {
                return packageInfoLists.get(packageName);
            }
        }
    }

    public static ApplicationInfo getApplicationInfo(Context context, String packageName) {
        synchronized (applicationInfoLists) {
            if (!applicationInfoLists.containsKey(packageName)) {
                ApplicationInfo applicationInfo;
                try {
                    applicationInfo = getPackageManager(context).getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                    applicationInfoLists.put(packageName, applicationInfo);
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                    applicationInfo = null;
                }

                return applicationInfo;
            } else {
                return applicationInfoLists.get(packageName);
            }
        }
    }


    public String getChannelValue(String channelName) {
        String value;
        try {
            value = String.valueOf(applicationInfo.metaData.get(channelName));
        } catch (Exception e) {
            value = null;
            e.printStackTrace();
        }

        return value;
    }
}
