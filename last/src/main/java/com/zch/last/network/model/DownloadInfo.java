package com.zch.last.network.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.model.BaseCloneableModel;
import com.zch.last.model.Progress;
import com.zch.last.utils.UtilHTTP;
import com.zch.last.utils.UtilObject;

import java.util.Arrays;

public class DownloadInfo<T> extends BaseCloneableModel {
    private static final long serialVersionUID = 7665321162888202808L;
    @NonNull
    public final String baseUrl;
    @NonNull
    public final String resUrl;
    @NonNull
    public final String savePath;//文件夹目录
    @Nullable
    public String saveFileName;
    @Nullable
    public Progress<T> progress;
    public long createdTime;

    /**
     * @param url 完整的url
     */
    public DownloadInfo(@NonNull String url, @NonNull String savePath) {
        String[] httpUrl = UtilHTTP.splitHTTPUrl(url);
        this.baseUrl = httpUrl[0];
        this.resUrl = httpUrl[1];
        this.savePath = savePath;
    }

    /**
     * @param savePath 保存地址
     */
    public DownloadInfo(@NonNull String baseUrl, @NonNull String resUrl, @NonNull String savePath) {
        this.baseUrl = baseUrl;
        this.resUrl = resUrl;
        this.savePath = savePath;
    }


    public void refresh(@NonNull DownloadInfo info) {
        this.saveFileName = info.saveFileName;
        this.createdTime = info.createdTime;
        if (this.progress != null) {
            this.progress.resetRecordParams(info.progress);
        }
    }

    public boolean equals(@NonNull String bUrl, @NonNull String rUrl, @NonNull String sfPath, @Nullable String fName) {
        return UtilObject.equals(this.baseUrl, bUrl) &&
                UtilObject.equals(this.resUrl, rUrl) &&
                UtilObject.equals(this.savePath, sfPath) &&
                UtilObject.equals(this.saveFileName, fName);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof DownloadInfo)) return false;
        DownloadInfo info = (DownloadInfo) obj;
        return equals(info.baseUrl, info.resUrl, info.savePath, info.saveFileName);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.baseUrl, this.resUrl,
                this.savePath, this.saveFileName});
    }
}
