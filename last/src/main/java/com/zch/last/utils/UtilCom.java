package com.zch.last.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilCom {

    @IntDef({Pattern.CASE_INSENSITIVE, //大小写不敏感
            Pattern.COMMENTS, Pattern.DOTALL, Pattern.LITERAL,
            Pattern.MULTILINE, Pattern.UNICODE_CASE, Pattern.UNIX_LINES,
            0//
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PatternFlag {
    }


    /**
     * @param text  字符串
     * @param regex 正则表达式
     * @param flag  标记
     * @return 是否匹配
     */
    public static boolean matches(String text, String regex, @PatternFlag int flag) {
        if (text == null) {
            return regex == null;
        } else if (regex == null) {
            return false;
        } else if (text.length() == 0 && regex.length() == 0) {
            return true;
        }
        Pattern pattern = Pattern.compile(regex, flag);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    /**
     * 规范数字
     *
     * @param number  数字
     * @param minInt  整数最小保留位数
     * @param minFrac 小数最小保留位数
     * @param maxInt  整数最大保留位数
     * @param maxFrac 小数最大保留位数
     */
    @NonNull
    public static String regNumb(Number number, int minInt, int minFrac, int maxInt, int maxFrac) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        if (minInt >= 0) {
            numberFormat.setMinimumIntegerDigits(minInt);
        }
        if (minFrac >= 0) {
            numberFormat.setMinimumFractionDigits(minFrac);
        }
        if (maxInt >= 0) {
            numberFormat.setMaximumIntegerDigits(maxInt);
        }
        if (maxFrac >= 0) {
            numberFormat.setMaximumFractionDigits(maxFrac);
        }
        return numberFormat.format(number);
    }

    /**
     * @param numb 清除除了数字和小数点外的字符
     */
    @NonNull
    public static String numberRegular(String numb, @Nullable Class<? extends Number> clazz) {
        if (numb == null) {
            return "";
        }
        numb = numb.replaceAll("[^0-9.]", "");
        StringBuilder nb = new StringBuilder(numb);
        int index;
        while ((index = nb.indexOf(".")) > -1) {
            if (index == 0) {
                nb.insert(0, "0");
            } else if (index == nb.length() - 1) {
                nb.append("0");
            } else {
                if (index != nb.lastIndexOf(".")) {
                    String start = nb.substring(0, index + 1);
                    String end = nb.substring(index + 1, nb.length() - 1);
                    end = end.replaceAll("[.]", "");
                    if (end.length() == 0) {
                        end = "0";
                    }
                    nb.delete(0, nb.length());
                    nb.append(start).append(end);
                }
                break;
            }
        }
        String res = nb.toString();
        String clazzName = clazz == null ? null : clazz.getName();
        if (clazzName != null && (
                Byte.class.getName().equals(clazzName) || Integer.class.getName().equals(clazzName) ||
                        Short.class.getName().equals(clazzName) || Long.class.getName().equals(clazzName)
        )) {
            index = res.indexOf(".");
            if (index > -1) {
                res = res.substring(0, index);
            }
        }
        return res;
    }

    /**
     * 改变长度
     *
     * @param increase 长度增长个数
     */
    @NonNull
    public static int[] changeSize(int[] t, int increase) {

        if (t == null) {
            return new int[increase < 0 ? 0 : increase];
        }
        int newSize = t.length + increase;
        if (newSize <= 0) {
            return new int[0];
        }
        int minSize = Math.min(newSize, t.length);
        int[] newArray = new int[minSize];
        System.arraycopy(t, 0, newArray, 0, minSize);
        return newArray;
    }

    public static int parseInt(String text, int def) {
        if (text == null || text.length() == 0) {
            return def;
        }
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            return def;
        }
    }

    public static float parseFloat(String text, float def) {
        if (text == null || text.length() == 0) {
            return def;
        }
        try {
            return Float.parseFloat(text);
        } catch (Exception e) {
            return def;
        }
    }

    public static double parseDouble(String text, double def) {
        if (text == null || text.length() == 0) {
            return def;
        }
        try {
            return Double.parseDouble(text);
        } catch (Exception e) {
            return def;
        }
    }

    @ColorInt
    public static int getColor(Context context, @ColorRes int colorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(colorRes, context.getTheme());
        }
        return context.getResources().getColor(colorRes);
    }

    public static ColorStateList getColorStateList(Context context, @ColorRes int colorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColorStateList(colorRes);
        }
        return context.getResources().getColorStateList(colorRes);
    }


}
