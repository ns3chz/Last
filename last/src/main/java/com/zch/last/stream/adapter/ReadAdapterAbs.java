package com.zch.last.stream.adapter;


import androidx.annotation.IntRange;

import com.zch.last.BuildConfig;
import com.zch.last.utils.UtilLogger;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ReadAdapterAbs implements ReadAdapterImp {
    public final String TAG = getClass().getName();

    public enum State {
        NONE,//无数据
        READING,//正在读
        TIMEOUT,//超时
        FINISHED//完成
    }

    /**
     * 回调
     */
    @NonNull
    protected final Set<OnCallBack> callBackSet;
    protected long timestart = -1;
    /**
     * 超时时间
     */
    public final int timeout;
    @Nullable
    protected byte[] data;//当前数据
    @NonNull
    protected State mState = State.NONE;

    public ReadAdapterAbs(@NonNull OnCallBack callBack) {
        this(100, callBack);
    }

    public ReadAdapterAbs(@IntRange(from = 0, to = Integer.MAX_VALUE) int timeout, @NonNull OnCallBack callBack) {
        this.timeout = timeout;
        this.callBackSet = new CopyOnWriteArraySet<>();
        this.callBackSet.add(callBack);
    }

    @Override
    public void read(@NonNull byte[] BUFFER, int length) {
        timestart = System.currentTimeMillis();
        setState(State.READING);
        byte[] data = new byte[length];
        System.arraycopy(BUFFER, 0, data, 0, length);
        long current = System.currentTimeMillis();
        long temp = current - timestart;
        if (timeout > 0) {
            if (temp >= timeout) {
                setState(State.TIMEOUT);
                notifyCallbackTimeout(timestart, current, timeout);
                return;
            }
        }
        notifyCallbackRead(data, temp);
        setState(State.FINISHED);
        clear();
    }

    /**
     * 合并
     *
     * @return 当报头，报尾，超时时间一致时，合并回调
     */
    public boolean merge(ReadAdapterAbs imp) {
        if (imp == null) return false;
        if (timeout != imp.timeout) return false;
        this.callBackSet.addAll(imp.callBackSet);
        return true;
    }

    /**
     * @param data      数据
     * @param timecount 用时
     */
    protected void notifyCallbackRead(@NonNull final byte[] data, final long timecount) {
        final byte[] DATA = new byte[data.length];
        System.arraycopy(data, 0, DATA, 0, data.length);
        Disposable subscribe = Observable.just("")
                .observeOn(Schedulers.computation())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        for (OnCallBack onCallBack : callBackSet) {
                            byte[] newData = new byte[DATA.length];
                            System.arraycopy(DATA, 0, newData, 0, DATA.length);
                            onCallBack.read(newData, timecount);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param start   开始接收时间，
     * @param current 当前处理时间
     * @param timeout 超时时间
     */
    protected void notifyCallbackTimeout(final long start, final long current, final int timeout) {
        Disposable subscribe;
        for (OnCallBack callBack : callBackSet) {
            if (callBack == null) continue;
            subscribe = Observable.just(callBack)
                    .observeOn(Schedulers.computation())
                    .subscribe(new Consumer<OnCallBack>() {
                        @Override
                        public void accept(OnCallBack onCallBack) throws Exception {
                            onCallBack.timeout(start, current, timeout);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (BuildConfig.DEBUG) {
                                throwable.printStackTrace();
                            }
                        }
                    });
        }
    }

    protected void clear() {
        data = null;
        timestart = -1;
    }

    public void setState(@NonNull State state) {
        mState = state;
    }

    @NonNull
    public State getState() {
        return mState;
    }

    public boolean remove(OnCallBack callBack) {
        try {
            return this.callBackSet.remove(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void LOG(String msg) {
        UtilLogger.logV(TAG, msg);
    }

    public interface OnCallBack {
        void read(@NonNull byte[] data, long time);

        /**
         * @param start   开始
         * @param current 当前
         * @param timeout 超时时间大小
         */
        void timeout(long start, long current, int timeout);
    }
}
