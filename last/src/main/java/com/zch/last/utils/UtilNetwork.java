package com.zch.last.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.listener.Await;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;

/**
 * 网络
 */
public class UtilNetwork {
    private static final String TAG = "UtilNetwork";

    /**
     * Network Connect Type
     */
    public enum NCT {
        NONE, MOBILE, WIFI, ETHERNET, BLUETOOTH, VPN,
        DUMMY, MOBILE_DUN, WIMAX
    }

    /**
     * @return 返回连接的网络
     */
    @Nullable
    @SuppressLint("MissingPermission")
    public static Map<NCT, NetworkInfo> isNetworkConnected(Context context) {
        Map<NCT, NetworkInfo> nctMap = null;
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    nctMap = new HashMap<>();
                    NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (mobileInfo != null && mobileInfo.isAvailable()) {
                        nctMap.put(NCT.MOBILE, mobileInfo);
                    }
                    NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (wifiInfo != null && wifiInfo.isAvailable()) {
                        nctMap.put(NCT.WIFI, wifiInfo);
                    }
                    NetworkInfo ethernetInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                    if (ethernetInfo != null && ethernetInfo.isAvailable()) {
                        nctMap.put(NCT.ETHERNET, ethernetInfo);
                    }
                    NetworkInfo bluetoothInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
                    if (bluetoothInfo != null && bluetoothInfo.isAvailable()) {
                        nctMap.put(NCT.BLUETOOTH, bluetoothInfo);
                    }
                    NetworkInfo dummyInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_DUMMY);
                    if (dummyInfo != null && dummyInfo.isAvailable()) {
                        nctMap.put(NCT.DUMMY, dummyInfo);
                    }
                    NetworkInfo mobileDunInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE_DUN);
                    if (mobileDunInfo != null && mobileDunInfo.isAvailable()) {
                        nctMap.put(NCT.MOBILE_DUN, mobileDunInfo);
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        NetworkInfo vpnInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_VPN);
                        if (vpnInfo != null && vpnInfo.isAvailable()) {
                            nctMap.put(NCT.VPN, vpnInfo);
                        }
                    }
                    NetworkInfo wimaxInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
                    if (wimaxInfo != null && wimaxInfo.isAvailable()) {
                        nctMap.put(NCT.WIMAX, wimaxInfo);
                    }
                }
            }
        }
        return nctMap;
    }

    public static boolean isInternetConnected(Context context) {
        Map<NCT, NetworkInfo> networkConnected = isNetworkConnected(context);
        if (networkConnected == null) return false;
        return networkConnected.containsKey(NCT.WIFI) || networkConnected.containsKey(NCT.MOBILE);
    }

    /**
     * @return 获得网关ip地址
     */
    @NonNull
    public static String getWLAN_IP(@NonNull Application application) {
        try {
            WifiManager wifiManager = (WifiManager) application.getSystemService(Context.WIFI_SERVICE);
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            return Formatter.formatIpAddress(dhcpInfo.gateway);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param listener 回调网关MAC地址
     */
    public static void getWLAN_MAC(@NonNull Application application, final Await<String> listener) {
        final String routeIp = getWLAN_IP(application);
        if (routeIp.length() == 0) {
            if (listener != null) {
                listener.comeUp(null);
            }
            return;
        }
        UtilThread.runOnScheduler(Schedulers.newThread(), new Runnable() {
            @Override
            public void run() {
                BufferedReader bufferedReader = null;
                String mac = null;
                try {
                    Process exec = Runtime.getRuntime().exec("cat proc/net/arp");
                    InputStream inputStream = exec.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (UtilCom.matches(line, "^" + routeIp + "\\s+.*$", 0)) {
                            String[] split = line.split("\\s+");
                            mac = split[3];
                            break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (listener != null) {
                    listener.comeUp(mac);
                }
            }
        });
    }

    public static void main(String[] text) {
        String ip = "192.168.1.1";
        boolean matches = UtilCom.matches("192.168.1.11 fdsfds fsf sdf", "^" + ip + "\\s+.*$", 0);
        System.out.println(matches);
    }
}
