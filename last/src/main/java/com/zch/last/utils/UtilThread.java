package com.zch.last.utils;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 线程
 */
public class UtilThread {

    /**
     * @param runnable 执行方法
     */
    public static Disposable runOnUiThread(@NonNull Runnable runnable) {
        return runOnUiThread(0, runnable);
    }

    /**
     * @param millesDelay 延迟时间
     * @param runnable    执行方法
     */
    public static Disposable runOnUiThread(long millesDelay, @NonNull Runnable runnable) {
        return runOnScheduler(millesDelay, AndroidSchedulers.mainThread(), runnable);
    }

    /**
     * @param scheduler 执行线程
     * @param runnable  执行方法
     */
    public static Disposable runOnScheduler(@NonNull Scheduler scheduler, @NonNull Runnable runnable) {
        return runOnScheduler(0, scheduler, runnable);
    }

    /**
     * @param millesDelay 延迟时间(毫秒)
     * @param scheduler   执行线程
     * @param runnable    执行方法
     */
    public static Disposable runOnScheduler(long millesDelay, @NonNull Scheduler scheduler, @NonNull Runnable runnable) {
        Observable<Runnable> runnableObservable = Observable.just(runnable)
                .subscribeOn(Schedulers.computation());
        if (millesDelay > 0) {
            runnableObservable = runnableObservable.delay(millesDelay, TimeUnit.MILLISECONDS, scheduler, true);
        } else {
            runnableObservable = runnableObservable.observeOn(scheduler);
        }
        return runnableObservable.subscribe(new Consumer<Runnable>() {
            @Override
            public void accept(Runnable runnable) throws Exception {
                runnable.run();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

}
