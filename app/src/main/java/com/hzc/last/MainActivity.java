package com.hzc.last;

import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.activity.BaseLastActivity;
import com.zch.last.model.clickview.TextViewClick;
import com.zch.last.utils.UtilAlert;
import com.zch.last.utils.UtilToast;
import com.zch.last.widget.alert.GlobalAlertAsk;
import com.zch.last.widget.dialog.DialogAsk;

public class MainActivity extends BaseLastActivity implements View.OnClickListener {

    private final String wakeTag = "com.hzc.last:myWakeLock";
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                DialogAsk dialogAsk = new DialogAsk(this)
                        .setButtonsText("NO", "YES")
                        .setTitleText("弹出了框框");
                dialogAsk.show();
                break;
            case R.id.button1:
                break;
            case R.id.button2:
                showAlert();
                break;
        }
    }

    private GlobalAlertAsk globalAlertAsk;

    private void showAlert() {
        globalAlertAsk = UtilAlert.showGlobalAlertDefaultAsk(this, "警告", "程序崩溃,即将推出", true
                , new TextViewClick("退出") {
                    @Override
                    public void onClick(View v) {
                        System.exit(0);

                    }
                }, new TextViewClick("弹出新对话框") {
                    @Override
                    public void onClick(View v) {
                        globalAlertAsk.dissmiss();
                        globalAlertAsk = UtilAlert.showGlobalAlertDefaultAsk(getApplicationContext(), "新标题", "新内容", false,
                                new TextViewClick("好的") {
                                    @Override
                                    public void onClick(View v) {
                                        globalAlertAsk.dissmiss();
                                        UtilToast.toast("关闭");
                                    }

                                    @Override
                                    public void prepare(TextView view) {
                                        view.setTextColor(Color.RED);
                                        view.setBackgroundColor(Color.BLACK);
                                    }
                                });

                    }
                }, new TextViewClick("否") {
                    @Override
                    public void onClick(View v) {
                        globalAlertAsk.viewModel.refreshButtonLines();
                        UtilToast.toast("否");

                    }
                });
    }

    @Override
    public void setContentView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initIntent(@NonNull Intent intent) {
    }

    @Override
    public void initView() {
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

}
