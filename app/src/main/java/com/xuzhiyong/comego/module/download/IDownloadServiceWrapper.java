package com.xuzhiyong.comego.module.download;

public interface IDownloadServiceWrapper {
    long add(DownloadRequestInfo info);
    boolean start(long requestID);
    boolean remove(long requestID, boolean removeFile);
    void pause(long requestID);
    void resume(long requestID);
    void restart(long requestID);
}
