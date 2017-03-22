package com.xuzhiyong.comego.module.download;

public class DownloadRequestInfo {

    public static final String DEFAULT_PATH = "/duowan/ada/downloads";

    public static final long RESULT_TASK_NULL = -1;
    public static final long RESULT_URL_EMPTY = -2;
    public static final long RESULT_URL_SCHEME_NOT_MATCHED = -3;
    public static final long RESULT_FOLDER_NONEXISTENT = -4;
    public static final long RESULT_DOWNLOAD_MANAGER_DISABLED = -5;

    public String url;
    public String fileName;
    public String folderPath;
    public String mimeType;
    public String userAgent;
}
