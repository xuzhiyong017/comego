package com.xuzhiyong.comego.module.download;

public interface DownloadInterface {
    void syncDownloadList();
    void tryStopSyncList();
    long add(DownloadRequestInfo info);
    boolean start(long requestID);
    boolean remove(long requestID, boolean removeFile);
    void pause(long requestID);
    void resume(long requestID);
    void restart(long requestID);
    long getRequestIDByUrl(String url);
    DownloadSetup.JDownloadInfo getInfo(long requestID);
}
