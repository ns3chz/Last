package com.zch.last.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface BaseActivityImpl {

    boolean useButterKnife();

    void setContentView(@Nullable Bundle savedInstanceState);

    void initIntent(@NonNull Intent intent);

    void initView();

    void initListener();

    void initData();

    void setTag(@NonNull String tag);
}
