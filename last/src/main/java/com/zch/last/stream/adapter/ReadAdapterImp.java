package com.zch.last.stream.adapter;

import io.reactivex.annotations.NonNull;

public interface ReadAdapterImp{
    void read(@NonNull final byte[] BUFFER, int length);
}
