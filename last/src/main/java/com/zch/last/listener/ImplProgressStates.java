package com.zch.last.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.model.Progress;

public interface ImplProgressStates<T> {

    void onPrepared(@NonNull Progress<T> progress) throws Exception;

    void onProgress(@NonNull Progress<T> progress) throws Exception;

    void onComplete(boolean success, @NonNull String msg) throws Exception;

    void onException(@Nullable Progress<T> progress, @NonNull Exception e) throws Exception;

}
