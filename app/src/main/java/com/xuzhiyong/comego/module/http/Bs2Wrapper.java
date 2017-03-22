package com.xuzhiyong.comego.module.http;

import android.text.TextUtils;

import com.duowan.fw.util.JFileUtils;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JMD5Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import com.xuzhiyong.comego.module.http.HttpHelper.Bs2UploadException;
import com.xuzhiyong.comego.module.http.HttpHelper.DownloadImage;
import com.xuzhiyong.comego.module.http.HttpHelper.ImageDownloadListener;
import com.xuzhiyong.comego.module.http.HttpHelper.OperationTimeoutException;
import com.xuzhiyong.comego.module.http.HttpHelper.UploadListener;

/**
 * Created by xuzhiyong on 16-4-11.
 */
class Bs2Wrapper {

    private static final String TAG = "Bs2Upload";

    static final String MIME_IMG = "image/jpeg";
    static final String MIME_ZIP = "application/x-zip-compressed";

    static String generateFileName(File file) {
        return "WEPLAY_" + JMD5Utils.md5(file) + ".jpg";
    }

    static void uploadFile(final File file, String fileName, final HttpHelper.UploadListener listener) {
        uploadFile(file, fileName, MIME_IMG, listener);
    }

    static void uploadFile(final File file, String fileName, final String mimeType, final UploadListener listener) {
        if (null == file) {
            if (null != listener) {
                listener.onError(new Bs2UploadException("file null", UploadListener.ERR_FILE_NULL));
            }
            return;
        }
        if (!file.exists()) {
            if (null != listener) {
                listener.onError(new Bs2UploadException("file not exist", UploadListener.ERR_FILE_NOT_EXIST));
            }
            return;
        }

        if (TextUtils.isEmpty(fileName)) {
            fileName = generateFileName(file);
        }
        final String key = fileName;

//        Observable<Proto> token = getToken(key);
//        if (null != token) {
//            token.observeOn(Schedulers.io())
//                    .subscribe(new Action1<Proto>() {
//                        @Override
//                        public void call(Proto proto) {
//                            if (proto.body.result.success
//                                    && null != proto.body.uptokenRes
//                                    && !TextUtils.isEmpty(proto.body.uptokenRes.bs2token)) {
//                                UptokenRes res = proto.body.uptokenRes;
//                                byte[] data = null;
//                                try {
//                                    data = JFileUtils.readFile(file);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                    JLog.error(TAG, "read bytes from file failed:" + e.getMessage());
//                                    if (null != listener) {
//                                        listener.onError(new Bs2UploadException("read byte failed", UploadListener.ERR_READ_BYTES, e));
//                                    }
//                                }
//                                if (null != data) {
//                                    doUpload(appInfo(CloudStorageConst.BUCKET_NAME, res.bs2token), data, key, mimeType, listener);
//                                }
//                            } else if (null != listener) {
//                                Bs2UploadException bse = new Bs2UploadException("get token failed", UploadListener.ERR_GET_TOKEN_FAILED);
//                                bse.proto = proto;
//                                listener.onError(bse);
//                            }
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//                            if (null != listener) {
//                                Bs2UploadException bse = new Bs2UploadException("get token timeout", UploadListener.ERR_GET_TOKEN_TIMEOUT, throwable);
//                                listener.onError(bse);
//                            }
//                        }
//                    });
//        } else if (null != listener) {
//            listener.onError(new Bs2UploadException("Bs2Upload login state offline", UploadListener.ERR_OFFLINE));
//        }
    }

//    private static Observable<Proto> getToken(final String key) {
//        if (LoginHelper.isOnLine()) {
//            return Observable.create(new OnSubscribe<Proto>() {
//                @Override
//                public void call(final Subscriber<? super Proto> subscriber) {
//                    final ProtoHandler handler = new ProtoHandler() {
//                        @Override
//                        public void onRespond(Proto proto) {
//                            subscriber.onNext(proto);
//                            subscriber.onCompleted();
//                        }
//
//                        @Override
//                        public void onTimeOut(Proto proto) {
//                            subscriber.onError(new OperationTimeoutException(proto));
//                        }
//                    };
//                    doGetToken(key, handler);
//                }
//            });
//        } else {
//            // TODO: 16-4-11 get token by http
//            return null;
//        }
//    }

//    private static AppInfo appInfo(String bucket, String token) {
//        AppInfo info = new AppInfo();
//        info.setBucket(bucket);
//        info.setToken(token);
//        return info;
//    }
//
//    private static void doGetToken(String fileName, ProtoHandler handler) {
//        UptokenReq uptokenReq = UptokenReq.newBuilder()
//                .cookie(LoginHelper.curLoginData().cookie)
//                .buckets(new ArrayList<String>())
//                .filename(fileName)
//                .build();
//        NetRequest.newBuilder().setGroup(PType.PLogin)
//                .setReqSub(SPLogin.PUptokenReq)
//                .setResSub(SPLogin.PUptokenRes)
//                .setMessage(NetHelper.pbb().uptokenReq(uptokenReq).build())
//                .setTimeOut(DConst.KC_MaxNetOperatorTimeOut)
//                .setHandler(handler)
//                .request();
//    }
//
//    private static void doUpload(AppInfo info, byte[] data, final String key, String mimeType, final UploadListener listener) {
//        UploadClient client = Bs2Client.newUploadClient(info, new BS2TaskListener() {
//            @Override
//            public void onBeginOfTask() {
//                if (null != listener) {
//                    listener.onProgress(UploadListener.PROGRESS_START);
//                }
//            }
//
//            @Override
//            public void onEndOfTask() {
//                if (null != listener) {
//                    listener.onProgress(UploadListener.PROGRESS_END);
//                }
//            }
//
//            @Override
//            public void onResult(Object result) {
//                if (null != listener && result instanceof CallRet) {
//                    if (((CallRet) result).getCode() == 200) {
//                        listener.onUploadSucceed(key);
//                    } else {
//                        listener.onUploadFailed(key, result.toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onProgress(int progress) {
//                if (null != listener) {
//                    listener.onProgress(progress);
//                }
//            }
//
//            @Override
//            public void OnError(Result error) {
//                if (null != listener) {
//                    listener.onError(error);
//                }
//            }
//        });
//        try {
//            client.Upload(data, key, mimeType, -1);
//        } catch (Exception e) {
//            e.printStackTrace();
//            JLog.error("Bs2Wrapper", "doUpload exception:" + e.getMessage());
//            if (null != listener) {
//                listener.onError(new Bs2UploadException("doUpload exception", UploadListener.ERR_DO_UPLOAD, e));
//            }
//        }
//    }
}
