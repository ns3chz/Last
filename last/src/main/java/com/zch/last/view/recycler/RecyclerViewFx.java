//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zch.last.view.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFx extends RecyclerView {
    private final List<OnScrollToBottomListener> onScrollToBottomListeners;
    private boolean shouldScrollToPositionTop;
    private OnScrollListener onScrollToBottomListener;
    private OnScrollListener onScrollToPositionListener;

    public RecyclerViewFx(Context context) {
        this(context, null);
    }

    public RecyclerViewFx(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewFx(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onScrollToBottomListeners = new ArrayList<>();
        this.shouldScrollToPositionTop = false;
        this.onScrollToBottomListener = new OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0 && onScrollToBottomListeners.size() > 0) {
                    LayoutManager layoutManager = getLayoutManager();
                    if (layoutManager != null) {
                        int visibleCount = layoutManager.getChildCount();
                        int totalCount = layoutManager.getItemCount();
                        View childView = layoutManager.getChildAt(visibleCount - 1);
                        if (childView != null) {
                            int position = layoutManager.getPosition(childView);
                            if (position == totalCount - 1) {
                                for (int i = 0; i < onScrollToBottomListeners.size(); ++i) {
                                    RecyclerViewFx.OnScrollToBottomListener listener = onScrollToBottomListeners.get(i);
                                    if (listener != null) {
                                        listener.arrived(childView, position);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        };
        this.onScrollToPositionListener = new OnScrollListener() {
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (shouldScrollToPositionTop) {
                    LayoutManager layoutManager = getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < getChildCount()) {
                            int top = getChildAt(firstVisibleItemPosition).getTop();
                            scrollTo(0, top);
                        }
                    }

                    shouldScrollToPositionTop = false;
                }

            }
        };
        this.addOnScrollListener(this.onScrollToPositionListener);
        this.addOnScrollListener(this.onScrollToBottomListener);
    }

    @Override
    public synchronized void scrollToPosition(int position) {
        LayoutManager layoutManager = this.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            if (position <= firstVisibleItemPosition) {
                super.scrollToPosition(position);
            } else if (position <= lastVisibleItemPosition) {
                int top = this.getChildAt(position - firstVisibleItemPosition).getTop();
                this.scrollTo(0, top);
            } else {
                this.shouldScrollToPositionTop = true;
                super.scrollToPosition(position);
            }
        }

    }

    public void addOnScrollToBottomListener(RecyclerViewFx.OnScrollToBottomListener listener) {
        if (listener != null) {
            this.onScrollToBottomListeners.add(listener);
        }
    }

    public void removeOnScrollToBottomListener(RecyclerViewFx.OnScrollToBottomListener listener) {
        if (listener != null) {
            this.onScrollToBottomListeners.remove(listener);
        }
    }

    public interface OnScrollToBottomListener {
        void arrived(View var1, int var2);
    }
}
