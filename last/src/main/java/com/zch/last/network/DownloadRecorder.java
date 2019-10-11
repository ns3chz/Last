package com.zch.last.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.zch.last.core.LastApplication;
import com.zch.last.manager.SpManager;
import com.zch.last.network.model.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录保存下载任务
 */
public class DownloadRecorder {
    private static final String SP_NAME = "download_recorder";
    private static final String KEY_DOWNLOAD_ALREADY = "download_already";
    private static final String KEY_DOWNLOAD_ING = "download_ing";

    @Nullable
    private static List<DownloadInfo> downloadingList;
    private static List<DownloadInfo> downloadedList;
    private static final Object SYN_DOWNLOADING_LIST = new Object();
    private static final Object SYN_DOWNLOADED_LIST = new Object();

    @NonNull
    public static List<DownloadInfo> getDownloadingList() {
        if (downloadingList == null) {
            synchronized (SYN_DOWNLOADING_LIST) {
                if (downloadingList == null) {
                    downloadingList = SpManager.getBean(LastApplication.INSTANCE,
                            SP_NAME, KEY_DOWNLOAD_ING, "",
                            new TypeToken<List<DownloadInfo>>() {
                            }.getType());
                }
                if (downloadingList == null) {
                    downloadingList = new ArrayList<>();
                }
            }
        }

        return downloadingList;
    }

    @Nullable
    public static DownloadInfo getDownloadingInfo(@NonNull String url, @NonNull String path) {
        DownloadInfo info = new DownloadInfo(url, path);
        return getDownloadingInfo(info.baseUrl, info.resUrl, info.savePath, info.saveFileName);
    }

    public static DownloadInfo getDownloadingInfo(@NonNull String baseUrl, @NonNull String resUrl, @NonNull String filepath, @Nullable String fileName) {
        try {
            synchronized (SYN_DOWNLOADING_LIST) {
                List<DownloadInfo> infoList = getDownloadingList();
                DownloadInfo info;
                for (int i = 0; i < infoList.size(); i++) {
                    info = infoList.get(i);
                    if (info == null) continue;
                    if (info.equals(baseUrl, resUrl, filepath, fileName)) {
                        return info;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void removeDownloadingInfo(DownloadInfo info) {
        try {
            if (info == null) return;
            if (downloadingList != null) {
                synchronized (SYN_DOWNLOADING_LIST) {
                    DownloadInfo downloadInfo;
                    for (int i = 0; i < downloadingList.size(); i++) {
                        downloadInfo = downloadingList.get(i);
                        if (downloadInfo == null) continue;
                        if (downloadInfo.equals(info.baseUrl, info.resUrl, info.savePath, info.saveFileName)) {
                            downloadingList.remove(i);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param downloadInfo 增加和更新下载信息
     * @return true:新增成功,false:已存在相同下载
     */
    public static void refreshDownloadingInfo(DownloadInfo downloadInfo) {
        try {
            if (downloadInfo == null) return;
            DownloadInfo findInfo = getDownloadingInfo(downloadInfo.baseUrl, downloadInfo.resUrl,
                    downloadInfo.savePath, downloadInfo.saveFileName);
            synchronized (SYN_DOWNLOADING_LIST) {
                if (findInfo == null) {
                    List<DownloadInfo> list = getDownloadingList();
                    list.add(downloadInfo);
                } else {
                    findInfo.refresh(downloadInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveDownloadingList() {
        try {
            SpManager.putBean(LastApplication.INSTANCE, SP_NAME, KEY_DOWNLOAD_ING, downloadingList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //--------------------------下载完成-----------------------------------

    @NonNull
    public static List<DownloadInfo> getDownloadedList() {
        if (downloadedList == null) {
            synchronized (SYN_DOWNLOADED_LIST) {
                if (downloadedList == null) {
                    downloadedList = SpManager.getBean(LastApplication.INSTANCE,
                            SP_NAME, KEY_DOWNLOAD_ALREADY, "",
                            new TypeToken<List<DownloadInfo>>() {
                            }.getType());
                }
                if (downloadedList == null) {
                    downloadedList = new ArrayList<>();
                }
            }
        }

        return downloadedList;
    }

    @Nullable
    public static DownloadInfo getDownloadedInfo(@NonNull String baseUrl, @NonNull String resUrl, @NonNull String filepath, @Nullable String fileName) {
        try {
            if (downloadedList != null) {
                synchronized (SYN_DOWNLOADED_LIST) {
                    DownloadInfo info;
                    for (int i = 0; i < downloadedList.size(); i++) {
                        info = downloadedList.get(i);
                        if (info == null) continue;
                        if (info.equals(baseUrl, resUrl, filepath, fileName)) {
                            return info;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void removeDownloadedInfo(DownloadInfo info) {
        try {
            if (info == null) return;
            if (downloadedList != null) {
                synchronized (SYN_DOWNLOADED_LIST) {
                    DownloadInfo downloadInfo;
                    for (int i = 0; i < downloadedList.size(); i++) {
                        downloadInfo = downloadedList.get(i);
                        if (downloadInfo == null) continue;
                        if (downloadInfo.equals(info.baseUrl, info.resUrl, info.savePath, info.saveFileName)) {
                            downloadedList.remove(i);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param downloadInfo 增加和更新下载信息
     * @return true:新增成功,false:已存在相同下载
     */
    public static void refreshDownloadedInfo(DownloadInfo downloadInfo) {
        try {
            if (downloadInfo == null) return;
            DownloadInfo findInfo = getDownloadedInfo(downloadInfo.baseUrl, downloadInfo.resUrl,
                    downloadInfo.savePath, downloadInfo.saveFileName);
            synchronized (SYN_DOWNLOADED_LIST) {
                if (findInfo == null) {
                    List<DownloadInfo> list = getDownloadedList();
                    list.add(downloadInfo);
                } else {
                    findInfo.refresh(downloadInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveDownloadedList() {
        try {
            SpManager.putBean(LastApplication.INSTANCE, SP_NAME, KEY_DOWNLOAD_ALREADY, downloadedList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void change2Downloaded(DownloadInfo downloadInfo) {
        removeDownloadingInfo(downloadInfo);
        refreshDownloadedInfo(downloadInfo);
    }
}
