package com.zch.last.stream.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class OnOutputStreamTaskListener {
    public void onWriteBefore(@NonNull byte[] data) {

    }

    public abstract void onWrited(@NonNull byte[] data);

    public void onError(@Nullable byte[] data, @NonNull Exception e) {

    }
}
