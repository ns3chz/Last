//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zch.last.utils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UtilReflect {
//    private UtilReflect() throws Exception {
//        throw new Exception("unexpect construct class");
//    }

    /**
     * 创建新对象
     *
     * @param object   老对象
     * @param classArr 构造参数类型集合
     * @param params   构造参数集合
     * @param <T>      某类
     * @return 返回新对象
     */
    @Nullable
    public static <T> T newInstance(@NonNull Object object, Class<?>[] classArr, Object... params) {
        Class<?> clazz = object.getClass();
        T o = null;
        try {
            o = (T) newInstance(clazz, classArr, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;

    }

    /**
     * 创建新对象
     *
     * @param clazz    某类类型
     * @param classArr 构造参数类型集合
     * @param params   构造参数集合
     * @param <T>      某类
     * @return 返回新对象
     */
    @Nullable
    public static <T> T newInstance(@NonNull Class<T> clazz, Class<?>[] classArr, Object... params) {
        Constructor constructor;
        try {
            constructor = clazz.getDeclaredConstructor(classArr);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        return newInstance(constructor, params);

    }

    /**
     * 创建新对象
     *
     * @param constructor 构造函数
     * @param params      构造参数
     * @param <T>         某类
     * @return 返回新对象
     */
    @Nullable
    public static <T> T newInstance(@NonNull Constructor constructor, Object... params) {
        constructor.setAccessible(true);
        T newInstance = null;

        try {
            newInstance = (T) constructor.newInstance(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newInstance;
    }

    /**
     * 调用某类的方法
     *
     * @param obj          某类对象
     * @param methodName   方法名
     * @param fieldClasses 参数返回类型
     * @param fields       方法需要的参数
     * @param <T>          某类
     */
    @Nullable
    public static <T> T call(@NonNull Object obj, String methodName, Class<?>[] fieldClasses, Object... fields) {
        Class<?> tClass = obj.getClass();
        Method method;
        try {
            method = tClass.getDeclaredMethod(methodName, fieldClasses);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        T t = null;
        try {
            method.setAccessible(true);
            t = (T) method.invoke(obj, fields);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 调用某类的方法
     *
     * @param obj          某类对象
     * @param clazz        父类
     * @param methodName   方法名
     * @param fieldClasses 参数返回类型
     * @param fields       方法需要的参数
     * @param <T>          某类
     * @return 返回值
     */
    @Nullable
    public static <T> T call(@NonNull Object obj, @NonNull Class<?> clazz, @NonNull String methodName, Class<?>[] fieldClasses, Object... fields) {
        Method method;
        try {
            method = clazz.getDeclaredMethod(methodName, fieldClasses);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        T t = null;
        try {
            method.setAccessible(true);
            t = (T) method.invoke(obj, fields);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * @param obj       某类对象
     * @param fieldName 成员变量名
     * @param <T>       某类
     * @return 获取成员变量值
     */
    @Nullable
    public static <T> T getField(@NonNull Object obj, @NonNull String fieldName) {
        if (fieldName.length() == 0) return null;
        Class<?> aClass = obj.getClass();
        Field field;
        try {
            field = aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
        T t = null;
        try {
            field.setAccessible(true);
            t = (T) field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * @param obj       某类对象
     * @param clazz     父类
     * @param fieldName 成员变量名
     * @param <T>       某类
     * @return 获取成员变量值
     */
    @Nullable
    public static <T> T getField(@NonNull Object obj, @NonNull Class<?> clazz, @NonNull String fieldName) {
        if (fieldName.length() == 0) return null;
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
        T t = null;
        try {
            field.setAccessible(true);
            t = (T) field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 设置成员变量值
     *
     * @param obj       某类对象
     * @param fieldName 成员变量名
     * @param value     修改值
     */
    public static void setField(@NonNull Object obj, @NonNull String fieldName, Object value) {
        if (fieldName.length() == 0) return;
        Class<?> aClass = obj.getClass();
        Field field;
        try {
            field = aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param obj       某类对象
     * @param clazz     父类
     * @param fieldName 成员变量名
     * @param value     修改值
     */
    public static void setField(@NonNull Object obj, @NonNull Class<?> clazz, @NonNull String fieldName, Object value) {
        if (fieldName.length() == 0) return;
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
