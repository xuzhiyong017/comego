package com.duowan.fw.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import com.duowan.fw.R;
import com.duowan.fw.root.BaseContext;

public class JHttpLogSender {

    public interface HttpLogCallback {
        void onSendFinished(int result);
    }

    static final String LOG_KEY = "yynumber";
    static final String LOG_NET = "net";
    static final String LOG_IMEI = "imei";
    static final String LOG_VENDOR = "vendor";
    static final String LOG_MODEL = "model";
    static final String LOG_OSVER = "osver";
    static final String LOG_APPPLATFORM = "appplatform";
    static final String LOG_APPPID = "appid";
    static final String LOG_TYPE = "logtype";
    static final String LOG_APPVER = "appver";
    static final String LOG_FEEDBACK = "feedback";

    private static final String LOG_APPID = "appId";
    private static final String LOG_SIGN = "sign";
    private static final String LOG_DATA = "data";
    private static final String LOG_FILE = "file";

    public static final String LOG_REPORT_APPID = "more-android";
    public static final String LOG_REPORT_SIGN_KEY = "fjdie134bd0amewien301";
    private static final String LOG_REPORT_POST_URL = "http://clientreport.yy.com/v1";
    private static final String FEEDBACK_POST_URL = "http://reportplf.yy.com/userFeedback";//"http://115.238.170.90:8082/userFeedback";

    private Context mContext = null;
    private HttpLogCallback mCallback = null;
    private String mPostUrl = null;
    private String mLogPostUrl = null;
    private String mFeedbackPostUrl = null;

    public JHttpLogSender(Context context) {
        mContext = context;
        mPostUrl = mContext.getString(R.string.feedback_post_url);
        mLogPostUrl = LOG_REPORT_POST_URL;
        mFeedbackPostUrl = FEEDBACK_POST_URL;
    }

    public void sendCrashLog(String[] fileNames) {
        new AsyncTask<String, Void, Integer>() {
            protected Integer doInBackground(String... str) {
                return submitCrashLog(str);
            }
        }.execute(fileNames);
    }

    public void sendCrashLogEx(byte[] data) {
        submitCrashLog(data);
    }

    public void sendCrashLog(byte[] data) {
        final byte[] pData = data;
        Runnable r = new Runnable() {
            
            public void run() {
                submitCrashLog(pData);
            }
        };
        (new Thread(r)).start();
    }

    public void sendCrashLog(final String sign, final String data, final byte[] file) {
        Runnable r = new Runnable() {

            public void run() {
                submitCrashLog(sign, data, file);
            }
        };
        (new Thread(r)).start();
    }

    public void sendFeedback(String feedbackString, HttpLogCallback callback, boolean attachLog) {
        mCallback = callback;
        
        String feedback = attachLog ? JLog.getFeedBackLogString() : null;
        new AsyncTask<String, Void, Integer>() {
            protected Integer doInBackground(String... str) {
                return submitFeedback(str[0], str[1]);
            }

            protected void onPostExecute(Integer result) {
                onFeedbackSent(result);
            }
        }.execute(feedbackString, feedback);
    }

    private void onFeedbackSent(int result) {
        if (mCallback != null) {
            mCallback.onSendFinished(result);
        }
    }

    private String getVersion() {
        return JVersionUtil.getLocalName(mContext);
    }

    private int submitCrashLog(byte[] data) {
        JHttpMultipartPost log = new JHttpMultipartPost();
        JHttpMultipartPost.MultipartDataPacker packer = createLogBody(log);
        if (packer != null) {
            packer.addPart(LOG_TYPE, "crash");
            packer.addFilePart("log1", data, JHttpMultipartPost.ZIP_TYPE);
            packer.finish();
            return log.submit(mPostUrl, packer);
        }
        return 0;
    }

    private int submitCrashLog(String sign, String data, byte[] file) {
        JHttpMultipartPost log = new JHttpMultipartPost();
        JHttpMultipartPost.MultipartDataPacker packer = createLogReportBody(log);
        if (packer != null) {
            packer.addPart(LOG_SIGN, JHttpMultipartPost.TEXT_TYPE, sign);
            packer.addPart(LOG_DATA, JHttpMultipartPost.TEXT_TYPE, data);
            packer.addFilePart(LOG_FILE, file, JHttpMultipartPost.ZIP_TYPE);
            packer.finish();
            return log.submit(mLogPostUrl, packer);
        }
        return 0;
    }

