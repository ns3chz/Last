package com.zch.last.view.recycler.model;

import java.util.Arrays;

public class ModelChoose<D> {
    private int position;
    private D data;

    public ModelChoose(int position, D data) {
        this.position = position;
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (obj instanceof ModelChoose) {
                ModelChoose model = (ModelChoose) obj;
                Object data = model.getData();

                return equals(model.position, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.equals(obj);
    }

    public boolean equals(int position, Object data) {
        if (this.getPosition() != position) return false;
        D thisData = this.getData();
        if (data == null) {
            return thisData == null;
        } else {
            return data.equals(thisData);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{position, data});
    }
}
