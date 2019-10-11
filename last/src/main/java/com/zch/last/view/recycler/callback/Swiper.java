package com.zch.last.view.recycler.callback;

import android.graphics.Canvas;
import android.util.SparseIntArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zch.last.view.recycler.callback.listener.OnSwipeListener;
import com.zch.last.view.recycler.callback.listener.SwipeCallback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;



/**
 * 可以侧滑删除
 * TODO 侧拉出菜单
 *
 * @param <T>
 */
public class Swiper<T> implements SwipeCallback {
    private RecyclerView mRecyclerView;
    private int swipeFlag;
    private List<T> dataList;
    private int thisActionState;//此次手势action状态
    private OnSwipeListener onSwipeListener;
    private SparseIntArray mDirectionViewIds;
    private boolean canSwipe = true;

    public Swiper(int swipeFlag, List<T> dataList) {
        this.swipeFlag = swipeFlag;
        this.dataList = dataList;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (mRecyclerView != recyclerView) {
            mRecyclerView = recyclerView;
        }
        return canSwipe ? this.swipeFlag : 0;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (this.onSwipeListener != null) {
            this.onSwipeListener.onSwiping();
        }
        int adapterPosition = viewHolder.getAdapterPosition();
        this.dataList.remove(adapterPosition);
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        adapter.notifyItemRemoved(adapterPosition);
        if (adapterPosition >= 0 && adapterPosition < this.dataList.size()) {
            adapter.notifyItemRangeChanged(adapterPosition, this.dataList.size() - adapterPosition);
        }
    }


    @Override
    public void onSelectChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (thisActionState != ItemTouchHelper.ACTION_STATE_SWIPE && viewHolder != null &&
                actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //开始
            if (this.onSwipeListener != null) {
                this.onSwipeListener.onStart(viewHolder);
            }
        } else if (thisActionState == ItemTouchHelper.ACTION_STATE_SWIPE && viewHolder == null &&
                actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            //结束
            if (this.onSwipeListener != null) {
                this.onSwipeListener.onEnd();
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
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public View getDirectionView(RecyclerView.ViewHolder holder, @Direction int direction) {
        return this.mDirectionViewIds == null ? null : holder.itemView.findViewById(this.mDirectionViewIds.get(direction));
    }

    @Override
    public boolean clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        return false;
    }

    public Swiper<T> setSwipeFlag(int flag) {
        this.swipeFlag = flag;
        return this;
    }

    public Swiper<T> setDataList(List<T> list) {
        this.dataList = list;
        return this;
    }

    /**
     * 添加上下左右方向上的view的id
     *
     * @param direction 上下左右
     */
    public Swiper<T> addDirectionView(@Direction int direction, @IdRes int viewId) {
        if (this.mDirectionViewIds == null) {
            this.mDirectionViewIds = new SparseIntArray();
        }
        this.mDirectionViewIds.put(direction, viewId);
        return this;
    }

    public Swiper<T> setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
        return this;
    }

    public void removeDirectionView(@Direction int... directions) {
        if (this.mDirectionViewIds != null && directions != null) {
            for (int i = 0; i < directions.length; i++) {
                int direction = directions[i];
                this.mDirectionViewIds.delete(direction);
            }
        }
    }


    public static final int ROOT = 1 << 4;

    @IntDef({ItemTouchHelper.UP, ItemTouchHelper.DOWN, ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT, ROOT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }

}
