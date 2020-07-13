package com.hzc.last.thread;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.hzc.last.R;
import com.hzc.last.databinding.ActivityThreadPoolBinding;
import com.zch.last.activity.BaseMVVMActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolActivity extends BaseMVVMActivity<ActivityThreadPoolBinding> {

    private ExecutorService executorService;
    private Thread thread;

    @Override
    protected int setContentRes() {
        return R.layout.activity_thread_pool;
    }

    @Override
    public void initIntent(@Nullable Intent intent) {

    }

    @Override
    public void initView() {
        executorService = Executors.newCachedThreadPool();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("ThreadPoolActivity", "thread fun");
            }
        });
    }

    @Override
    public void initListener() {
        viewDataBinding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread.State state = thread.getState();
                Log.d("ThreadPoolActivity", state.toString());

                try {
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initData() {

    }
}
