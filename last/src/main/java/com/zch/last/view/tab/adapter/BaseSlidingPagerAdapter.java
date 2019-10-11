package com.zch.last.view.tab.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSlidingPagerAdapter<DATA, PAGE> extends PagerAdapter {
    protected Context mContext;
    @NonNull
    private final List<DATA> DATA;
    @Nullable
     PAGE currentPage;

    public BaseSlidingPagerAdapter(Context mContext) {
        this.mContext = mContext;
        this.DATA = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return DATA.size();
    }

    @Nullable
    public PAGE getCurrentPage() {
        return currentPage;
    }

    public DATA getData(int position) {
        return DATA.get(position);
    }

    public void addData(DATA data) {
        DATA.add(data);
    }

    public void addData(int index, DATA data) {
        DATA.add(index, data);
    }

    public void removeData(DATA data) {
        DATA.remove(data);
    }

    public void removeData(int index) {
        DATA.remove(index);
    }

}
