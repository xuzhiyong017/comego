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
