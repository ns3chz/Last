//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zch.last.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.zch.last.utils.UtilAnalysis;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SpManager {

    private static final ConcurrentMap<String, SharedPreferences> spList = new ConcurrentHashMap<>();
    private static final ConcurrentMap<SharedPreferences, Editor> editorList = new ConcurrentHashMap<>();
    private static SharedPreferences sp;
    private static Editor mEditor;

    public static SharedPreferences getSp(Context context, String spName) {
        if (context == null) {
            return null;
        } else {
            if (!spList.containsKey(spName)) {
                sp = context.getSharedPreferences(spName, 0);
                spList.put(spName, sp);
                return sp;
            } else {
                return spList.get(spName);
            }
        }
    }

    public static Editor getEditor(SharedPreferences spre) {
        if (spre == null) {
            return null;
        } else {
            if (!editorList.containsKey(spre)) {
                mEditor = spre.edit();
                editorList.put(spre, mEditor);
                return mEditor;
            } else {
                return editorList.get(spre);
            }
        }
    }

    public static void putString(Context context, String spName, String key, String value) {
        if (context != null) {
            sp = getSp(context, spName);
            mEditor = getEditor(sp);
            mEditor.putString(key, value);
            mEditor.commit();
        } else {
            Log.e("SharedPreferenceManager", "SharedPreferenceManager HandleSharePre:context is null!!!");
        }

    }

    public static String getString(Context context, String spName, String key, String defValue) {
        try {
            sp = getSp(context, spName);
            return sp.getString(key, defValue);
        } catch (Exception var5) {
            var5.printStackTrace();
            return defValue;
        }
    }

    public static void putInt(Context context, String spName, String key, int value) {
        if (context != null) {
            sp = getSp(context, spName);
            mEditor = getEditor(sp);
            mEditor.putInt(key, value);
            mEditor.commit();
        } else {
            Log.e("SharedPreferenceManager", "SharedPreferenceManager HandleSharePre:context is null!!!");
        }

    }

    public static int getInt(Context context, String spName, String key, int defValue) {
        try {
            sp = getSp(context, spName);
            return sp.getInt(key, defValue);
        } catch (Exception var5) {
            var5.printStackTrace();
            return defValue;
        }
    }

    public static void putBoolean(Context context, String spName, String key, boolean value) {
        if (context != null) {
            sp = getSp(context, spName);
            mEditor = getEditor(sp);
            mEditor.putBoolean(key, value);
            mEditor.commit();
        } else {
            Log.e("SharedPreferenceManager", "SharedPreferenceManager HandleSharePre:context is null!!!");
        }

    }

    public static boolean getBoolean(Context context, String spName, String key, boolean defValue) {
        try {
            sp = getSp(context, spName);
            return sp.getBoolean(key, defValue);
        } catch (Exception var5) {
            var5.printStackTrace();
            return defValue;
        }
    }

    public static void putLong(Context context, String spName, String key, long value) {
        if (context != null) {
            sp = getSp(context, spName);
            mEditor = getEditor(sp);
            mEditor.putLong(key, value);
            mEditor.commit();
        } else {
            Log.e("SharedPreferenceManager", "SharedPreferenceManager HandleSharePre:context is null!!!");
        }

    }

    public static long getLong(Context context, String spName, String key, long defValue) {
        try {
            sp = getSp(context, spName);
            return sp.getLong(key, defValue);
        } catch (Exception var6) {
            var6.printStackTrace();
            return defValue;
        }
    }

    public static void putFloat(Context context, String spName, String key, float value) {
        if (context != null) {
            sp = getSp(context, spName);
            mEditor = getEditor(sp);
            mEditor.putFloat(key, value);
            mEditor.commit();
        } else {
            Log.e("SharedPreferenceManager", "SharedPreferenceManager HandleSharePre:context is null!!!");
        }

    }

    public static float getFloat(Context context, String spName, String key, float defValue) {
        try {
            sp = getSp(context, spName);
            return sp.getFloat(key, defValue);
        } catch (Exception var5) {
            var5.printStackTrace();
            return defValue;
        }
    }

    public static <T> void putBean(Context context, String spName, String key, T t) {
        if (context != null) {
            sp = getSp(context, spName);
            mEditor = getEditor(sp);
            mEditor.putString(key, UtilAnalysis.parseObject2JsonString(t));
            mEditor.commit();
        } else {
            Log.e("SharedPreferenceManager", "SharedPreferenceManager HandleSharePre:context is null!!!");
        }

    }

    public static <T> T getBean(Context context, String spName, String key, String defBeanJson, Class<T> clazz) {
        try {
            sp = getSp(context, spName);
            return UtilAnalysis.parseJson2Bean(sp.getString(key, defBeanJson), clazz);
        } catch (Exception var6) {
            var6.printStackTrace();
            return UtilAnalysis.parseJson2Bean(defBeanJson, clazz);
        }
    }

    public static <T> T getBean(Context context, String spName, String key, String defBeanJson, Type type) {
        try {
            sp = getSp(context, spName);
            return UtilAnalysis.parseJson2Bean(sp.getString(key, defBeanJson), type);
        } catch (Exception var6) {
            var6.printStackTrace();
            return UtilAnalysis.parseJson2Bean(defBeanJson, type);
        }
    }
}
