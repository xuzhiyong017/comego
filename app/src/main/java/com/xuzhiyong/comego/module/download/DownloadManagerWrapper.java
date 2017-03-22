package com.xuzhiyong.comego.module.download;

import android.net.Uri;

import com.duowan.fw.Module;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JStringUtils;
import com.mozillaonline.providers.DownloadManager;

import java.io.File;

public class DownloadManagerWrapper implements IDownloadServiceWrapper{

    private static final String DLM_DISABLED = "content://downloads/my_downloads";

    @Override
    public long add(DownloadRequestInfo task) {
        if (null == DownloadSetup.manager()) {
            startDownloadService();
        }

        if (null == task) {
            return DownloadRequestInfo.RESULT_TASK_NULL;
        }

        if (JStringUtils.isNullOrEmpty(task.url)) {
            return DownloadRequestInfo.RESULT_URL_EMPTY;
        }

        if (!JStringUtils.isNullOrEmpty(task.folderPath)) {
            File file = new File(task.folderPath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return DownloadRequestInfo.RESULT_FOLDER_NONEXISTENT;
                }
            }
        }

        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(task.url));
            if (!JStringUtils.isNullOrEmpty(task.fileName) && !JStringUtils.isNullOrEmpty(task.folderPath)) {
                request.setDestinationUri(Uri.fromFile(new File(task.folderPath + "/" + task.fileName)));
            }
            if (!JStringUtils.isNullOrEmpty(task.mimeType)) {
                request.setMimeType(task.mimeType);
            }
            if (!JStringUtils.isNullOrEmpty(task.userAgent)) {
                request.addRequestHeader("User-Agent", task.userAgent);
            }
            return DownloadSetup.startDownload(request);
        } catch (IllegalArgumentException e) {
            // For system download manager
            if (e.getMessage().contains(DLM_DISABLED)) {
                return DownloadRequestInfo.RESULT_DOWNLOAD_MANAGER_DISABLED;
            }

            return DownloadRequestInfo.RESULT_URL_SCHEME_NOT_MATCHED;
        }
    }

    @Override
    public boolean start(long requestID) {
        return true;
    }

    @Override
    public boolean remove(long requestID, boolean removeFile) {
        return DownloadSetup.removeDownload(requestID) > 0;
    }

    @Override
    public void pause(long requestID) {
        try {
            DownloadSetup.pauseDownload(requestID);
        } catch (IllegalArgumentException e) {
            JLog.error(this, "pause download:" + requestID + "; exception:" + e.getMessage());
        }
    }

    @Override
    public void resume(long requestID) {
        try {
            DownloadSetup.resumeDownload(requestID);
        } catch (IllegalArgumentException e) {
            JLog.error(this, "resume download:" + requestID + "; exception:" + e.getMessage());
        }
    }

    @Override
    public void restart(long requestID) {
        DownloadSetup.restartDownload(requestID);
    }

    private synchronized void startDownloadService() {
        if (null == DownloadSetup.manager()) {
            DownloadSetup.startDownloadService(Module.gMainContext);
        }
    }

}
