package com.zch.last.listener;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.model.Progress;

/**
 * 进度状态listener
 *
 * @param <T> 传递的参数
 */
public class OnProgressStatesListener<T> implements ImplProgressStates<T> {
    private Context mContext = null;
    public boolean showProgressDialog = false;

    public OnProgressStatesListener() {
    }

    public OnProgressStatesListener(Context mContext) {
        this.mContext = mContext;
        showProgressDialog = this.mContext != null;
    }

    @Override
    public void onPrepared(@NonNull Progress<T> progress) throws Exception {

    }

    @Override
    public void onProgress(@NonNull Progress<T> progress) throws Exception {

    }

    @Override
    public void onComplete(boolean success, @NonNull String msg) throws Exception {

    }

    @Override
    public void onException(@Nullable Progress<T> progress, @NonNull Exception e) throws Exception {

    }


}
