package com.zch.last.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.utils.UtilReflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SIMcard extends BaseCloneableModel {
    private static final long serialVersionUID = 8434754363571285562L;

    public static final String URI_STR = "content://telephony/siminfo";

    /**
     * 查询key表
     */
    public static final class COLUMN {
        @Nullable
        private static String[] VALUES;

        public static final String _id = "_id";//sub_id
        public static final String icc_id = "icc_id";
        public static final String sim_id = "sim_id";//卡位
        public static final String display_name = "display_name";//显示的名字
        public static final String carrier_name = "carrier_name";//运营商名字
        public static final String name_source = "name_source";//
        public static final String color = "color";//例：color=-16746133 -> color=FFFF FFFF FF00 796B -> #FF00796B
        public static final String number = "number";//
        public static final String display_number_format = "display_number_format";//
        public static final String data_roaming = "data_roaming";//
        public static final String mcc = "mcc";//
        public static final String mnc = "mnc";//
        public static final String sim_provisioning_status = "sim_provisioning_status";//
        public static final String is_embedded = "is_embedded";//
        public static final String card_id = "card_id";//
        public static final String access_rules = "access_rules";//
        public static final String is_removable = "is_removable";//
        public static final String enable_cmas_extreme_threat_alerts = "enable_cmas_extreme_threat_alerts";//
        public static final String enable_cmas_severe_threat_alerts = "enable_cmas_severe_threat_alerts";//
        public static final String enable_cmas_amber_alerts = "enable_cmas_amber_alerts";//
        public static final String enable_emergency_alerts = "enable_emergency_alerts";//
        public static final String alert_sound_duration = "alert_sound_duration";//
        public static final String alert_reminder_interval = "alert_reminder_interval";//
        public static final String enable_alert_vibrate = "enable_alert_vibrate";//
        public static final String enable_alert_speech = "enable_alert_speech";//
        public static final String enable_etws_test_alerts = "enable_etws_test_alerts";//
        public static final String enable_channel_50_alerts = "enable_channel_50_alerts";//
        public static final String enable_cmas_test_alerts = "enable_cmas_test_alerts";//
        public static final String show_cmas_opt_out_dialog = "show_cmas_opt_out_dialog";//
        public static final String volte_vt_enabled = "volte_vt_enabled";//
        public static final String vt_ims_enabled = "vt_ims_enabled";//
        public static final String wfc_ims_enabled = "wfc_ims_enabled";//
        public static final String wfc_ims_mode = "wfc_ims_mode";//
        public static final String wfc_ims_roaming_mode = "wfc_ims_roaming_mode";//
        public static final String wfc_ims_roaming_enabled = "wfc_ims_roaming_enabled";//

        @NonNull
        public static String[] getValues() {
            if (VALUES != null) {
                return VALUES;
            }
            Class<COLUMN> columnClass = COLUMN.class;
            Field[] fields = columnClass.getDeclaredFields();
            String[] values = new String[fields.length];
            Class<?> type;
            int index = 0;
            for (int i = 0; i < fields.length; i++) {
                type = fields[i].getType();
                if (!String.class.equals(type) || type.isArray()) continue;
                fields[i].setAccessible(true);
                try {
                    values[index] = (String) fields[i].get(null);
                    index++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            VALUES = new String[index];
            System.arraycopy(values, 0, VALUES, 0, VALUES.length);
            return VALUES;
        }
    }

    public String imsi;

    public int _id;//sub_id
    public String icc_id;
    public int sim_id;
    public String display_name;//运营商名字

    public String carrier_name;//
    public String name_source;//
    public String color;//
    public String number;//
    public String display_number_format;//
    public String data_roaming;//
    public String mcc;//
    public String mnc;//
    public String sim_provisioning_status;//
    public String is_embedded;//
    public String card_id;//
    public String access_rules;//
    public String is_removable;//
    public String enable_cmas_extreme_threat_alerts;//
    public String enable_cmas_severe_threat_alerts;//
    public String enable_cmas_amber_alerts;//
    public String enable_emergency_alerts;//
    public String alert_sound_duration;//
    public String alert_reminder_interval;//
    public String enable_alert_vibrate;//
    public String enable_alert_speech;//
    public String enable_etws_test_alerts;//
    public String enable_channel_50_alerts;//
    public String enable_cmas_test_alerts;//
    public String show_cmas_opt_out_dialog;//
    public String volte_vt_enabled;//
    public String vt_ims_enabled;//
    public String wfc_ims_enabled;//
    public String wfc_ims_mode;//
    public String wfc_ims_roaming_mode;//
    public String wfc_ims_roaming_enabled;//


    /**
     * @return 得到基本的卡信息
     */
    public static List<SIMcard> getSimpleInfos(Context context) {
        return getInfos(context, COLUMN._id, COLUMN.icc_id, COLUMN.sim_id, COLUMN.display_name, COLUMN.carrier_name);
    }

    @Nullable
    public static List<SIMcard> getInfos(Context context, String... qStr) {
        if (qStr == null) {
            qStr = new String[0];
        }
        List<String> queryString = checkQueryStringArray(qStr);
        Cursor cursor = null;
        Uri uri = Uri.parse(URI_STR);
        try {
            ContentResolver contentResolver = context.getContentResolver();
            cursor = contentResolver.query(uri, queryString.toArray(qStr),
                    "0=0", new String[0], null);
            List<SIMcard> list = new ArrayList<>();
            if (cursor != null) {
                Class<SIMcard> siMcardClass = SIMcard.class;
                int columnIndex;
                Field field;
                while (cursor.moveToNext()) {
                    SIMcard siMcard = new SIMcard();
                    for (String column : queryString) {
                        try {
                            columnIndex = cursor.getColumnIndex(column);
                            field = siMcardClass.getField(column);
                            field.setAccessible(true);
                            Object fieldValue = null;
                            if (String.class.equals(field.getType())) {
                                fieldValue = cursor.getString(columnIndex);
                            } else if (int.class.equals(field.getType())) {
                                fieldValue = cursor.getInt(columnIndex);
                            }
                            if (fieldValue != null) {
                                field.set(siMcard, fieldValue);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //获取imsi
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        siMcard.imsi = UtilReflect.call(telephonyManager,
                                "getSubscriberId", new Class[]{int.class}, siMcard._id);
                    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                        siMcard.imsi = UtilReflect.call(telephonyManager,
                                "getSubscriberId", new Class[]{long.class}, siMcard._id);
                    }
                    list.add(siMcard);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @NonNull
    private static List<String> checkQueryStringArray(@NonNull String... queryString) {
        if (queryString.length == 0) {
            return new ArrayList<>(Arrays.asList(COLUMN.getValues()));
        }
        return new ArrayList<>(Arrays.asList(queryString));
    }

    @Nullable
    private static List<SIMcard> getInfos(Context context) {
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(URI_STR);
            ContentResolver contentResolver = context.getContentResolver();
            cursor = contentResolver.query(uri,
                    new String[0], "0=0",
                    new String[0], null);
//            cursor = contentResolver.query(uri,
//                    new String[]{"_id", "sim_id", "icc_id", "display_name"}, "0=0",
//                    new String[]{}, null);
            List<SIMcard> list = new ArrayList<>();
            if (null != cursor) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                while (cursor.moveToNext()) {
                    SIMcard siMcard = new SIMcard();
                    try {
                        //获取sub_id
                        siMcard._id = cursor.getInt(cursor.getColumnIndex(COLUMN._id));
                        //获取iccid
                        siMcard.icc_id = cursor.getString(cursor.getColumnIndex(COLUMN.icc_id));
                        //获取sim_id
                        siMcard.sim_id = cursor.getInt(cursor.getColumnIndex(COLUMN.sim_id));
                        //获取运营商名称
                        siMcard.display_name = cursor.getString(cursor.getColumnIndex(COLUMN.display_name));
                        //获取imsi
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            siMcard.imsi = UtilReflect.call(telephonyManager,
                                    "getSubscriberId", new Class[]{int.class}, siMcard._id);
                        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            siMcard.imsi = UtilReflect.call(telephonyManager,
                                    "getSubscriberId", new Class[]{long.class}, siMcard._id);
                        }
                        siMcard.carrier_name = cursor.getString(cursor.getColumnIndex(COLUMN.carrier_name));
                        //
                        siMcard.name_source = cursor.getString(cursor.getColumnIndex(COLUMN.name_source));
                        siMcard.color = cursor.getString(cursor.getColumnIndex(COLUMN.color));
                        siMcard.number = cursor.getString(cursor.getColumnIndex(COLUMN.number));
                        siMcard.display_number_format = cursor.getString(cursor.getColumnIndex(COLUMN.display_number_format));
                        siMcard.data_roaming = cursor.getString(cursor.getColumnIndex(COLUMN.data_roaming));
                        siMcard.mcc = cursor.getString(cursor.getColumnIndex(COLUMN.mcc));
                        siMcard.mnc = cursor.getString(cursor.getColumnIndex(COLUMN.mnc));
                        siMcard.sim_provisioning_status = cursor.getString(cursor.getColumnIndex(COLUMN.sim_provisioning_status));
                        siMcard.is_embedded = cursor.getString(cursor.getColumnIndex(COLUMN.is_embedded));
                        siMcard.card_id = cursor.getString(cursor.getColumnIndex(COLUMN.card_id));
                        siMcard.access_rules = cursor.getString(cursor.getColumnIndex(COLUMN.access_rules));
                        siMcard.is_removable = cursor.getString(cursor.getColumnIndex(COLUMN.is_removable));
                        siMcard.enable_cmas_extreme_threat_alerts = cursor.getString(cursor.getColumnIndex(COLUMN.enable_cmas_extreme_threat_alerts));
                        siMcard.enable_cmas_severe_threat_alerts = cursor.getString(cursor.getColumnIndex(COLUMN.enable_cmas_severe_threat_alerts));
                        siMcard.enable_cmas_amber_alerts = cursor.getString(cursor.getColumnIndex(COLUMN.enable_cmas_amber_alerts));
                        siMcard.enable_emergency_alerts = cursor.getString(cursor.getColumnIndex(COLUMN.enable_emergency_alerts));
                        siMcard.alert_sound_duration = cursor.getString(cursor.getColumnIndex(COLUMN.alert_sound_duration));
                        siMcard.alert_reminder_interval = cursor.getString(cursor.getColumnIndex(COLUMN.alert_reminder_interval));
                        siMcard.enable_alert_vibrate = cursor.getString(cursor.getColumnIndex(COLUMN.enable_alert_vibrate));
                        siMcard.enable_alert_speech = cursor.getString(cursor.getColumnIndex(COLUMN.enable_alert_speech));
                        siMcard.enable_etws_test_alerts = cursor.getString(cursor.getColumnIndex(COLUMN.enable_etws_test_alerts));
                        siMcard.enable_channel_50_alerts = cursor.getString(cursor.getColumnIndex(COLUMN.enable_channel_50_alerts));
                        siMcard.enable_cmas_test_alerts = cursor.getString(cursor.getColumnIndex(COLUMN.enable_cmas_test_alerts));
                        siMcard.show_cmas_opt_out_dialog = cursor.getString(cursor.getColumnIndex(COLUMN.show_cmas_opt_out_dialog));
                        siMcard.volte_vt_enabled = cursor.getString(cursor.getColumnIndex(COLUMN.volte_vt_enabled));
                        siMcard.vt_ims_enabled = cursor.getString(cursor.getColumnIndex(COLUMN.vt_ims_enabled));
                        siMcard.wfc_ims_enabled = cursor.getString(cursor.getColumnIndex(COLUMN.wfc_ims_enabled));
                        siMcard.wfc_ims_mode = cursor.getString(cursor.getColumnIndex(COLUMN.wfc_ims_mode));
                        siMcard.wfc_ims_roaming_mode = cursor.getString(cursor.getColumnIndex(COLUMN.wfc_ims_roaming_mode));
                        siMcard.wfc_ims_roaming_enabled = cursor.getString(cursor.getColumnIndex(COLUMN.wfc_ims_roaming_enabled));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //
                    list.add(siMcard);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "\n\nSIMcard{" +
                "\nimsi='" + imsi + '\'' +
                ",\n _id=" + _id +
                ",\n icc_id='" + icc_id + '\'' +
                ",\n sim_id=" + sim_id +
                ",\n display_name='" + display_name + '\'' +
                ",\n carrier_name='" + carrier_name + '\'' +
                ",\n name_source='" + name_source + '\'' +
                ",\n color='" + color + '\'' +
                ",\n number='" + number + '\'' +
                ",\n display_number_format='" + display_number_format + '\'' +
                ",\n data_roaming='" + data_roaming + '\'' +
                ",\n mcc='" + mcc + '\'' +
                ",\n mnc='" + mnc + '\'' +
                ",\n sim_provisioning_status='" + sim_provisioning_status + '\'' +
                ",\n is_embedded='" + is_embedded + '\'' +
                ",\n card_id='" + card_id + '\'' +
                ",\n access_rules='" + access_rules + '\'' +
                ",\n is_removable='" + is_removable + '\'' +
                ",\n enable_cmas_extreme_threat_alerts='" + enable_cmas_extreme_threat_alerts + '\'' +
                ",\n enable_cmas_severe_threat_alerts='" + enable_cmas_severe_threat_alerts + '\'' +
                ",\n enable_cmas_amber_alerts='" + enable_cmas_amber_alerts + '\'' +
                ",\n enable_emergency_alerts='" + enable_emergency_alerts + '\'' +
                ",\n alert_sound_duration='" + alert_sound_duration + '\'' +
                ",\n alert_reminder_interval='" + alert_reminder_interval + '\'' +
                ",\n enable_alert_vibrate='" + enable_alert_vibrate + '\'' +
                ",\n enable_alert_speech='" + enable_alert_speech + '\'' +
                ",\n enable_etws_test_alerts='" + enable_etws_test_alerts + '\'' +
                ",\n enable_channel_50_alerts='" + enable_channel_50_alerts + '\'' +
                ",\n enable_cmas_test_alerts='" + enable_cmas_test_alerts + '\'' +
                ",\n show_cmas_opt_out_dialog='" + show_cmas_opt_out_dialog + '\'' +
                ",\n volte_vt_enabled='" + volte_vt_enabled + '\'' +
                ",\n vt_ims_enabled='" + vt_ims_enabled + '\'' +
                ",\n wfc_ims_enabled='" + wfc_ims_enabled + '\'' +
                ",\n wfc_ims_mode='" + wfc_ims_mode + '\'' +
                ",\n wfc_ims_roaming_mode='" + wfc_ims_roaming_mode + '\'' +
                ",\n wfc_ims_roaming_enabled='" + wfc_ims_roaming_enabled + '\'' +
                '}';
    }
}
