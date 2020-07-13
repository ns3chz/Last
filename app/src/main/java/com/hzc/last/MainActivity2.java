package com.hzc.last;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.activity.BaseLastActivity;
import com.zch.last.utils.UtilReflect;

public class MainActivity2 extends BaseLastActivity implements View.OnClickListener {
    private String TAG = "MainActivity2";
    private TextView tvResult;

    @Override
    public void onCreated(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main2);
    }

    @Override
    public void initIntent(@NonNull Intent intent) {

    }

    @Override
    public void initView() {
        tvResult = findViewById(R.id.tv_result);
        findViewById(R.id.button).setOnClickListener(this);


    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                break;
        }
    }
}
