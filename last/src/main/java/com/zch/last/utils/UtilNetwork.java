package com.zch.last.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.listener.Await;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
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
    public static Map<NCT, NetworkInfo> getNetworkConnected(Context context) {
        Map<NCT, NetworkInfo> nctMap = null;
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    nctMap = new HashMap<>();
                    NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (mobileInfo != null && mobileInfo.isConnected()) {
                        nctMap.put(NCT.MOBILE, mobileInfo);
                    }
                    NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (wifiInfo != null && wifiInfo.isConnected()) {
                        nctMap.put(NCT.WIFI, wifiInfo);
                    }
                    NetworkInfo ethernetInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                    if (ethernetInfo != null && ethernetInfo.isConnected()) {
                        nctMap.put(NCT.ETHERNET, ethernetInfo);
                    }
                    NetworkInfo bluetoothInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
                    if (bluetoothInfo != null && bluetoothInfo.isConnected()) {
                        nctMap.put(NCT.BLUETOOTH, bluetoothInfo);
                    }
                    NetworkInfo dummyInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_DUMMY);
                    if (dummyInfo != null && dummyInfo.isConnected()) {
                        nctMap.put(NCT.DUMMY, dummyInfo);
                    }
                    NetworkInfo mobileDunInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE_DUN);
                    if (mobileDunInfo != null && mobileDunInfo.isConnected()) {
                        nctMap.put(NCT.MOBILE_DUN, mobileDunInfo);
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        NetworkInfo vpnInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_VPN);
                        if (vpnInfo != null && vpnInfo.isConnected()) {
                            nctMap.put(NCT.VPN, vpnInfo);
                        }
                    }
                    NetworkInfo wimaxInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
                    if (wimaxInfo != null && wimaxInfo.isConnected()) {
                        nctMap.put(NCT.WIMAX, wimaxInfo);
                    }
                }
            }
        }
        return nctMap;
    }

    public static boolean isInternetConnected(Context context) {
        Map<NCT, NetworkInfo> networkConnected = getNetworkConnected(context);
        if (networkConnected == null) return false;
        return networkConnected.containsKey(NCT.WIFI) || networkConnected.containsKey(NCT.MOBILE);
    }

    /**
     * @return 获得网关ip地址
     */
    @NonNull
    public static String getIp(@NonNull Context context) {
        Map<NCT, NetworkInfo> map = getNetworkConnected(context);
        String ip = null;
        if (map != null) {
            if (map.containsKey(NCT.MOBILE)) {
                //移动网络
                try {
                    Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
                    if (enumeration != null) {
                        while (enumeration.hasMoreElements()) {
                            NetworkInterface anInterface = enumeration.nextElement();
                            Enumeration<InetAddress> inetAddressEnumeration = anInterface.getInetAddresses();
                            if (inetAddressEnumeration != null) {
                                while (inetAddressEnumeration.hasMoreElements()) {
                                    InetAddress inetAddress = inetAddressEnumeration.nextElement();
                                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                                        ip = inetAddress.getHostAddress();
                                        break;
                                    }
                                }
                            }
                            if (ip != null) {
                                break;
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (map.containsKey(NCT.WIFI)) {
                //WIFI网络
                ip = getWLAN_IP(context);
            }
        }
        return ip == null ? "" : ip;
    }

    /**
     * @return 获得网关ip地址
     */
    @NonNull
    public static String getWLAN_IP(@NonNull Context context) {
        String ip = null;
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();

            ip = (ipAddress & 0xFF) + "." +
                    ((ipAddress >> 8) & 0xFF) + "." +
                    ((ipAddress >> 16) & 0xFF) + "." +
                    ((ipAddress >> 24) & 0xFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip == null ? "" : ip;
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
