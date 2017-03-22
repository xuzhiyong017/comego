package com.xuzhiyong.comego.module.http;

import com.duowan.fw.util.JFileUtils;
import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.ui.utils.UIConst;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HttpHelper {

    /// call in background io thread
    public interface UploadListener {
        int PROGRESS_START = -100;
        int PROGRESS_END = -200;
        int ERR_FILE_NULL = 0xff001;
        int ERR_FILE_NOT_EXIST = 0xff002;
        int ERR_OFFLINE = 0xff003;
        int ERR_GET_TOKEN_TIMEOUT = 0xff004;
        int ERR_GET_TOKEN_FAILED = 0xff005;
        int ERR_READ_BYTES = 0xff006;
        int ERR_DO_UPLOAD = 0xff007;
        void onProgress(int progress);
        void onUploadSucceed(String key);
        void onUploadFailed(String key, String reason);
        void onError(Exception error);
    }

    public interface ImageDownloadListener {
        void onResult(boolean success, String path);
    }

    public static class OperationTimeoutException extends Exception {
        public OperationTimeoutException() {

        }
    }

    public static class Bs2UploadException extends Exception {
        public int code;

        public Bs2UploadException(String message, int code) {
            this(message, code, null);
        }

        public Bs2UploadException(String message, int code, Throwable cause) {
            super("Bs2Upload " + message, cause);
            this.code = code;
        }
    }

    public static String getImageUrl(String key) {
        return CloudStorageConst.URL_DOWNLOAD + key;
    }

    public static void uploadImage(String path, UploadListener listener) {
        uploadImage(new File(path), listener);
    }

    public static void uploadImage(File file, UploadListener listener) {
        Bs2Wrapper.uploadFile(file, null, listener);
    }

    public static void uploadImage(File file, String fileName, UploadListener listener) {
        Bs2Wrapper.uploadFile(file, fileName, listener);
    }

    public static void uploadZip(File file, String fileName, UploadListener listener) {
        Bs2Wrapper.uploadFile(file, fileName, Bs2Wrapper.MIME_ZIP, listener);
    }

    public static String getThumbnailUrl(String url) {
        if (url.startsWith(CloudStorageConst.URL_DOWNLOAD) || url.startsWith("http://image.yy.com")) {
            return url + UIConst.ThumbnailDefaultSmall;
        }
        return url;
    }

    public static String getThumbnailUrlBig(String url) {
        if (url.startsWith(CloudStorageConst.URL_DOWNLOAD) || url.startsWith("http://image.yy.com")) {
            return url + UIConst.ThumbnailDefaultBig;
        }
        return url;
    }

    public static String generateFileName(File file) {
        return Bs2Wrapper.generateFileName(file);
    }

    public static void downloadImage(String url, final String filePath, final ImageDownloadListener listener) {
        int index = url.lastIndexOf("/");
        String name = url.substring(index + 1);
        String baseUrl = url.substring(0, index + 1);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        DownloadImage downloadImage = retrofit.create(DownloadImage.class);
        downloadImage.downloadImage(name).subscribeOn(Schedulers.io())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        JFileUtils fileUtils = null;
                        try {
                            fileUtils = JFileUtils.openFile(filePath);
                            fileUtils.write(responseBody.bytes());
                            fileUtils.close();
                            if (null != listener) {
                                listener.onResult(true, filePath);
                            }
                        } catch (Exception e) {
                            JLog.error("HttpHelper", "downloadImage save response failed:" + e.getMessage());
                            if (null != listener) {
                                listener.onResult(false, filePath);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        JLog.error("HttpHelper", "downloadImage failed:" + throwable.getMessage());
                        if (null != listener) {
                            listener.onResult(false, filePath);
                        }
                    }
                });
    }

    interface DownloadImage {
        @GET("{name}")
        Observable<ResponseBody> downloadImage(@Path("name") String name);
    }
}
