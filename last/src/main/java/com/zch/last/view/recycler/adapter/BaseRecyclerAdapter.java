package com.zch.last.view.recycler.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.zch.last.R;
import com.zch.last.view.recycler.adapter.listener.OnBindHolder;
import com.zch.last.view.recycler.adapter.listener.OnItemClickListener;
import com.zch.last.view.recycler.adapter.listener.OnItemLongClickListener;
import com.zch.last.view.recycler.adapter.listener.OnRecyclerItemSelectedListener;
import com.zch.last.view.recycler.adapter.listener.OnViewClickListener;
import com.zch.last.view.recycler.adapter.listener.OnViewLongClickListener;
import com.zch.last.view.recycler.model.ModelChoose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * TODO 选中后，拖动item选中状态有误,只使用data判断选中
 * basic recycler adapter
 * onItemClickListener
 * onViewClickListener
 * onBindHolderListener
 * setDataList
 *
 * @param <VH>
 */
public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder, DATA> extends RecyclerView.Adapter<VH> implements View.OnClickListener, View.OnLongClickListener {
    @NonNull
    protected final List<DATA> dataList;
    private List<ModelChoose<DATA>> chooseList;
    private ChoiceMode choiceMode = ChoiceMode.NONE;//选择模式，多选或单选
    private ChoiceState choiceState = ChoiceState.NONE;//选中方式，select，check
    private RecyclerView.LayoutManager layoutManager;

    public Context mContext;
    protected LayoutInflater mInflater;
    protected OnBindHolder<VH, DATA> onBindHolderListener;
    private OnItemClickListener<VH, DATA> onItemClickListener;
    private OnRecyclerItemSelectedListener<VH, DATA> onRecyclerItemSelectedListener;//选择监听
    private OnItemLongClickListener<VH, DATA> onItemLongClickListener;
    private OnViewClickListener<VH, DATA> onViewClickListener;//点击view，通过id查找
    private OnViewLongClickListener<VH, DATA> onViewLongClickListener;//长按view，通过id查找
    private @IdRes
    int[] onViewClickIds;//view点击
    private @IdRes
    int[] onViewLongClickIds;//view长按

    public BaseRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.dataList = new ArrayList<>();
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        DATA data = dataList.get(position);
        //设置id点击
        if (hasListenViewClick()) {
            for (int i = 0; i < onViewClickIds.length; i++) {
                View idView = holder.itemView.findViewById(onViewClickIds[i]);
                if (idView != null) {
                    idView.setTag(R.id.view_tag_holder, holder);
                    idView.setTag(R.id.view_tag_position, position);
                    idView.setOnClickListener(this);
                }
            }
        }
        //设置item点击
        if (onItemClickListener != null || !ChoiceMode.NONE.equals(choiceMode)) {
            holder.itemView.setTag(R.id.view_tag_holder, holder);
            holder.itemView.setTag(R.id.view_tag_position, position);
            holder.itemView.setOnClickListener(this);
        } else {
            holder.itemView.setOnClickListener(null);
        }
        //设置id长按
        if (hasListenViewLongClick()) {
            for (int i = 0; i < onViewLongClickIds.length; i++) {
                View idView = holder.itemView.findViewById(onViewLongClickIds[i]);
                if (idView != null) {
                    idView.setTag(R.id.view_tag_holder, holder);
                    idView.setTag(R.id.view_tag_position, position);
                    idView.setOnLongClickListener(this);
                }
            }
        }
        //设置长按事件
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(this);
        } else {
            holder.itemView.setOnLongClickListener(null);
        }
        //
        boolean hadChoice = false;
        if (chooseList != null) {//选中item
            for (int i = 0; i < chooseList.size(); i++) {
                ModelChoose<DATA> dataModelChoose = chooseList.get(i);
                if (dataModelChoose.equals(position, data)) {
                    hadChoice = true;
                    break;
                }
            }

        } //
        if (hadChoice) {
            switch (choiceState) {//选中方式
                case CHECKED:
                    setViewChecked(holder.itemView, true);
                    break;
                case SELECT:
                    setViewSelected(holder.itemView, true);
                    break;
                default:
//                    setViewSelected(holder.itemView, true);
                    break;
            }
        } else {
            setViewChecked(holder.itemView, false);
            setViewSelected(holder.itemView, false);
        }
        //

        if (onBindHolderListener != null) {
            onBindHolderListener.onBind(holder, data, position);
        }
    }

    private void setViewChecked(View view, boolean checked) {
        if (view instanceof Checkable) {
            Checkable hiCheck = (Checkable) view;
            if (hiCheck.isChecked() != checked) {
                hiCheck.setChecked(checked);
            }
        }
    }

    private void setViewSelected(View view, boolean selected) {
        if (view.isSelected() != selected) {
            view.setSelected(selected);
        }
    }

    public void bindHolder(OnBindHolder<VH, DATA> listener) {
        this.onBindHolderListener = listener;
    }

    /**
     * item data
     */
    @Override
    public void onClick(View v) {
        Object viewholder = v.getTag(R.id.view_tag_holder);
        Object position = v.getTag(R.id.view_tag_position);
        if (viewholder != null && position != null) {
            VH holder = (VH) viewholder;
            int pos = (int) position;
            DATA data = dataList.get(pos);
            if (v.getId() == holder.itemView.getId()) {
                if (onItemClickListener != null) {
                    if (onItemClickListener.onItemClick(holder, data, pos)) {
                        //true时，拦截事件
                        return;
                    }
                }
                // 选择事件choice
                select(holder, pos, true);
            } else {
                //其他view点击事件
                if (onViewClickListener != null && hasListenViewClick()) {
                    onViewClickListener.click(v, holder, data, pos);
                }
            }
        } else {
            Log.e(getClass().getName(), "onClick(v) " + ((viewholder == null) ? "viewholder == null ! " : "") +
                    ((position == null) ? "position == null !" : ""));
        }
    }

    /**
     * @return 是否有除item外的view点击事件
     */
    public boolean hasListenViewClick() {
        return onViewClickIds != null && onViewClickIds.length > 0;
    }

    @Override
    public boolean onLongClick(View v) {
        Object viewholder = v.getTag(R.id.view_tag_holder);
        Object position = v.getTag(R.id.view_tag_position);
        if (viewholder != null && position != null) {
            VH holder = (VH) viewholder;
            int pos = (int) position;
            if (v.getId() == holder.itemView.getId()) {
                if (onItemLongClickListener != null) {
                    if (onItemLongClickListener.onItemLoneClick(holder, dataList.get(pos), pos)) {
                        return true;
                    }
                }
            } else {
                if (onViewLongClickListener != null && hasListenViewLongClick()) {
                    if (onViewLongClickListener.onViewLoneClick(v, holder, dataList.get(pos), pos)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    /**
     * @return 是否有除item外的view长按事件
     */
    public boolean hasListenViewLongClick() {
        return onViewLongClickIds != null && onViewLongClickIds.length > 0;
    }

    public void select(int position) {
        select(position, false);
    }

    public void select(int position, boolean trigger) {
        select(null, position, trigger);
    }

    /**
     * @param position 选中position
     * @param trigger  触发回调
     */
    public void select(@Nullable VH holder, int position, boolean trigger) {
        if (position < 0 || position >= dataList.size()) return;
        // 选择事件choice
        DATA data = dataList.get(position);
        List<ModelChoose<DATA>> mChooseList = getChooseList();
        List<ModelChoose<DATA>> notifyChooseList = getNotifyChooseList();
        List<ModelChoose<DATA>> notifyCancelList = getNotifyCancelList();
        ModelChoose<DATA> cancelChoose;
        ModelChoose<DATA> dataChoose;
        switch (choiceMode) {
            case MULTI:
                if (mChooseList.size() > 0) {
                    int choosePos = posHasChoose(position, data);
                    if (choosePos >= 0) {
                        cancelChoose = mChooseList.get(choosePos);
                        dataChoose = null;
                    } else {
                        cancelChoose = null;
                        dataChoose = new ModelChoose<>(position, data);
                    }
                } else {
                    cancelChoose = null;
                    dataChoose = new ModelChoose<>(position, data);
                }
                //刷新item
                if (cancelChoose != null) {
                    mChooseList.remove(cancelChoose);
                    notifyCancelList.add(cancelChoose);
                    notifyItemChanged(cancelChoose.getPosition());
                }
                if (dataChoose != null) {
                    mChooseList.add(dataChoose);
                    notifyChooseList.add(dataChoose);
                    notifyItemChanged(dataChoose.getPosition());
                }
                //通知选择回调
                if (trigger) {
                    notifySelected(holder, notifyChooseList, notifyCancelList);
                }
                break;
            case SINGLE:
                if (mChooseList.size() > 0) {
                    cancelChoose = mChooseList.get(0);
                    if (cancelChoose.equals(position, data)) {
                        dataChoose = null;
                    } else {
                        dataChoose = new ModelChoose<>(position, data);
                    }
                } else {
                    cancelChoose = null;
                    dataChoose = new ModelChoose<>(position, data);
                }
                //刷新item
                if (cancelChoose != null) {
                    mChooseList.remove(cancelChoose);
                    notifyCancelList.add(cancelChoose);
                    notifyItemChanged(cancelChoose.getPosition());
                }
                if (dataChoose != null) {
                    mChooseList.add(dataChoose);
                    notifyChooseList.add(dataChoose);
                    notifyItemChanged(dataChoose.getPosition());
                }
                //通知选择回调
                if (trigger) {
                    notifySelected(holder, notifyChooseList, notifyCancelList);
                }
                break;
            default:
                if (mChooseList.size() != 0) {
                    mChooseList.clear();
                    notifyDataSetChanged();
                }
                break;
        }
    }


    private List<ModelChoose<DATA>> notifyChooseList = null;//用于传递选择事件的数据
    private List<ModelChoose<DATA>> notifyCancelList = null;//用于传递取消选择事件的数据

    private List<ModelChoose<DATA>> getNotifyChooseList() {
        if (notifyChooseList == null) {
            notifyChooseList = new ArrayList<>();
        } else {
            notifyChooseList.clear();
        }
        return notifyChooseList;
    }

    private List<ModelChoose<DATA>> getNotifyCancelList() {
        if (notifyCancelList == null) {
            notifyCancelList = new ArrayList<>();
        } else {
            notifyCancelList.clear();
        }
        return notifyCancelList;
    }

    /**
     * @return position位置的数据在选择列表中的位置，小于0是未选择,
     */
    public int posHasChoose(int position) {
        if (position < 0 || position >= dataList.size()) return -1;
        return posHasChoose(position, dataList.get(position));
    }

    /**
     * @return position位置的数据在选择列表中的位置，小于0是未选择,
     */
    private int posHasChoose(int position, DATA data) {
        if (chooseList == null) return -1;
        int choosePos = -1;
        for (int i = 0; i < chooseList.size(); i++) {
            ModelChoose<DATA> dataModelChoose = chooseList.get(i);
            if (dataModelChoose.equals(position, data)) {
                choosePos = i;
                break;
            }
        }
        return choosePos;
    }

    /**
     * 通知选择/未选择
     */
    private void notifySelected(@Nullable VH holder, @NonNull List<ModelChoose<DATA>> chooseList, @NonNull List<ModelChoose<DATA>> cancelList) {
        if (this.onRecyclerItemSelectedListener != null) {
            this.onRecyclerItemSelectedListener.selected(holder, chooseList, cancelList);
        }
    }

    /**
     * @param listener 听各个viewId的click事件 （需要刷新adapter）
     */
    public void setOnViewClickListener(OnViewClickListener<VH, DATA> listener, @IdRes int... viewIds) {
        this.onViewClickIds = viewIds;
        this.onViewClickListener = listener;
    }

    public void setOnViewLongClickListener(OnViewLongClickListener<VH, DATA> listener, @IdRes int... viewIds) {
        this.onViewLongClickIds = viewIds;
        this.onViewLongClickListener = listener;
    }

    /**
     * @param listener 听每个item的click事件（需要刷新adapter）
     */
    public void setOnItemClickListener(OnItemClickListener<VH, DATA> listener) {
        this.onItemClickListener = listener;
    }

    /**
     * @param listener 听每个item的长按事件（需要刷新adapter）
     */
    public void setOnItemLongClickListener(OnItemLongClickListener<VH, DATA> listener) {
        this.onItemLongClickListener = listener;
    }

    /**
     * @param listener 听选择事件
     */
    public void setOnRecyclerItemSelectedListener(OnRecyclerItemSelectedListener<VH, DATA> listener) {
        this.onRecyclerItemSelectedListener = listener;
    }

    @NonNull
    public List<DATA> getDataList() {
        return dataList;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public void setDataList(DATA[] list) {
        setDataList(Arrays.asList(list));
    }

    public void setDataList(List<DATA> list) {
        this.dataList.clear();
        if (list != null) {
            this.dataList.addAll(list);
        }
    }

    public void addData(DATA data) {
        addData(-1, data);

    }

    public void addData(int pos, DATA data) {
        if (pos < 0) {
            pos = 0;
        }
        if (pos > this.dataList.size()) {
            pos = this.dataList.size();
        }
        this.dataList.add(pos, data);
    }

    @NonNull
    public List<ModelChoose<DATA>> getChooseList() {
        if (chooseList == null) {
            synchronized (this) {
                if (chooseList == null) {
                    chooseList = new ArrayList<>();
                }
            }
        }
        return chooseList;
    }

    public void setChoiceMode(@NonNull ChoiceMode mode) {
        this.choiceMode = mode;
        resetSelected();
    }

    public void setChoiceState(ChoiceState choiceState) {
        this.choiceState = choiceState;
        resetSelected();
    }

    public void resetSelected() {
        if (this.chooseList != null) {
            this.chooseList.clear();
        }
    }

    //-----------------------------------------------------------------------------

    /**
     * TODO 刷新当前已经显示的item
     */
    private Disposable notifyCurrentDataChanged() {
        return notifyOnUiThread(100, new Runnable() {
            @Override
            public void run() {
                if (layoutManager == null) {
                    notifyDataSetChanged();
                    return;
                }
                int firstVisibleItemPosition = -1;
                int lastVisibleItemPosition = -1;
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int[] first = staggeredGridLayoutManager.findFirstVisibleItemPositions(null);
                    int[] last = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
                    if (first != null && first.length > 0) {
                        firstVisibleItemPosition = first[0];
                    }
                    if (last != null && last.length > 0) {
                        lastVisibleItemPosition = last[0];
                    }
                }
                if (firstVisibleItemPosition != -1 && lastVisibleItemPosition != -1) {
                    notifyItemRangeChanged(firstVisibleItemPosition, lastVisibleItemPosition - firstVisibleItemPosition + 1);
                } else {
                    notifyDataSetChanged();
                }
            }
        });
    }

    public Disposable notifyOnUiThread() {
        return notifyOnUiThread(2);
    }

    public Disposable notifyOnUiThread(long delay) {
        return notifyOnUiThread(delay, new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private Disposable notifyOnUiThread(long delay, Runnable runnable) {
        return Observable.just(runnable)
                .subscribeOn(Schedulers.computation())
                .delay(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Runnable>() {
                    @Override
                    public void accept(Runnable r) throws Exception {
                        if (r != null) {
                            r.run();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }
    //-----------------------------------------------------------------------------


    public enum ChoiceMode {
        NONE, SINGLE, MULTI
    }

    public enum ChoiceState {
        SELECT, CHECKED, NONE
    }


}
