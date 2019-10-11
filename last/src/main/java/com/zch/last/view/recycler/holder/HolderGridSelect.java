package com.zch.last.view.recycler.holder;

import android.view.View;
import android.widget.TextView;

import com.zch.last.R;


/**
 * Created by Administrator on 2017/6/26.
 */

public class HolderGridSelect extends BaseRecyclerViewHolder {

    public final TextView selectTv;

    public HolderGridSelect(View itemView) {
        super(itemView);
        selectTv =  itemView.findViewById(R.id.selectTv);
    }
}
