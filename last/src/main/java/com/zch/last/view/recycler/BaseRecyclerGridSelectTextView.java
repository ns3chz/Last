//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zch.last.view.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.R;
import com.zch.last.view.recycler.adapter.RecyclerListAdapter;
import com.zch.last.view.recycler.model.ModelChoose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class BaseRecyclerGridSelectTextView<MODEL> extends RecyclerViewFx {
    private RecyclerListAdapter<MODEL> adapterRecyclerGridSelect;
    @LayoutRes
    private int itemLayoutRes;//item布局

    public BaseRecyclerGridSelectTextView(Context context) {
        this(context, null);
    }

    public BaseRecyclerGridSelectTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerGridSelectTextView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
        this.init();
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = context.getResources().obtainAttributes(attrs, R.styleable.BaseRecyclerGridSelectTextView);
            itemLayoutRes = array.getResourceId(R.styleable.BaseRecyclerGridSelectTextView_item_layout, R.layout.item_recycler_grid_select);
        } catch (Exception e) {
            itemLayoutRes = R.layout.item_recycler_grid_select;
        } finally {
            try {
                if (array != null) {
                    array.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        this.adapterRecyclerGridSelect = new RecyclerListAdapter<>(this.getContext(), itemLayoutRes, null, null);
        this.initLayoutManager();
        this.setAdapter(this.adapterRecyclerGridSelect);
    }

    protected abstract void initLayoutManager();

    public void setDataList(List<MODEL> dataList) {
        if (this.adapterRecyclerGridSelect != null) {
            this.adapterRecyclerGridSelect.setDataList(dataList);
            this.adapterRecyclerGridSelect.notifyOnUiThread();
        }
    }

    @NonNull
    public List<MODEL> getDataList() {
        if (this.adapterRecyclerGridSelect == null) {
            return new ArrayList<>();
        }
        return this.adapterRecyclerGridSelect.getDataList();
    }

    public void setDataList(MODEL[] dataList) {
        if (this.adapterRecyclerGridSelect != null) {
            this.adapterRecyclerGridSelect.setDataList(Arrays.asList(dataList));
            this.adapterRecyclerGridSelect.notifyOnUiThread();
        }
    }

    public boolean isSelected() {
        return this.adapterRecyclerGridSelect != null && this.adapterRecyclerGridSelect.getChooseList().size() != 0;
    }

    public void setSelected(int index) {
        setSelected(index, false);
    }

    public void setSelected(int index, boolean trigger) {
        if (this.adapterRecyclerGridSelect != null) {
            this.adapterRecyclerGridSelect.select(index, trigger);
        }
    }

    public void setItemDecoration(ItemDecoration itemDecoration) {
        this.addItemDecoration(itemDecoration);
    }


    public void notifyDataSetChanged() {
        this.notifyDataSetChanged(2L);
    }

    public void notifyDataSetChanged(long delay) {
        if (this.adapterRecyclerGridSelect != null) {
            this.adapterRecyclerGridSelect.notifyOnUiThread(delay);
        }
    }

    public RecyclerListAdapter<MODEL> getAdapter() {
        return this.adapterRecyclerGridSelect;
    }

    @Nullable
    public List<MODEL> getSelectModelList() {
        if (this.adapterRecyclerGridSelect == null) {
            return null;
        }
        List<ModelChoose<MODEL>> chooseList = this.adapterRecyclerGridSelect.getChooseList();
        if (chooseList.size() == 0) return null;
        List<MODEL> list = new ArrayList<>();
        for (int i = 0; i < chooseList.size(); i++) {
            ModelChoose<MODEL> modelModelChoose = chooseList.get(i);
            if (modelModelChoose == null) continue;
            MODEL data = modelModelChoose.getData();
            if (data == null) continue;
            list.add(data);
        }
        return list;
    }

}
