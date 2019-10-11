package com.zch.last.view.recycler.callback;

import android.graphics.Canvas;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zch.last.view.recycler.callback.listener.DragCallback;
import com.zch.last.view.recycler.callback.listener.OnDragListener;

import java.util.Collections;
import java.util.List;


/**
 * can long press drag, touch drag use{ ItemTouchHelper.startDrag(android.support.v7.widget.RecyclerView.ViewHolder)}
 *
 * @param <T>
 */
public class Dragger<T> implements DragCallback {
    private RecyclerView mRecyclerView;
    public int dragFlag;
    private List<T> dataList;
    private boolean canDrag = true;
    private int startPosition;
    private int endPosition;
    private int thisActionState;//此次手势action状态
    private OnDragListener onDragListener;

    public Dragger(int dragFlag, List<T> list) {
        this.dragFlag = dragFlag;
        this.dataList = list;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (mRecyclerView != recyclerView) {
            mRecyclerView = recyclerView;
        }
        return canDrag ? this.dragFlag : 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (mRecyclerView != recyclerView) {
            mRecyclerView = recyclerView;
        }
        if (mRecyclerView == null) return false;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) return false;
        //滑动事件
        endPosition = target.getAdapterPosition();
        int from = viewHolder.getAdapterPosition();
        Collections.swap(dataList, from, endPosition);
        adapter.notifyItemMoved(from, endPosition);
        //正在拖拽
        if (this.onDragListener != null) {
            this.onDragListener.onDragging(startPosition, viewHolder.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSelectChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (thisActionState != ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null &&
                actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            //手势开始
            startPosition = viewHolder.getAdapterPosition();
            endPosition = startPosition;
            if (this.onDragListener != null) {
                this.onDragListener.onStart(viewHolder, startPosition);
            }
        } else if (thisActionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder == null &&
                actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            //手势结束,停止拖拽
            //刷新position
            if (startPosition != endPosition) {
                RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
                adapter.notifyItemRangeChanged(Math.min(startPosition, endPosition), Math.abs(startPosition - endPosition) + 1);
            }
            if (this.onDragListener != null) {
                this.onDragListener.onEnd(startPosition, endPosition);
            }
        }
        thisActionState = actionState;
    }

    @Override
    public boolean onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        return false;
    }

    @Override
    public boolean onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        return false;
    }

    @Override
    public boolean clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    public Dragger<T> setOnDragListener(OnDragListener listener) {
        this.onDragListener = listener;
        return this;
    }

    public Dragger<T> setDragFlag(int dragFlag) {
        this.dragFlag = dragFlag;
        return this;
    }

    public Dragger<T> setDataList(List<T> dataList) {
        this.dataList = dataList;
        return this;
    }

    public boolean canDrag() {
        return canDrag;
    }

    public Dragger<T> canDrag(boolean can) {
        this.canDrag = can;
        return this;
    }
}
