package com.zch.last.stream;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import com.zch.last.BuildConfig;
import com.zch.last.stream.adapter.ReadAdapterAbs;
import com.zch.last.stream.listener.OnInputStreamTaskListener;

import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InputStreamTask {
    @Nullable
    private InputStream inputStream;
    @IntRange(from = 1, to = Integer.MAX_VALUE)
    public int readLen = 1024;//一次最多读取长度
    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int readInterval = 0;//读取间隔，毫秒
    private Disposable readDisposable;
    @Nullable
    private ReadAdapterAbs readAdapterSet;
    private boolean stopRead = true;
    @Nullable
    public OnInputStreamTaskListener onTaskListener;

    public InputStreamTask() {
        this(null);
    }

    public InputStreamTask(@Nullable InputStream stream) {
        this.inputStream = stream;
    }

    public void startRead() {
        stopRead = false;
        if (readDisposable != null && !readDisposable.isDisposed()) {
            return;
        }
        if (onTaskListener != null) {
            onTaskListener.onStartRead();
        }
        readDisposable = Observable.just("")
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        final byte[] BUFFER = new byte[readLen];
                        int len;
                        int extraWait;
                        while (true) {
                            extraWait = 0;
                            try {
                                while (inputStream != null && (len = inputStream.read(BUFFER)) != -1) {
                                    try {
                                        if (readAdapterSet != null) {
                                            readAdapterSet.read(BUFFER, len);
                                        }
                                    } catch (Exception e) {
                                        if (BuildConfig.DEBUG) {
                                            if (onTaskListener != null) {
                                                onTaskListener.onException(e);
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                if (BuildConfig.DEBUG) {
                                    if (onTaskListener != null) {
                                        onTaskListener.onException(e);
                                    }
                                }
                                if (inputStream == null || inputStream.available() == 0) {
                                    if (onTaskListener != null) {
                                        onTaskListener.onStreamClosed();
                                    }
                                    stopRead();
                                }
                                extraWait = 500;
                            }

                            if (stopRead) {
                                break;
                            }
                            try {
                                if (readInterval > 0) {
                                    Thread.sleep(readInterval + extraWait);
                                }
                            } catch (InterruptedException e) {
                                if (BuildConfig.DEBUG) {
                                    if (onTaskListener != null) {
                                        onTaskListener.onException(e);
                                    }
                                }
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (BuildConfig.DEBUG) {
                            if (onTaskListener != null) {
                                onTaskListener.onException(new Exception(throwable));
                            }
                        }
                    }
                });
    }

    public void stopRead() {
        stopRead = true;
        if (readDisposable != null) {
            readDisposable.dispose();
            readDisposable = null;
        }
        if (onTaskListener != null) {
            onTaskListener.onStopRead();
        }
    }

    public void setReadAdapter(@Nullable ReadAdapterAbs readParam) {
        readAdapterSet = readParam;
    }

    public void setInputStream(@Nullable InputStream inputStream) {
        this.inputStream = inputStream;
    }

}
