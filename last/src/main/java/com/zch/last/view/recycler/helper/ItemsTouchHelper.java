package com.zch.last.view.recycler.helper;


import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zch.last.view.recycler.callback.ItemHelperCallback;

import java.util.List;


public class ItemsTouchHelper<T> extends ItemTouchHelper {

    public ItemHelperCallback<T> callback;


    public ItemsTouchHelper() {
        this(new ItemHelperCallback<T>());
    }

    public ItemsTouchHelper(List<T> list) {
        this(new ItemHelperCallback<>(list));
    }

    public ItemsTouchHelper(int dragFlag, int swipeFlag, List<T> list) {
        this(new ItemHelperCallback<>(dragFlag, swipeFlag, list));
    }

    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #attachToRecyclerView(RecyclerView)} . Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    public ItemsTouchHelper(ItemHelperCallback<T> callback) {
        super(callback);
        this.callback = callback;
    }


}
