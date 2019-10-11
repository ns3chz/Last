package com.zch.last.model;

import android.os.SystemClock;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import com.zch.last.listener.OnProgressStatesListener;
import com.zch.last.utils.UtilObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Progress<T> extends BaseCloneableModel {
    private static final long serialVersionUID = 5144038986120495808L;

    @IntRange(from = 15, to = 1000)
    private transient static final int REFRESH_INTERVAL_TIME = 300;//刷新进度间隔时间，毫秒
    private transient static final int MAX_COUNT_SPEED = 10;//总的最大参考个数

    @Nullable
    public transient T data;
    public long totalSize;//文件总共大小
    public long currentWritedSize;//当前已写入大小
    public long startReadPosition = 0;//输入流读取开始位置
    public boolean isUseStartReadPosition = true;
    public long startWritePosition = 0;//输出流写入开始位置
    public long needWriteLenth = -1;//剩下需要写入的大小,-1：写到最后
    @IntRange(from = 1, to = 1073741824)
    public int WRITE_SIZE = 1024;//缓存大小,默认1024,最小1byte,最大1G:1024x1024x1024

    public transient float fraction;//进度百分比
    public transient long speed;//及时速度,byte/秒
    public transient long speedAverage;//平均速度,byte/秒
    public transient OnProgressStatesListener<T> statesListener;

    private transient long tempSize;//一段时间内的传输大小
    private transient long lastRefreshTime;//上次记录的时间
    private transient final List<Long> speedList;//记录每段速度，计算平均速度

    public Progress() {
        this(0);
    }

    public Progress(long totalSize) {
        this.totalSize = totalSize;
        this.speedList = new ArrayList<>();
    }

    /**
     * 刷新
     *
     * @param writeSize 传输大小
     */
    public Progress refresh(int writeSize) {
        long currentTime = SystemClock.elapsedRealtime();

        //计算剩下需要write的大小
        this.currentWritedSize += writeSize;
        if (this.needWriteLenth > 0) {
            this.needWriteLenth -= writeSize;
        }
        this.tempSize += writeSize;

        long differTime = currentTime - this.lastRefreshTime;
        boolean shouldRefresh = differTime >= REFRESH_INTERVAL_TIME;

        if (shouldRefresh || this.needWriteLenth == 0 || (this.totalSize > 0 && this.currentWritedSize == totalSize)) {
            if (differTime <= 0) {
                differTime = 1;
            }
            if (this.needWriteLenth >= 0) {
                this.fraction = 1f * this.currentWritedSize / (this.currentWritedSize + this.needWriteLenth);
            } else if (this.totalSize > 0) {
                this.fraction = 1f * this.currentWritedSize / this.totalSize;
            } else {
                this.fraction = -1;
            }
            this.speed = (this.tempSize * 1000) / differTime;//瞬时时间
            averSpeed(this.speed);//计算平均时间

            //
            this.lastRefreshTime = currentTime;
            this.tempSize = 0;
            if (statesListener != null) {
                try {
                    statesListener.onProgress(this);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        statesListener.onException(this, e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            this.speed = 0;
        }
        return this;
    }

    /**
     * @return 计算平均速度
     */
    private void averSpeed(long newSpeed) {
        int lastSize = speedList.size();
        speedList.add(newSpeed);
        long totalAdd = this.speedAverage * lastSize;
        while (speedList.size() > MAX_COUNT_SPEED) {
            long first = speedList.get(0);
            totalAdd -= first;
            speedList.remove(0);
        }
        totalAdd += newSpeed;
        this.speedAverage = totalAdd / speedList.size();
    }

    public void resetRecordParams() {
        resetRecordParams(null);
    }

    public void resetRecordParams(@Nullable Progress progress) {
        if (progress == null) {
            this.totalSize = 0;
            this.currentWritedSize = 0;

            this.startReadPosition = 0;//输入流读取开始位置
            this.startWritePosition = 0;//输出流写入开始位置
            this.needWriteLenth = -1;//需要写入的大小
            WRITE_SIZE = 1024;
        } else {
            this.totalSize = progress.totalSize;
            this.currentWritedSize = progress.currentWritedSize;

            this.startReadPosition = progress.startReadPosition;//输入流读取开始位置
            this.startWritePosition = progress.startWritePosition;//输出流写入开始位置
            this.needWriteLenth = progress.needWriteLenth;//需要写入的大小
            WRITE_SIZE = progress.WRITE_SIZE;
        }

    }

    /**
     * 重置参数
     */
    public void resetImmediatelyParams() {
        this.fraction = 0;
        this.speed = 0;
        this.speedAverage = 0;
        this.tempSize = 0;
        this.lastRefreshTime = 0;
        this.speedList.clear();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Progress)) return false;
        Progress progress = (Progress) obj;

        return UtilObject.equals(this.totalSize, progress.totalSize) &&
                UtilObject.equals(this.currentWritedSize, progress.currentWritedSize) &&
                UtilObject.equals(this.startReadPosition, progress.startReadPosition) &&
                UtilObject.equals(this.startWritePosition, progress.startWritePosition) &&
                UtilObject.equals(this.needWriteLenth, progress.needWriteLenth);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.totalSize, this.currentWritedSize, this.startReadPosition,
                this.startWritePosition, this.needWriteLenth});
    }
}
