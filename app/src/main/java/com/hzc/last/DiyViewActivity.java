package com.hzc.last;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.hzc.last.diyview.DiyView;
import com.zch.last.activity.BaseLastActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiyViewActivity extends BaseLastActivity {
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.dv_view)
    DiyView dvView;

    @Override
    public boolean useButterKnife() {
        return true;
    }

    @Override
    public void onCreated(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_diy_view);
    }

    @Override
    public void initIntent(@Nullable Intent intent) {

    }

    @Override
    public void initView() {
//        DiyView diyView = new DiyView(this);
//        container.addView(diyView);
    }

    @Override
    public void initListener() {
        dvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public void initData() {

    }

}
