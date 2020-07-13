package com.zch.last.stream.adapter;

import androidx.annotation.IntRange;

import com.zch.last.utils.UtilObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class ReadHeadTailAdapter extends ReadAdapterAbs {
    /**
     * 报头
     */
    @Nullable
    public final byte[] head;
    /**
     * 报尾，无报尾则尽快返回值
     */
    @Nullable
    public final byte[] tail;

    public ReadHeadTailAdapter(@NonNull OnCallBack callBack) {
        this(100, callBack);
    }

    public ReadHeadTailAdapter(int timeout, @NonNull OnCallBack callBack) {
        this(null, null, timeout, callBack);
    }

    public ReadHeadTailAdapter(@Nullable byte[] head, @Nullable byte[] tail, @IntRange(from = 0, to = Integer.MAX_VALUE) int timeout,
                               @NonNull OnCallBack callBack) {
        super(timeout, callBack);
        this.head = head;
        this.tail = tail;
    }

    public void read(@NonNull final byte[] BUFFER) {
        super.read(BUFFER, BUFFER.length);
    }

    @Override
    public void read(@NonNull byte[] BUFFER, int length) {
        long currentTimeMillis;
        //判断超时
        if (timestart > 0 && timeout > 0) {
            currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - timestart >= timeout) {
                notifyCallbackTimeout(timestart, currentTimeMillis, timeout);
                setState(State.TIMEOUT);
                clear();
                return;
            }
        }
        if (length == 0) {
            return;
        }
        if (data == null) {
            setState(State.READING);
            timestart = System.currentTimeMillis();
            data = new byte[length];
            System.arraycopy(BUFFER, 0, data, 0, length);
        } else {
            byte[] temp = data;
            data = new byte[temp.length + length];
            System.arraycopy(temp, 0, data, 0, temp.length);
            System.arraycopy(BUFFER, 0, data, temp.length, length);
        }
        checkSelf();
    }

    /**
     * 检查报头报尾
     */
    private void checkSelf() {
        if (data == null) return;
        boolean findHead = true;
        boolean findTail = true;
        byte[][] headTail;
        if (head != null && head.length > 0) {
            //对报头有要求
            headTail = UtilObject.findHeadTail(data, head, true, true);
            data = headTail[0];
            findHead = headTail[1] != null;
        }
        if (findHead) {
            if (tail != null && tail.length > 0) {
                //对报尾有要求
                headTail = UtilObject.findHeadTail(data, tail, false, true);
                data = headTail[0];
                findTail = headTail[1] != null;
            }
        }

        if (findHead && findTail) {
            if (data != null && data.length > 0) {
                notifyCallbackRead(data, System.currentTimeMillis() - timestart);
                setState(State.FINISHED);
                clear();
            }
        }
    }

    @Override
    public boolean merge(ReadAdapterAbs imp) {
        if (!(imp instanceof ReadHeadTailAdapter)) {
            return false;
        }
        ReadHeadTailAdapter adapter = (ReadHeadTailAdapter) imp;
        if (this.timeout != adapter.timeout) return false;
        if (!UtilObject.equalsValue(this.head, adapter.head)) {
            return false;
        }
        if (!UtilObject.equalsValue(this.tail, adapter.tail)) {
            return false;
        }
        super.callBackSet.addAll(adapter.callBackSet);
        return true;
    }


}
