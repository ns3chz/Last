package com.zch.last.utils;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UtilSerialize {
    @Nullable
    public static byte[] serialize(Object object) {
        return serialize(object, null);
    }

    @Nullable
    public static byte[] serialize(Object object, OnSerialized onSerialized) {
        if (object == null) return null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            if (onSerialized != null) {
                onSerialized.before(baos, oos);
            }
            oos.writeObject(object);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (Exception ignored) {
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    @Nullable
    public static <T> T deserialize(@Nullable byte[] bytes) {
        return deserialize(bytes, null);
    }

    @Nullable
    public static <T> T deserialize(@Nullable byte[] bytes, OnDeserialized onDeserialized) {
        if (bytes == null) return null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            if (onDeserialized != null) {
                onDeserialized.before(bais, ois);
            }
            return (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (Exception ignored) {
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }


    public interface OnDeserialized {
        void before(ByteArrayInputStream bais, ObjectInputStream ois);
    }

    public interface OnSerialized {
        void before(ByteArrayOutputStream baos, ObjectOutputStream oos);
    }
}
