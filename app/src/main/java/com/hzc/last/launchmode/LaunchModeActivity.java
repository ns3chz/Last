package com.hzc.last.launchmode;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hzc.last.R;
import com.zch.last.activity.BaseLastActivity;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class LaunchModeActivity extends BaseLastActivity {
    private static int activityNum = 0;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_standard)
    Button btnStandard;
    @BindView(R.id.btn_single_top)
    Button btnSingleTop;
    @BindView(R.id.btn_single_task)
    Button btnSingleTask;
    @BindView(R.id.btn_single_instance)
    Button btnSingleInstance;
    private String mode;

    @Override
    protected void createBefore(@Nullable Bundle savedInstanceState) {
        activityNum++;
    }

    @Override
    public void onCreated(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_launch_mode);
    }

    @Override
    public void initIntent(@NonNull Intent intent) {
        mode = intent.getStringExtra("MODE");
        if (mode == null || mode.length() == 0) {
            mode = "NEW";
        } else {
            mode += " " + activityNum;
        }
        tvTitle.setText(mode);
        Log.d("LaunchModeActivity", "onCreate " + mode);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_standard, R.id.btn_single_top, R.id.btn_single_task, R.id.btn_single_instance})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_standard:
                intent.setClass(this, Standard.class);
                intent.putExtra("MODE", "Standard");
                break;
            case R.id.btn_single_top:
                intent.setClass(this, SingleTop.class);
                intent.putExtra("MODE", "SingleTop");
                break;
            case R.id.btn_single_task:
                intent.setClass(this, SingleTask.class);
                intent.putExtra("MODE", "SingleTask");
                break;
            case R.id.btn_single_instance:
                intent.setClass(this, SingleInstance.class);
                intent.putExtra("MODE", "SingleInstance");
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("LaunchModeActivity", "onNewIntent " + mode);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("LaunchModeActivity", "onSaveInstanceState1 " + mode);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d("LaunchModeActivity", "onSaveInstanceState2 " + mode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("LaunchModeActivity", "onRestoreInstanceState1 " + mode);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.d("LaunchModeActivity", "onRestoreInstanceState2 " + mode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LaunchModeActivity", "onStart " + mode);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("LaunchModeActivity", "onRestart " + mode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LaunchModeActivity", "onResume " + mode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LaunchModeActivity", "onPause " + mode);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LaunchModeActivity", "onStop " + mode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LaunchModeActivity", "onDestroy " + mode);
    }
}
