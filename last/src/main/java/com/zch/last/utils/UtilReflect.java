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
        return (T) newInstance(clazz, classArr, params);

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
        T newInstance = null;

        try {
            constructor.setAccessible(true);
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
     * @return 返回值
     */
    @Nullable
    public static <T> T call(@NonNull Object obj, @NonNull String methodName,@Nullable Class<?>[] fieldClasses, boolean declared, Object... fields) {
        Class<?> clazz = obj.getClass();
        Method method;
        try {
            if (declared) {
                method = clazz.getDeclaredMethod(methodName, fieldClasses);
            } else {
                method = clazz.getMethod(methodName, fieldClasses);
            }
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
     * 调用父类无法正常访问到的方法（private，protected）
     */
    public static <T> T callSuperMethod(Object obj, SearchComparable<Method> comparable, Object... args) {
        if (obj == null || comparable == null) return null;
        Method method = findMethod(obj, comparable);
        if (method == null) return null;
        T invoke = null;
        try {
            invoke = (T) method.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoke;
    }

    /**
     * @param obj       某类对象
     * @param fieldName 成员变量名
     * @param <T>       某类
     * @return 获取成员变量值
     */
    @Nullable
    public static <T> T getField(@NonNull Object obj, @NonNull String fieldName, boolean declared) {
        if (fieldName.length() == 0) return null;
        Class<?> clazz = obj.getClass();
        Field field;
        try {
            if (declared) {
                field = clazz.getDeclaredField(fieldName);
            } else {
                field = clazz.getField(fieldName);
            }
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
     * @param obj        某类对象
     * @param value      修改值
     * @param comparable 对比找到参数
     * @return 设置结果
     */
    public static boolean setFieldValue(Object obj, Object value, SearchComparable<Field> comparable) {
        if (obj == null || comparable == null) return false;
        Object field = findField(obj, comparable);
        if (field == null) return false;
        if (comparable.dataFrom == null) return false;
        try {
            comparable.dataFrom.set(field, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param object     对象
     * @param comparable 对比
     */
    @Nullable
    public static <T> T findField(Object object, SearchComparable<Field> comparable) {
        if (object == null || comparable == null) return null;
        Class<?> findClass = object.getClass();
        Field[] fields;
        T data = null;
        boolean findOut = false;
        while (findClass != null) {
            fields = findClass.getDeclaredFields();
            if (fields.length == 0) return null;
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    if (comparable.findOut(field)) {
                        data = (T) field.get(object);
                        comparable.dataFrom = field;
                        findOut = true;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (findOut) {
                break;
            }
            if (comparable.findSuper()) {
                findClass = findClass.getSuperclass();
            }
        }
        return data;
    }

    /**
     * @param object     对象
     * @param comparable 对比
     */
    @Nullable
    public static Method findMethod(Object object, SearchComparable<Method> comparable) {
        if (object == null || comparable == null) return null;
        Class<?> findClass = object.getClass();
        Method[] methods;
        while (findClass != null) {
            methods = findClass.getDeclaredMethods();
            if (methods.length == 0) return null;
            for (Method method : methods) {
                try {
                    method.setAccessible(true);
                    if (comparable.findOut(method)) {
                        comparable.dataFrom = method;
                        return method;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (comparable.findSuper()) {
                findClass = findClass.getSuperclass();
            }
        }
        return null;
    }

    public static abstract class SearchComparable<T> {
        private boolean findSuper = false;
        @Nullable
        public T dataFrom;

        public SearchComparable() {
        }

        public SearchComparable(boolean findSuper) {
            this.findSuper = findSuper;
        }

        /**
         * @return 到父类中找
         */
        public boolean findSuper() {
            return findSuper;
        }

        /**
         * @param obj 当前对比对象
         * @return 找到返回true
         */
        public abstract boolean findOut(@NonNull T obj);
    }
}
