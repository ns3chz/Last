package com.zch.last.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class UtilPermission {
    private static class Holder {
        static List<Request> permissionRequestList = new ArrayList<>();
    }

    public static Request request(@NonNull Activity activity, int requestCode, @NonNull String[] requestPermission, OnPermissionRequestListener listener) {
        Request request = new Request(requestCode, requestPermission, listener);
        Holder.permissionRequestList.add(request);
        request.request(activity);
        return request;
    }

    public static Request request(@NonNull Fragment fragment, int requestCode, @NonNull String[] requestPermission, OnPermissionRequestListener listener) {
        Request request = new Request(requestCode, requestPermission, listener);
        Holder.permissionRequestList.add(request);
        request.request(fragment);
        return request;
    }

    public static Request request(@NonNull android.app.Fragment fragment, int requestCode, @NonNull String[] requestPermission, OnPermissionRequestListener listener) {
        Request request = new Request(requestCode, requestPermission, listener);
        Holder.permissionRequestList.add(request);
        request.request(fragment);
        return request;
    }

    /**
     * {@link Activity#onRequestPermissionsResult(int, String[], int[])}
     */
    public static void listen(@NonNull Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        listenObject(activity, requestCode, permissions, grantResults);
    }

    public static void listen(@NonNull Fragment fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        listenObject(fragment, requestCode, permissions, grantResults);
    }

    public static void listen(@NonNull android.app.Fragment fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        listenObject(fragment, requestCode, permissions, grantResults);
    }

    private static void listenObject(@NonNull Object object, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = Holder.permissionRequestList.size() - 1; i >= 0; i--) {
            Request request = Holder.permissionRequestList.get(i);
            if (request.listen(object, requestCode, permissions, grantResults)) {
                Holder.permissionRequestList.remove(request);
            } else {
                if (request.target == null) continue;
                if (request.target instanceof Activity) {
                    if (((Activity) request.target).isFinishing()) {
                        Holder.permissionRequestList.remove(request);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (((Activity) request.target).isDestroyed()) {
                            Holder.permissionRequestList.remove(request);
                        }
                    }
                } else if (request.target instanceof Fragment) {
                    if (((Fragment) request.target).isRemoving() || ((Fragment) request.target).isDetached()) {
                        Holder.permissionRequestList.remove(request);
                    }
                } else if (request.target instanceof android.app.Fragment) {
                    if (((android.app.Fragment) request.target).isRemoving() ||
                            ((android.app.Fragment) request.target).isDetached()) {
                        Holder.permissionRequestList.remove(request);
                    }
                }
            }
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
    private static List<String>[] splitPermissions(@Nullable Context context, @NonNull String... permissions) {
        if (context == null) {
            return new List[]{null, null};
        }
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
        @Nullable
        private OnPermissionRequestListener onPermissionRequestListener;
        private int requestCode;
        private Object target;

        Request(int code, @NonNull String[] permissions, @Nullable OnPermissionRequestListener listener) {
            this.requestCode = code;
            this.permissions = permissions;
            this.onPermissionRequestListener = listener;
        }

        /**
         * @param activity 开始请求权限
         */
        void request(@NonNull Activity activity) {
            target = activity;
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
        }

        void request(@NonNull Fragment fragment) {
            target = fragment;
            if (permissions == null || permissions.length == 0) return;
            List<String>[] splitPermissions = splitPermissions(fragment.getContext(), permissions);
            if (splitPermissions[1] == null || splitPermissions[1].size() == 0) {
                //全部都被授权时
                if (onPermissionRequestListener != null) {
                    onPermissionRequestListener.listen(requestCode, permissions, splitPermissions[0], splitPermissions[1]);
                }
                return;
            }
            fragment.requestPermissions(permissions, requestCode);
        }

        void request(@NonNull android.app.Fragment fragment) {
            target = fragment;
            if (permissions == null || permissions.length == 0) return;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (onPermissionRequestListener != null) {
                    onPermissionRequestListener.listen(requestCode, permissions, null, null);
                }
                return;
            }
            List<String>[] splitPermissions = splitPermissions(fragment.getContext(), permissions);
            if (splitPermissions[1] == null || splitPermissions[1].size() == 0) {
                //全部都被授权时
                if (onPermissionRequestListener != null) {
                    onPermissionRequestListener.listen(requestCode, permissions, splitPermissions[0], splitPermissions[1]);
                }
                return;
            }
            fragment.requestPermissions(permissions, requestCode);
        }

        /**
         * {@link Activity#onRequestPermissionsResult}
         *
         * @param object      activity or fragment
         * @param requestCode {@link Activity#onRequestPermissionsResult->requestCode}
         */
        boolean listen(Object object, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (UtilObject.equals(object, target) && this.requestCode == requestCode) {
                List<String>[] splitPermissions = splitPermissions(permissions, grantResults);
                if (onPermissionRequestListener != null) {
                    onPermissionRequestListener.listen(requestCode, permissions, splitPermissions[0], splitPermissions[1]);
                }
                return true;
            }
            return false;
        }

    }

    public interface OnPermissionRequestListener {
        /**
         * @param requestCode        请求码
         * @param requestPermissions 请求权限
         * @param grantedPermissions 被允许的权限
         * @param deniedPermissions  被拒绝的权限
         */
        void listen(int requestCode, @Nullable String[] requestPermissions, @Nullable List<String> grantedPermissions, @Nullable List<String> deniedPermissions);
    }
}
