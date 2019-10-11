package com.zch.last.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.core.content.PermissionChecker;

import com.zch.last.model.SIMcard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 设备
 */
public class UtilDevice {

    /**
     * 效率最高，执行10000次大约19ms
     *
     * @return 屏幕宽高
     */
    @NonNull
    public static int[] getScreenSize(Context context) {
        if (context == null) {
            return new int[]{0, 0};
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

//    /**
//     * 执行10000次，大约3800ms
//     *
//     * @return 屏幕宽高
//     */
//    private static int[] getWindowScreenSize(Window window) {
//        WindowManager windowManager = null;
//        try {
//            windowManager = window.getWindowManager();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return getWindowScreenSize(windowManager);
//    }
//
//    /**
//     * 执行10000次，大约3600ms
//     *
//     * @return 屏幕宽高
//     */
//    private static int[] getWindowScreenSize(Context context) {
//        WindowManager windowManager = null;
//        try {
//            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return getWindowScreenSize(windowManager);
//    }
//
//    private static int[] getWindowScreenSize(WindowManager windowManager) {
//        int[] size = new int[2];
//        try {
//            Display defaultDisplay = windowManager.getDefaultDisplay();
//
//            Point outSize = new Point();
//            defaultDisplay.getSize(outSize);
//            size[0] = outSize.x;
//            size[1] = outSize.y;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return size;
//    }

    /**
     * @return 获取android id
     */
    @NonNull
    public static String getAndroidId(Context context) {
        String value = null;
        try {
            //Android ID
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                value = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                value = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value == null ? "" : value;
    }

    /**
     * @return 获取设备id
     */
    @NonNull
    @RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
    public static String getImei(Context context) {
        String value = null;
        try {
            if (PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        value = telephonyManager.getImei();
                    } else {
                        value = telephonyManager.getDeviceId();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value == null ? "" : value;
    }

    /**
     * @return wifi的mac地址
     */
    @NonNull
    public static String getWifiMac(Context context) {
        String value = null;
        try {
            //MAC_WLAN
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//api低于6.0
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    WifiInfo wifiManagerConnectionInfo = wifiManager.getConnectionInfo();
                    value = wifiManagerConnectionInfo.getMacAddress();
                }
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {//api低于7.0
                Process exec = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
                InputStreamReader isr = new InputStreamReader((exec.getInputStream()));
                LineNumberReader lnr = new LineNumberReader(isr);
                for (int i = 0; i < lnr.getLineNumber(); i++) {
                    value = lnr.readLine();
                    if (value != null && value.length() != 0) {
                        value = value.trim();
                        break;
                    }
                }
                //
                if (value == null || value.length() == 0) {
                    value = loadFileAsString("/sys/class/net/eth0/address")
                            .toUpperCase();
                    if (value.length() > 17) {
                        value = value.substring(0, 17);
                    }
                }
            } else {//7.0以上
                // 通过ip地址来获取绑定的mac地址
                value = getWifiMac_GINGERBREAD();
                if (value == null || value.length() == 0) {
                    //扫描各个网络接口获取mac地址
                    value = getMachineHardwareAddress();
                    if (value == null || value.length() == 0) {
                        //通过busybox获取本地存储的mac地址
                        value = getLocalMacAddressFromBusybox();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value == null ? "" : value;
    }

    //----------------------------------------------------------------------------------------------
    //---------------------------------- private ---------------------------------------------------
    //----------------------------------------------------------------------------------------------


    /**
     * @return api27以上获取wifi mac,通过ip地址来获取绑定的mac地址
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private static String getWifiMac_GINGERBREAD() {
        String value = null;
        InetAddress ip = getInetAddress();
        try {
            NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(ip);
            byte[] hardwareAddress = byInetAddress.getHardwareAddress();
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < hardwareAddress.length; i++) {
                if (i != 0) {
                    buffer.append(":");
                }
                String str = Integer.toHexString(hardwareAddress[i] & 0xFF);
                buffer.append(str.length() == 1 ? "0" + str : str);
            }
            value = buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value == null ? "" : value;
    }

    /**
     * @return 扫描各个网络接口获取mac地址
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private static String getMachineHardwareAddress() {
        String hardWareAddress = null;

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            NetworkInterface iF;
            while (interfaces.hasMoreElements()) {
                iF = interfaces.nextElement();
                try {
                    hardWareAddress = UtilObject.byte2hex(iF.getHardwareAddress());
                    if (hardWareAddress.length() != 0)
                        break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hardWareAddress == null ? "" : hardWareAddress;
    }

    /**
     * @return 通过busybox获取本地存储的mac地址, 需要root
     */
    private static String getLocalMacAddressFromBusybox() {
        String result = callCmd("busybox ifconfig", "HWaddr");
        if (result.length() == 0) {
            return "";
        }
        String mac = null;
        try {
            if (result.contains("HWaddr")) {
                int index = result.indexOf("HWaddr");
                mac = result.substring(index + 6, result.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac == null ? "" : mac;
    }

    private static String callCmd(String cmd, String filter) {
        StringBuilder result = new StringBuilder();
        String line;
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && !line.contains(filter)) {
                result.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    @Nullable
    private static InetAddress getInetAddress() {
        InetAddress ip = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    ip = inetAddresses.nextElement();
                    if (!ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")) {
                        break;
                    } else {
                        ip = null;
                    }
                }
                if (ip != null) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }
}
