package com.zch.last.model;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 支持对象的浅克隆，深克隆
 */
public class BaseCloneableModel extends BaseModel implements Cloneable {

    @Nullable
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 浅克隆, 只克隆值类型，引用类型引用地址不变
     */
    @Nullable
    public final <T extends BaseCloneableModel> T lightClone() {
        return (T) this.clone();
    }

    /**
     * 继承于该类型的所有子类型中的所有引用类型必须实现 Serializable 接口
     *
     * @return 深克隆，引用类型对象生成新地址
     */
    @Nullable
    public  <T extends BaseCloneableModel> T deepClone() {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            //将对象写入流中
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            //将对象从流中取出
            bais = new ByteArrayInputStream(baos.toByteArray());
            ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //释放
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
