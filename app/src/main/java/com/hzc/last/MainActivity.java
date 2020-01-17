package com.hzc.last;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.zch.last.widget.dialog.DialogAsk;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LifecycleObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.d("OnLifecycleEvent", "ON_CREATE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start() {
        Log.d("OnLifecycleEvent", "ON_START");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        Log.d("OnLifecycleEvent", "ON_RESUME");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {
        Log.d("OnLifecycleEvent", "ON_PAUSE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {
        Log.d("OnLifecycleEvent", "ON_STOP");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        Log.d("OnLifecycleEvent", "ON_DESTROY");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void any() {
        Log.d("OnLifecycleEvent", "ON_ANY");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                DialogAsk dialogAsk = new DialogAsk(this)
                        .setButtonsText("NO", "YES")
                        .setTitleText("弹出了框框");
                dialogAsk.show();
                break;
        }
    }
}
