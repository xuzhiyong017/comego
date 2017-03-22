package com.xuzhiyong.comego.module.update;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;

import com.duowan.fw.FwEvent;
import com.duowan.fw.FwEventAnnotation;
import com.duowan.fw.Module;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.kvo.Kvo;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.kvo.KvoBinder;
import com.duowan.fw.root.BaseContext;
import com.duowan.fw.util.JConstant;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JMD5Utils;
import com.duowan.fw.util.JPolling;
import com.duowan.fw.util.JStringUtils;
import com.duowan.fw.util.JTimeUtils;
import com.duowan.fw.util.JVer;
import com.duowan.fw.util.JVersionUtil;

import com.duowan.jni.JBsDiffTool;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.Login.LoginHelper;
import com.xuzhiyong.comego.module.URLHelper;
import com.xuzhiyong.comego.module.analysis.StatsConst;
import com.xuzhiyong.comego.module.analysis.StatsHelper;
import com.xuzhiyong.comego.module.download.DownloadHelper;
import com.xuzhiyong.comego.service.LocalService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.regex.Pattern;

import protocol.ErrCode;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.android.schedulers.HandlerScheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UpdateModule extends Module implements UpdateInterface {
    // FIXME: 16-10-25 
    private static final long AUTO_CHECK_INTERVAL = 12 * 3600 * 1000L;
    private static final int CONNECT_TIMEOUT = 20000;
    private static final int READ_TIMEOUT = 15000;

    private static final String UPDATE_APP_URI = URLHelper.getUpdateUrl();
    private static final String UPDATE_URI = "http://yydl.duowan.com/mobile/gaga/android/gamegoupdate.json";
    private static final String UPDATE_URI_YY = "http://yydl.yy.com/mobile/gaga/android/gamegoupdate.json";
    private static final String UPDATE_URI_QINIU = "http://dwgaga.qiniudn.com/gamegoupdate.json";
    private static final String[] UPDATE_URI_LIST = {UPDATE_APP_URI, UPDATE_URI, UPDATE_URI_YY, UPDATE_URI_QINIU};
    private static final String DEFAULT_FOLDER = "/duowan/gamego/update";
    private static final String DEFAULT_APK_PREFIX = "gamego_"; // TODO gamego_1.0.1_58.apk gamego_version_versioncode.apk
    private static final String DEFAULT_APK_SUFFIX = ".apk";
    private static final String DEFAULT_PATCH_NAME = "patch_"; // patch_1.0.1_1.0.2.patch patch_from_to
    private static final String DEFAULT_PATCH_SUFFIX = ".patch";

    private IState mCurrentState;
    private UpdateData mUpdateData;
    private UpdateData.Patch mPatch;
    private String mFullPkgUrl;
    private String mVersion;

    private UpdateModuleData mData;
    private KvoBinder mBinder = new KvoBinder(this);

    public UpdateModule() {
        mData = new UpdateModuleData();
        DData.updateModuleData.link(this, mData);

        initLocalAppVersion();
        DEvent.autoBindingEvent(this);
        idle(null, null);
    }

    private void setCurrentState(IState state) {
        mCurrentState = state;
    }

    private void initLocalAppVersion() {
        PackageManager pm = BaseContext.gContext.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(BaseContext.gContext.getPackageName(), 0);
            mData.setValue(UpdateModuleData.Kvo_versionCode, pi.versionCode);
            mData.setValue(UpdateModuleData.Kvo_versionName, pi.versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private FwEvent.EventArg buildEvent(UpdateEvent.Event event, Object... objs) {
        return FwEvent.EventArg.buildEventWithArg(this, event, objs);
    }

    private void idle(String eid, String label) {
        setCurrentState(States.state(States.idle));
        mUpdateData = null;
        mPatch = null;
        if (null != eid) {
            stats(eid, label);
        }
    }

    @FwEventAnnotation(event = DEvent.E_AllModuleCreated)
    public void onLocalInited(FwEvent.EventArg event) {
        ThreadBus.bus().postDelayed(ThreadBus.Main, new Runnable() {
            @Override
            public void run() {
                scheduleUpdateTimerTask();
            }
        }, 60000);
    }

    @FwEventAnnotation(event = DEvent.E_LoginFailed)
    public void onLoginFailed(FwEvent.EventArg event) {
        Integer result = event.arg0(Integer.class);
        //版本太旧，已无法登录，执行检查更新
        if (result != null && result == ErrCode.OldVersion_VALUE) {
            // FIXME: 16-10-25
//            GToast.show(R.string.login_failed_oldversion);
//            FloatManager.getInstance().showFloatView(R.string.login_failed_oldversion);
//            updateVersionData(false);
        }
    }

    private void scheduleUpdateTimerTask() {
        long trigger = JPolling.millisOf(20, 30); // 8:30 pm
        long repeat = JTimeUtils.toMillis(24 * 60 * 60);// one day
        Bundle extras = new Bundle();
        extras.putInt(LocalService.LocalService_Op_Key, LocalService.Local_Op_CheckUpdate);
        JPolling.startTriggerService(BaseContext.gContext, trigger, repeat,
                LocalService.class, LocalService.ACTION, extras,
                LocalService.Local_Op_CheckUpdate);
    }



    /// package
    void checkUpdate(final boolean autoCheck) {
        if (autoCheck && (System.currentTimeMillis() - mData.timestamp) < AUTO_CHECK_INTERVAL) {
            return;
        }

        setCurrentState(States.state(States.check));
        mData.timestamp = System.currentTimeMillis();
        UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_CheckUpdate, UpdateEvent.CheckUpdate_Started, autoCheck);

        Observable.create(new OnSubscribe<UpdateData>() {
            @Override
            public void call(Subscriber<? super UpdateData> subscriber) {
                subscriber.onNext(checkUpdateData());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(HandlerScheduler.from(ThreadBus.bus().getHandler(ThreadBus.Working)))
                .subscribe(new Action1<UpdateData>() {
                    @Override
                    public void call(UpdateData updateData) {
                        onGetUpdateData(autoCheck, updateData);
                    }
                });
    }




    void installPackage(String path) {
        if (!TextUtils.isEmpty(path)) {
            String filePath = path;
            if (filePath.startsWith("file://")) {
                filePath = filePath.substring(7);
            }

            if (DownloadHelper.installPackage(gMainContext, filePath)) {
                idle(StatsConst.UPDATE_INSTALL_SUCCESS, "success_open_install_activity");
                return;
            }
        }
        UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_Install, UpdateEvent.Install_Failed);
        idle(StatsConst.UPDATE_INSTALL_FAILED, "failed");
    }

    /// private
    private void onGetUpdateData(boolean autoCheck, UpdateData data) {
        if (null == data) {
            UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_CheckUpdate, UpdateEvent.CheckUpdate_Failed, autoCheck);
            idle(null, null);
            return;
        }

        if (versionVerify(data)) {
            onGetNewerVersion(autoCheck, data);
            stats(StatsConst.UPDATE_CHECK_SUCCESS, null);
        } else {
            UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_CheckUpdate, UpdateEvent.CheckUpdate_Latest, autoCheck);
            idle(StatsConst.UPDATE_CHECK_SUCCESS, StatsConst.LATEST_VERSION);
        }
    }

    private boolean versionVerify(UpdateData data) {
        if (TextUtils.isEmpty(data.version)) {
            return false;
        }
        JVer ver = JVersionUtil.getVerFromStr(data.version);
        if (null != ver && (0 == data.ignore) && ver.bigThan(JVersionUtil.getLocalVer(gMainContext))) {
            if (TextUtils.isEmpty(data.uid_pattern)
                    || Pattern.compile(data.uid_pattern).matcher(String.valueOf(LoginHelper.getUid())).matches()) {
                return true;
            }
        }
        return false;
    }

    private void onGetNewerVersion(boolean autoCheck, UpdateData data) {
        mUpdateData = data;
        mPatch = null;
        mFullPkgUrl = data.url;
        mVersion = data.version;

        String path = DownloadHelper.getDownloadFolder(DEFAULT_FOLDER) + "/" + DEFAULT_APK_PREFIX + data.version + DEFAULT_APK_SUFFIX;
        File file = new File(path);
        if (file.exists() && JMD5Utils.md5(path).equalsIgnoreCase(data.md5)) {
            setCurrentState(States.state(States.install));
            mCurrentState.handleEvent(UpdateModule.this, buildEvent(UpdateEvent.Event.install, path));
            return;
        }

        mPatch = getCorrectPatch(data);
        if (data.force == 0) {
            if (data.ignore_auto_check == 1 && autoCheck) {//no need to show Update Dialog
                return;
            }
            UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_CheckUpdate, UpdateEvent.CheckUpdate_Newer, autoCheck,
                    null == mPatch ? data : mPatch);
        } else {
            // TODO install silently
        }
    }

    private UpdateData.Patch getCorrectPatch(UpdateData data) {
        ApplicationInfo appInfo = gMainContext.getApplicationInfo();
        String localPath = appInfo.sourceDir;
        if (TextUtils.isEmpty(localPath)) {
            String path = DownloadHelper.getDownloadFolder(DEFAULT_FOLDER) + "/" +
                    DEFAULT_APK_PREFIX + JVersionUtil.getLocalVer(gMainContext).toString() + DEFAULT_APK_SUFFIX;
            if (new File(path).exists()) {
                localPath = path;
            }
        }
        if (!TextUtils.isEmpty(localPath) && null != data.patches && data.patches.length > 0) {
            String localMd5 = JMD5Utils.md5(localPath);
            for (UpdateData.PatchInfo patch : data.patches) {
                if (localMd5.equalsIgnoreCase(patch.apk_md5)) {
                    stats(StatsConst.UPDATE_FIND_PATCH, null);
                    return new UpdateData.Patch(patch, localPath);
                }
            }
        }
        return null;
    }

    private UpdateData checkUpdateData() {
        String str = getUpdateData(UPDATE_URI_LIST);
        UpdateData data = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                data = new Gson().fromJson(str, UpdateData.class);
            } catch (JsonSyntaxException e) {
                stats(StatsConst.UPDATE_CHECK_FAILED, StatsConst.JSON_SYNTAX_ERROR);
                e.printStackTrace();
            }
        }
        return data;
    }

    private String getUpdateData(String[] list) {
        String data = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            for (String uri : list) {
                URL url = new URL(uri);
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.connect();
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        InputStream inputStream = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuffer buffer = new StringBuffer();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }
                        data = buffer.toString();
                        if (!TextUtils.isEmpty(data)) {
                            break;
                        }
                    }
                } catch (SocketTimeoutException e) {
                    stats(StatsConst.UPDATE_CHECK_FAILED, StatsConst.CHECK_TIMEOUT + ":" + uri);
                    e.printStackTrace();
                } catch (IOException e) {
                    stats(StatsConst.UPDATE_CHECK_FAILED, StatsConst.IO_EXCEPTION + ":" + uri);
                    e.printStackTrace();
                } finally {
                    if (null != connection) {
                        connection.disconnect();
                        if (null != reader) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return data;
    }


    private void stats(String eid, String label) {
        JLog.warn(this, "UpdateModule stats " + eid + "; " + (null != label ? label : ""));
        StatsHelper.reportTimesEvent(gMainContext, LoginHelper.getUid(), eid, label);
    }

}

