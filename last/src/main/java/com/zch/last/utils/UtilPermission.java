package com.zch.last.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UtilPermission {
    private static class Holder {
        static ConcurrentMap<Integer, Request> permissionRequestMap = new ConcurrentHashMap<>();
    }

    public static void request(@NonNull Activity activity, int requestCode, @NonNull String[] requestPermission, OnPermissionRequestListener listener) {
        Request request = new Request(requestCode, requestPermission, listener);
        Holder.permissionRequestMap.put(requestCode, request);
        request.request(activity);
    }

    /**
     * 写在基类{@link Activity#onRequestPermissionsResult(int, String[], int[])}
     */
    public static void listen(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Request request = Holder.permissionRequestMap.get(requestCode);
        if (request != null) {
            Holder.permissionRequestMap.remove(requestCode);
            request.listen(requestCode, permissions, grantResults);
        }
    }

    /**
     * @param permission 权限
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        return PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
    }

    /**
     * 拆分已授权和未授权权限
     *
     * @return {已授权，未授权}
     */
    @NonNull
    private static List<String>[] splitPermissions(@NonNull Context context, @NonNull String... permissions) {
        List<String> grantedList = null;
        List<String> deniedList = null;
        String permis;
        boolean granted;
        for (int i = 0; i < permissions.length; i++) {
            permis = permissions[i];
            if (permis == null || permis.length() == 0) continue;
            granted = hasPermission(context, permis);
            if (granted) {
                //已授权
                if (grantedList == null) {
                    grantedList = new ArrayList<>();
                }
                grantedList.add(permis);
            } else {
                //未授权
                if (deniedList == null) {
                    deniedList = new ArrayList<>();
                }
                deniedList.add(permis);
            }
        }
        return new List[]{grantedList, deniedList};
    }

    /**
     * 通过返回结果
     * 拆分已授权和未授权权限
     *
     * @param permissions  请求的权限数组
     * @param grantResults 请求的结果数组，与权限对应
     * @return {已授权，未授权}
     * {@link android.content.pm.PackageManager#PERMISSION_GRANTED,@link PackageManager#PERMISSION_DENIED}. Never null.
     */
    @NonNull
    private static List<String>[] splitPermissions(@NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> grantedList = null;
        List<String> deniedList = null;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                //已授权
                if (grantedList == null) {
                    grantedList = new ArrayList<>();
                }
                grantedList.add(permissions[i]);
            } else {
                //未授权
                if (deniedList == null) {
                    deniedList = new ArrayList<>();
                }
                deniedList.add(permissions[i]);
            }
        }

        return new List[]{grantedList, deniedList};
    }

    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------
    //-----------------------------------------------权限申请对象--------------------------------------------------
    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------

    /**
     * 包装请求的权限和请求的回调
     */
    private static class Request {
        private String[] permissions;
        private OnPermissionRequestListener onPermissionRequestListener;
        private int requestCode;

        public Request(int code, @NonNull String[] permissions, OnPermissionRequestListener listener) {
            this.requestCode = code;
            this.permissions = permissions;
            this.onPermissionRequestListener = listener;
        }

        /**
         * @param activity 开始请求权限
         */
        public void request(@NonNull Activity activity) {
            if (permissions == null || permissions.length == 0) return;
            List<String>[] splitPermissions = splitPermissions(activity, permissions);
            if (splitPermissions[1] == null || splitPermissions[1].size() == 0) {
                //全部都被授权时
                if (onPermissionRequestListener != null) {
                    onPermissionRequestListener.listen(requestCode, permissions, splitPermissions[0], splitPermissions[1]);
                }
                return;
            }
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
//            ActivityCompat.requestPermissions(activity, splitPermissions[1].toArray(deniedPermission), requestCode);
        }

        /**
         * {@link Activity#onRequestPermissionsResult}
         *
         * @param requestCode {@link Activity#onRequestPermissionsResult->requestCode}
         */
        public void listen(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == requestCode && onPermissionRequestListener != null) {
                List<String>[] splitPermissions = splitPermissions(permissions, grantResults);
                onPermissionRequestListener.listen(requestCode, permissions, splitPermissions[0], splitPermissions[1]);
            }
        }


    }

    public interface OnPermissionRequestListener {
        /**
         * @param requestCode        请求码
         * @param requestPermissions 请求权限
         * @param grantedPermissions 被允许的权限
         * @param deniedPermissions  被拒绝的权限
         */
        public void listen(int requestCode, @Nullable String[] requestPermissions, @Nullable List<String> grantedPermissions, @Nullable List<String> deniedPermissions);
    }
}
