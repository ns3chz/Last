package com.zch.last.stream;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.BuildConfig;
import com.zch.last.stream.listener.OnOutputStreamTaskListener;

import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */
public class OutputStreamTask {
    @Nullable
    private OnOutputStreamTaskListener onCallBack;
    @Nullable
    private OutputStream outputStream;
    @Nullable
    private Disposable writeDisposable;
    @NonNull
    private final LinkedBlockingQueue<byte[]> writeQueue;
    /**
     * 释放资源超时时间
     */
    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int releaseTimeout = 10000;//毫秒

    public OutputStreamTask() {
        this(100);
    }

    public OutputStreamTask(@IntRange(from = 1, to = Integer.MAX_VALUE) int capacity) {
        writeQueue = new LinkedBlockingQueue<>(capacity);
    }

    public void write(byte[] data) {
        if (data == null) {
            data = new byte[0];
        }
        try {
            if (onCallBack != null) {
                onCallBack.onWriteBefore(data);
            }
            writeQueue.put(data);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            if (onCallBack != null) {
                onCallBack.onError(data, e);
            }
        }
        if (outputStream == null) {
            if (writeDisposable != null && !writeDisposable.isDisposed()) {
                writeDisposable.dispose();
            }
            if (onCallBack != null) {
                onCallBack.onError(data, new Exception("outputStream is on null point !!!"));
            }
            return;
        }
        if (writeDisposable != null && !writeDisposable.isDisposed()) {
            return;
        }
        writeDisposable = Observable.just("")
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        long startTime = System.currentTimeMillis();
                        long currentTime;
                        byte[] bytes;
                        long pollTimeout = getPollTimeout();
                        while (outputStream != null) {
                            try {
                                bytes = writeQueue.poll(pollTimeout, TimeUnit.MILLISECONDS);
                            } catch (Exception e) {
                                bytes = null;
                                if (BuildConfig.DEBUG) {
                                    e.printStackTrace();
                                }
                                if (onCallBack != null) {
                                    onCallBack.onError(null, e);
                                }
                            }
                            if (bytes != null) {
                                startTime = System.currentTimeMillis();//刷新时间
                                pollTimeout = getPollTimeout();
                                try {
                                    outputStream.write(bytes);
                                    if (onCallBack != null) {
                                        onCallBack.onWrited(bytes);
                                    }
                                } catch (Exception e) {
                                    if (BuildConfig.DEBUG) {
                                        e.printStackTrace();
                                    }
                                    if (onCallBack != null) {
                                        onCallBack.onError(bytes, e);
                                    }
                                }
                            } else {
                                currentTime = System.currentTimeMillis();
                                long temp = currentTime - startTime;
                                if (temp >= releaseTimeout) {
                                    //超时
                                    break;
                                } else {
                                    //离超时释放时间少于获取元素的超时时间，重新设置获取元素的超时时间
                                    temp = releaseTimeout - temp;
                                    if (temp < pollTimeout) {
                                        pollTimeout = temp;
                                    }
                                }
                            }

                        }//while
                    }

                    private long getPollTimeout() {
                        return releaseTimeout / 10;//十分之一的时间获取超时
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (BuildConfig.DEBUG) {
                            if (onCallBack != null) {
                                onCallBack.onError(null, new Exception(throwable));
                            }
                        }
                    }
                });
    }

    public void stopWrite() {
        if (writeDisposable != null) {
            writeDisposable.dispose();
            writeDisposable = null;
        }
        writeQueue.clear();
    }

    public boolean isWriting() {
        return writeDisposable != null && !writeDisposable.isDisposed();
    }

    public void setCallBack(OnOutputStreamTaskListener onCallBack) {
        this.onCallBack = onCallBack;
    }

    public void setOutputStream(@Nullable OutputStream outputStream) {
        this.outputStream = outputStream;
    }

}
