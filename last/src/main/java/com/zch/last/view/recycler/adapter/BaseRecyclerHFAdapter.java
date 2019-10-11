package com.zch.last.view.recycler.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.zch.last.R;
import com.zch.last.view.recycler.adapter.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p></p>
 * <li>
 * 以 {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} 的形式添加header，footer，最多可以各添加1000个，
 * </li>
 * <li>
 * 适配了 {@link GridLayoutManager} , {@link StaggeredGridLayoutManager} 两种类型，令头尾能占据一整行
 * </li>
 * </p>
 *
 * @param <VH>
 */
public abstract class BaseRecyclerHFAdapter<VH extends RecyclerView.ViewHolder, DATA> extends BaseRecyclerAdapter<RecyclerView.ViewHolder, DATA> {


    private List<RecyclerView.ViewHolder> headViewHolders;
    private List<RecyclerView.ViewHolder> footViewHolders;
    private OnItemClickListener<RecyclerView.ViewHolder, Object> onItemHeaderClickListener;
    private OnItemClickListener<RecyclerView.ViewHolder, Object> onItemFooterClickListener;

    public BaseRecyclerHFAdapter(Context mContext) {
        super(mContext);
    }

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType < 0) {
            if (viewType % 2 == -1) {
                int headerIndex = headerItemType2Index(viewType);
                return headViewHolders.get(headerIndex);
//            } else if ( viewType % 2 == 0) {
            } else {
                int footerIndex = footerItemType2Index(viewType);
                return footViewHolders.get(footerIndex);
            }
        }
        int res = onCreateViewRes(parent, viewType);
        View view = null;
        if (res != 0) {
            view = mInflater.inflate(res, parent, false);
        }
        return onCreateViewHolders(parent, view, viewType);
    }

    /**
     * @return header index
     */
    private int headerItemType2Index(@IntRange(from = Integer.MIN_VALUE, to = -1) int viewType) {
        return -(viewType + 1) / 2;
    }

    /**
     * @return footer index
     */
    private int footerItemType2Index(@IntRange(from = Integer.MIN_VALUE, to = -1) int viewType) {
        return -viewType / 2 - 1;
    }

    @Override
    public final int getItemCount() {
        return getItemCounts() +
                (hasHeader() ? headViewHolders.size() : 0) +
                (hasFooter() ? footViewHolders.size() : 0);
    }

    @Override
    public final void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemPosition = position - getHeadersCount();
        //header，footer设置onclick事件
        if (itemPosition < 0) {
            if (this.onItemHeaderClickListener != null) {
                holder.itemView.setTag(R.id.view_tag_holder, holder);
                holder.itemView.setTag(R.id.view_tag_position, position);
                holder.itemView.setTag(R.id.view_tag_holder_hf, true);//holder是否是header
                holder.itemView.setOnClickListener(onHFClickListener);
            } else {
                holder.itemView.setTag(R.id.view_tag_holder, null);
                holder.itemView.setTag(R.id.view_tag_position, null);
                holder.itemView.setTag(R.id.view_tag_holder_hf, null);
                holder.itemView.setOnClickListener(null);
            }
            return;
        } else if (itemPosition >= getItemCounts()) {
            if (this.onItemFooterClickListener != null) {
                holder.itemView.setTag(R.id.view_tag_holder, holder);
                holder.itemView.setTag(R.id.view_tag_position, itemPosition - getItemCounts());
                holder.itemView.setTag(R.id.view_tag_holder_hf, false);//holder是否是header
                holder.itemView.setOnClickListener(onHFClickListener);
            } else {
                holder.itemView.setTag(R.id.view_tag_holder, null);
                holder.itemView.setTag(R.id.view_tag_position, null);
                holder.itemView.setTag(R.id.view_tag_holder_hf, null);
                holder.itemView.setOnClickListener(null);
            }
            return;
        }

        onBindsViewHolder((VH) holder, itemPosition);
        super.onBindViewHolder(holder, itemPosition);
    }

    /**
     * header，footer点击事件
     */
    private View.OnClickListener onHFClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean isHeader = (Boolean) v.getTag(R.id.view_tag_holder_hf);
            RecyclerView.ViewHolder viewholder = (RecyclerView.ViewHolder) v.getTag(R.id.view_tag_holder);
            if (isHeader == null || viewholder == null) {
                return;
            }
            Object position = v.getTag(R.id.view_tag_position);
            if (isHeader && onItemHeaderClickListener != null) {
                onItemHeaderClickListener.onItemClick(viewholder, null, (int) position);
            } else if (!isHeader && onItemFooterClickListener != null) {
                onItemFooterClickListener.onItemClick(viewholder, null, (int) position);
            }
        }
    };

    @Override
    public final int getItemViewType(int position) {
        if (isHeader(position)) {
            //header类型
            return getHeaderItemViewType(position);
        } else if (isFooter(position)) {
            //footer类型
            return getFooterItemViewType(position);
        } else {
            //
            if (hasHeader()) {
                return this.getItemViewTypes(position - getHeadersCount());
            } else {
                return this.getItemViewTypes(position);
            }
        }
    }

    /**
     * 前提有header的情况
     *
     * @return position -> itemViewType
     */
    private int getHeaderItemViewType(int position) {
        return -2 * getHeaderIndex(position) - 1;
    }

    /**
     * 前提有footer的情况
     *
     * @return position -> itemViewType
     */
    private int getFooterItemViewType(int position) {
        return -2 * (getFooterIndex(position) + 1);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int adapterPosition = holder.getAdapterPosition();
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            //让header、footer占据整行/列,适配StaggeredGridLayoutManager
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            params.setFullSpan(isHeader(adapterPosition) || isFooter(adapterPosition));
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            //让header、footer占据整行/列，适配GridLayoutManager
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position) || isFooter(position)) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }
    //------------------------------------------methods---------------------------------------------

    /**
     * @return 该position是否是header
     */
    private boolean isHeader(int position) {
        return hasHeader() && position >= 0 && position < headViewHolders.size();
    }

    private int getHeaderIndex(int position) {
        return position;
    }

    /**
     * @return 该position是否是footer
     */
    private boolean isFooter(int position) {
        if (!hasFooter()) return false;
        int index = getFooterIndex(position);
        return index >= 0 && index < footViewHolders.size();
    }

    /**
     * @return 根据position得到foot holder 在集合中的index
     */
    private int getFooterIndex(int position) {
        return position - getItemCounts() - (hasHeader() ? headViewHolders.size() : 0);
    }

    public int getItemCounts() {
        return dataList.size();
    }


    //------------------ header -------------------------

    /**
     * @return 是否有header
     */
    public boolean hasHeader() {
        return headViewHolders != null && headViewHolders.size() > 0;
    }

    /**
     * @return header个数
     */
    public int getHeadersCount() {
        return headViewHolders == null ? 0 : headViewHolders.size();
    }

    public @Nullable
    List<RecyclerView.ViewHolder> getHeaders() {
        return headViewHolders;
    }

    public void addHeader(RecyclerView.ViewHolder holder) {
        addHeader(-1, holder);
    }

    public void addHeader(int index, RecyclerView.ViewHolder holder) {
        if (holder == null) return;
        if (headViewHolders == null) {
            headViewHolders = new ArrayList<>();
        }

        if (index < 0 || index > headViewHolders.size()) {
            headViewHolders.add(holder);
        } else {
            headViewHolders.add(index, holder);
        }
    }

    public void setHeader(List<RecyclerView.ViewHolder> holders) {
        headViewHolders = holders;
    }

    public void setHeader(RecyclerView.ViewHolder... holders) {
        if (holders == null || holders.length == 0) {
            headViewHolders = null;
            return;
        }
        headViewHolders = Arrays.asList(holders);
    }

    //------------------ footer -------------------------

    /**
     * @return 是否有footer
     */
    public boolean hasFooter() {
        return footViewHolders != null && footViewHolders.size() > 0;
    }

    public int getFootersCount() {
        return footViewHolders == null ? 0 : footViewHolders.size();
    }

    public @Nullable
    List<RecyclerView.ViewHolder> getFooters() {
        return footViewHolders;
    }

    public void addFooter(RecyclerView.ViewHolder holder) {
        addFooter(-1, holder);
    }

    public void addFooter(int index, RecyclerView.ViewHolder holder) {
        if (holder == null) return;
        if (footViewHolders == null) {
            footViewHolders = new ArrayList<>();
        }
        if (index < 0 || index > footViewHolders.size()) {
            footViewHolders.add(holder);
        } else {
            footViewHolders.add(index, holder);
        }
    }

    public void setFooter(List<RecyclerView.ViewHolder> holders) {
        footViewHolders = holders;
    }

    public void setFooter(RecyclerView.ViewHolder... holders) {
        if (holders == null || holders.length == 0) {
            footViewHolders = null;
            return;
        }
        footViewHolders = Arrays.asList(holders);
    }

    public void setOnItemHeaderClickListener(OnItemClickListener<RecyclerView.ViewHolder, Object> onItemHeaderClickListener) {
        this.onItemHeaderClickListener = onItemHeaderClickListener;
    }

    public void setOnItemFooterClickListener(OnItemClickListener<RecyclerView.ViewHolder, Object> onItemFooterClickListener) {
        this.onItemFooterClickListener = onItemFooterClickListener;
    }

    //--------------------------------------------abstract------------------------------------------

    public abstract void onBindsViewHolder(VH holder, int position);

    /**
     * @param view     item view
     * @param viewType view type
     */
    public abstract VH onCreateViewHolders(ViewGroup parent, View view, @IntRange(from = 0, to = Integer.MAX_VALUE) int viewType);

    /**
     * @return item layout res
     */
    @LayoutRes
    public abstract int onCreateViewRes(@NonNull ViewGroup parent, int viewType);


    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getItemViewTypes(int position) {
        return 0;
    }


}
