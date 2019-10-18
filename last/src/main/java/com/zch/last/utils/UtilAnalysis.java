package com.zch.last.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * 解析工具
 */
public class UtilAnalysis {

    /**
     * parse Json to class
     *
     * @param clazz bean
     */
    @Nullable
    public static <T> T parseJson2Bean(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Gson mGson = new GsonBuilder().disableHtmlEscaping().create();//不进行unicode转译
        T t = null;
        try {
            t = mGson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            Log.e("parseJson2Bean", "parseJson2Bean...requestFailed!!!");
            e.printStackTrace();
        }
        return t;
    }

    @Nullable
    public static <T> T parseJson2Bean(String json, Type type) {
        if (json == null || json.length() == 0) {
            return null;
        }
        Gson mGson = new GsonBuilder().disableHtmlEscaping().create();//不进行unicode转译
        T t = null;
        try {
            t = mGson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            Log.e("parseJson2Bean", "parseJson2Bean...requestFailed!!!");
            e.printStackTrace();
        }
        return t;
    }

    @NonNull
    public static String parseObject2JsonString(Object bean) {
        if (bean == null) {
            return "";
        }
        Gson mGson = new GsonBuilder().disableHtmlEscaping().create();//不进行unicode转译
        String mJson = null;
        try {
            mJson = mGson.toJson(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mJson == null ? "" : mJson;
    }

    /**
     * 按步数扰乱字符串
     */
    public static class EncodeSegment {
        @NonNull
        public static <T> T[] encode(@NonNull T[] ts) {
            if (ts.length == 0) return ts;
            int halfLen = ts.length / 2;
            int index = 0, segTemp = 0, seg = 1;
            T temp = null;
            while (seg <= halfLen) {
                ts = encode(ts, seg, index, temp, segTemp);
                seg++;
            }
            return ts;
        }

        @NonNull
        public static <T> T[] decode(@NonNull T[] ts) {
            if (ts.length == 0) return ts;
            int index = 0, segTemp = 0, seg = ts.length / 2;
            T temp = null;
            while (seg > 0) {
                ts = encode(ts, seg, index, temp, segTemp);
                seg--;
            }
            return ts;
        }

        @NonNull
        private static <T> T[] encode(@NonNull T[] ts, int seg, int index, @Nullable T temp, int segTemp) {
            index = -1;
            while ((index + 2 * seg) < ts.length) {
                for (int i = 0; i < seg; i++) {
                    index++;
                    segTemp = index + seg;
                    temp = ts[index];
                    ts[index] = ts[segTemp];
                    ts[segTemp] = temp;
                }
                index += seg;
            }
            return ts;
        }
    }

}
