package com.xuzhiyong.comego.module.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.webkit.URLUtil;

import com.duowan.fw.util.JFileUtils;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JStringUtils;
import com.xuzhiyong.comego.module.DModule;

import java.io.File;

public class DownloadHelper {

    public static long addDownloadTask(DownloadRequestInfo task) {
        return DModule.ModuleDownload.cast(DownloadInterface.class).add(task);
    }

    public static long addDownloadTask(String url) {
        return addDownloadTask(url, null, null, null, null, null);
    }

    public static long addDownloadTask(String url, String fileName, String folderPath) {
        return addDownloadTask(url, fileName, folderPath, null, null, null);
    }

    public static long addDownloadTask(String url, String fileName, String folderPath, String contentDisposition, String mimeType, String userAgent) {
        DownloadRequestInfo info = new DownloadRequestInfo();
        info.url = url;
        info.mimeType = mimeType;
        info.userAgent = userAgent;
        info.fileName = JStringUtils.isNullOrEmpty(fileName) ? guessFileName(url, contentDisposition, mimeType) : fileName;
        info.folderPath = JStringUtils.isNullOrEmpty(folderPath) ? getDefaultDownloadPath() : folderPath;
        return addDownloadTask(info);
    }

    public static boolean removeDownloadTask(long requestID) {
        return removeDownloadTask(requestID, false);
    }

    public static boolean removeDownloadTask(long requestID, boolean removeFile) {
        return DModule.ModuleDownload.cast(DownloadInterface.class).remove(requestID, removeFile);
    }

    public static void restartDownloadTask(long requestID) {
        DModule.ModuleDownload.cast(DownloadInterface.class).restart(requestID);
    }

    public static void pauseDownloadTask(long requestID) {
        DModule.ModuleDownload.cast(DownloadInterface.class).pause(requestID);
    }

    public static void resumeDownloadTask(long requestID) {
        DModule.ModuleDownload.cast(DownloadInterface.class).resume(requestID);
    }

    public static String guessFileName(String url, String contentDisposition, String mimeType) {
        String fileName = null;
        if (URLUtil.isValidUrl(url)) {
            fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
        }
        return fileName;
    }

    public static String getDefaultDownloadPath() {
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DownloadRequestInfo.DEFAULT_PATH);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return null;
            }
        }

        if (folder.exists() && folder.isDirectory()) {
            return folder.getPath();
        }
        return null;
    }

    public static String getDownloadFolder(String path) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        path = JFileUtils.concatPath(root, path);
        File folder = new File(path);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return null;
            }
        }
        if (folder.exists() && folder.isDirectory()) {
            return folder.getPath();
        }
        return null;
    }

    public static long getRequestIdByUrl(String url) {
        return DModule.ModuleDownload.cast(DownloadInterface.class).getRequestIDByUrl(url);
    }

    public static DownloadSetup.JDownloadInfo getDownloadInfo(long requestID) {
        return DModule.ModuleDownload.cast(DownloadInterface.class).getInfo(requestID);
    }

    public static String guessFilePath(String url, String contentDisposition, String mimeType) {
        String path = getDefaultDownloadPath();
        if (null != path) {
            String fileName = guessFileName(url, contentDisposition, mimeType);
            if (!JStringUtils.isNullOrEmpty(fileName)) {
                return path + "/" + fileName;
            }
        }
        return null;
    }

    public static boolean installPackage(Context context, String filePath) {
        if (JStringUtils.isNullOrEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            JLog.error("DownloadHelper", "installPackage path:" + filePath + "; exception:" + e.toString());
            return false;
        }

    }

    public static boolean installPackage(Context context, Uri fileUri) {
        if (null == fileUri) {
            return false;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            JLog.error("DownloadHelper", "installPackage fileuri:" + fileUri + "; exception:" + e.toString());
            return false;
        }
    }

}