    private int submitCrashLog(String[] fileNames) {
        JHttpMultipartPost log = new JHttpMultipartPost();
        JHttpMultipartPost.MultipartDataPacker packer = createLogBody(log);
        if (packer != null) {
            packer.addPart(LOG_TYPE, "crash");

            for (int i = 0; i < fileNames.length; i++) {
                packer.addFilePart("log" + (i + 1), fileNames[i], JHttpMultipartPost.ZIP_TYPE);
            }

            return log.submit(mPostUrl, packer);
        }
        return 0;
    }

//    public int submitFeedback(String feedback, String logstring) {
//        JHttpMultipartPost log = new JHttpMultipartPost();
//        JHttpMultipartPost.MultipartDataPacker packer = createLogBody(log);
//        if (packer != null) {
//            packer.addPart(LOG_TYPE, "feedback");
//            packer.addPart(LOG_FEEDBACK, feedback);
//            if (logstring != null) {
//                packer.addFilePart("log1", logstring.getBytes(), JHttpMultipartPost.ZIP_TYPE);
//            }
//            packer.finish();
//        }
//        return log.submit(mPostUrl, packer);
//    }
    public int submitFeedback(String feedback, String logstring) {
        JHttpMultipartPost log = new JHttpMultipartPost();
        JHttpMultipartPost.MultipartDataPacker packer = createFeedbackBody(log, feedback);
        if (packer != null) {
            if (logstring != null) {
                packer.addFilePart(LOG_FILE, logstring.getBytes(), JHttpMultipartPost.ZIP_TYPE);
            }
            packer.finish();
            return log.submit(mFeedbackPostUrl, packer);
        }
        return 0;
    }


    private JHttpMultipartPost.MultipartDataPacker createLogReportBody(JHttpMultipartPost log) {
        JHttpMultipartPost.MultipartDataPacker packer = null;
        try {
            packer = log.getDataPacker(mContext, JHttpMultipartPost.ZLIB_COMPRESS);
            packer.addPart(LOG_APPID, JHttpMultipartPost.TEXT_TYPE, LOG_REPORT_APPID);
        } catch (Exception e) {
            JLog.error(this, "can not create MultipartDataPacker");
        }
        return packer;
    }

    private JHttpMultipartPost.MultipartDataPacker createFeedbackBody(JHttpMultipartPost log, String feedback) {
        JHttpMultipartPost.MultipartDataPacker packer = null;
        try {
            packer = log.getDataPacker(mContext, JHttpMultipartPost.ZLIB_COMPRESS);
            packer.addPart("nyy", JHttpMultipartPost.TEXT_TYPE, feedback);
        } catch (Exception e) {
            JLog.error(this, "can not create MultipartDataPacker");
        }
        return packer;
    }

    private JHttpMultipartPost.MultipartDataPacker createLogBody(JHttpMultipartPost log) {
        JHttpMultipartPost.MultipartDataPacker packer = null;
        try {
            packer = log.getDataPacker(mContext, JHttpMultipartPost.ZLIB_COMPRESS);
            packer.addPart(LOG_APPPLATFORM, "2");
            packer.addPart(LOG_APPPID, "com.duowan.ada");
            packer.addPart(LOG_MODEL, Build.MODEL);
            packer.addPart(LOG_APPVER, getVersion());
            packer.addPart(LOG_IMEI, JUtils.getImei(BaseContext.gContext));
            packer.addPart(LOG_KEY, BaseContext.gUsrKey);
            packer.addPart(LOG_NET, getNetworkType());
            packer.addPart(LOG_VENDOR, Build.MANUFACTURER);
            packer.addPart(LOG_OSVER, Build.VERSION.SDK);
        }
        catch (Exception e) {
            JLog.error(this, "can not create MultipartDataPacker");
        }
        return packer;
    }

    private String getNetworkType() {
        String type = "0";
        ConnectivityManager connectManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectManager.getActiveNetworkInfo();

        int activeNetworkType = info.getType();
        if (activeNetworkType == ConnectivityManager.TYPE_WIFI) {
            type = "3";
        } else if (activeNetworkType == ConnectivityManager.TYPE_MOBILE) {
            type = info.getExtraInfo();
            if (type == null || type.length() == 0) {
                type = "2";
            }
        }
        return type;
    }
}
