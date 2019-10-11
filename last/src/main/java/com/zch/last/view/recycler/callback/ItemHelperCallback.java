package com.zch.last.view.recycler.callback;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ItemHelperCallback<T> extends ItemTouchHelper.Callback {
    private RecyclerView mRecyclerView;
    private List<T> dataList;
    @Nullable
    public Dragger<T> mDragger;
    @Nullable
    public Swiper<T> mSwiper;
    private int currentActionState;

    public ItemHelperCallback() {
        this(null);
    }

    public ItemHelperCallback(List<T> list) {
        this(0, 0, list);
    }

    public ItemHelperCallback(int dragFlag, int swipeFlag, List<T> list) {
        this.dataList = list;
        if (dragFlag != 0) {
            setDragger(new Dragger<>(dragFlag, list));
        }
        if (swipeFlag != 0) {
            setSwiper(new Swiper<>(swipeFlag, list));
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (mRecyclerView != recyclerView) {
            mRecyclerView = recyclerView;
        }
        return makeMovementFlags(this.mDragger == null ? 0 : this.mDragger.getMovementFlags(recyclerView, viewHolder),
                this.mSwiper == null ? 0 : this.mSwiper.getMovementFlags(recyclerView, viewHolder));
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        Log.e(getClass().getName(), "onMove : viewholder = " + viewHolder.getAdapterPosition() + " ,target = " + target.getAdapterPosition());
        return this.mDragger != null && this.mDragger.onMove(recyclerView, viewHolder, target);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        Log.e(getClass().getName(), "onSwiped : direction = " + direction);
        if (this.mSwiper != null) {
            this.mSwiper.onSwiped(viewHolder, direction);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//        Log.e(getClass().getName(), "onSelectedChanged : actionState = " + actionState);
        super.onSelectedChanged(viewHolder, actionState);
        this.currentActionState = actionState;
        if (this.mDragger != null) {
            this.mDragger.onSelectChanged(viewHolder, actionState);
        }
        if (this.mSwiper != null) {
            this.mSwiper.onSelectChanged(viewHolder, actionState);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        boolean onSuper = false;
        if (this.currentActionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (this.mSwiper != null) {
                onSuper = this.mSwiper.clearView(recyclerView, viewHolder);
            }
        } else if (this.currentActionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (this.mDragger != null) {
                onSuper = this.mDragger.clearView(recyclerView, viewHolder);
            }
        }
        if (!onSuper) {
            super.clearView(recyclerView, viewHolder);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        Log.e(getClass().getName(), "onChildDraw : dx = " + dX + " ,dy = " + dY);
        boolean onSuper = false;
        if (this.currentActionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (this.mSwiper != null) {
                onSuper = this.mSwiper.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        } else if (this.currentActionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (this.mDragger != null) {
                onSuper = this.mDragger.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        if (!onSuper) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        Log.e(getClass().getName(), "onChildDrawOver : dx = " + dX + " ,dy = " + dY);
        boolean onSuper = false;
        if (this.currentActionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (this.mSwiper != null) {
                onSuper = this.mSwiper.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        } else if (this.currentActionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (this.mDragger != null) {
                onSuper = this.mDragger.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        if (!onSuper) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return this.mDragger == null || this.mDragger.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return this.mSwiper == null || this.mSwiper.isItemViewSwipeEnabled();
    }


    //----------------------------------------------------------------------------------------------


    public List<T> getDataList() {
        return dataList;
    }

    public ItemHelperCallback<T> setDataList(List<T> dataList) {
        this.dataList = dataList;
        return this;
    }

    public ItemHelperCallback<T> setDragger(Dragger<T> dragger) {
        this.mDragger = dragger;
        return this;
    }

    public ItemHelperCallback<T> setSwiper(Swiper<T> swiper) {
        this.mSwiper = swiper;
        return this;
    }

}
