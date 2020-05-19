package com.hzc.last;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    private TelephonyManager telephonyManager;
    private int simSignalStrength;

    @Override
    public void setContentView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main2);
    }

    @Override
    public void initIntent(@NonNull Intent intent) {

    }

    @Override
    public void initView() {
        tvResult = findViewById(R.id.tv_result);
        findViewById(R.id.button).setOnClickListener(this);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

    }

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength != null) {
                String text = signalStrength.toString();
                simSignalStrength = signalStrength.getGsmSignalStrength();
                int level=0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                     level = signalStrength.getLevel();
                } else {
                    Object obj = UtilReflect.call(signalStrength, "getLevel", null, true, null);
                    try {
                        if (obj != null) {
                            level = (int) obj;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                text += "level : " + level;
                text += "\nGsm signal : " + simSignalStrength;

                Integer LteSignalStrength = null;
                Object getLteSignalStrength = UtilReflect.call(signalStrength, "getLteSignalStrength", null, true, null);
                if (getLteSignalStrength != null) {
                    try {
                        LteSignalStrength = (Integer) getLteSignalStrength;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                text += "\nLte signal : " + LteSignalStrength;

                tvResult.setText(text);
            } else {
                tvResult.setText("信号强度：NULL");
            }
        }
    };

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
