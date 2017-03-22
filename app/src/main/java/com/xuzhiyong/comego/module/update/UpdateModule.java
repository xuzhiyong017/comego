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
import com.mozillaonline.providers.downloads.Downloads;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.Login.LoginHelper;
import com.xuzhiyong.comego.module.URLHelper;
import com.xuzhiyong.comego.module.analysis.StatsConst;
import com.xuzhiyong.comego.module.analysis.StatsHelper;
import com.xuzhiyong.comego.module.download.DownloadHelper;
import com.xuzhiyong.comego.module.download.DownloadInterface;
import com.xuzhiyong.comego.module.download.DownloadSetup;
import com.xuzhiyong.comego.module.update.UpdateInterface;
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
            updateVersionData(false);
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

    /// public
    @Override
    public void updateVersionData(final boolean autoCheck) {
        if (autoCheck && (JConstant.debuggable || JVersionUtil.getLocalNameOrigin(gMainContext).endsWith("SNAPSHOT"))) {
            return;
        }

        ThreadBus.bus().callThreadSafe(ThreadBus.Working, new Runnable() {
            @Override
            public void run() {
                mCurrentState.handleEvent(UpdateModule.this, buildEvent(UpdateEvent.Event.check_update, autoCheck));
            }
        });
    }

    @Override
    public void fullDownload() {
        ThreadBus.bus().callThreadSafe(ThreadBus.Working, new Runnable() {
            @Override
            public void run() {
                mCurrentState.handleEvent(UpdateModule.this, buildEvent(UpdateEvent.Event.full_download, mFullPkgUrl));
            }
        });
    }

    @Override
    public String getPatchNote() {
        return (null != mUpdateData && null != mUpdateData.description) ? mUpdateData.description : "";
    }

    @Override
    public String getVersion() {
        return null == mVersion ? "" : mVersion;
    }

    @Override
    public void downloadPatch() {
        ThreadBus.bus().callThreadSafe(ThreadBus.Working, new Runnable() {
            @Override
            public void run() {
                mCurrentState.handleEvent(UpdateModule.this, buildEvent(UpdateEvent.Event.download));
            }
        });
    }

    @Override
    public void ignore() {
        ThreadBus.bus().callThreadSafe(ThreadBus.Working, new Runnable() {
            @Override
            public void run() {
                idle(StatsConst.UPDATE_USER_CANCEL, null);
            }
        });
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

    void download() {
        setCurrentState(States.state(States.download));

        String fileName = null == mPatch ? DEFAULT_APK_PREFIX + mUpdateData.version + DEFAULT_APK_SUFFIX :
                DEFAULT_PATCH_NAME + mPatch.patchInfo.patch_version + "_" + mUpdateData.version + DEFAULT_PATCH_SUFFIX;
        String uri = null == mPatch ? mUpdateData.url : mPatch.patchInfo.patch_url;
        long requestId = DownloadHelper.addDownloadTask(uri, fileName, DownloadHelper.getDownloadFolder(DEFAULT_FOLDER));
        if (requestId > 0L) {
            onDownloadStarted(requestId);
        } else {
            UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_Download, requestId);
            idle(StatsConst.UPDATE_DOWNLOAD_FAILED, StatsConst.DOWNLOAD_FAILED_CODE + requestId);
        }
    }

    void downloadApk(String url) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(mVersion)) {
            return;
        }

        setCurrentState(States.state(States.download));
        String fileName = DEFAULT_APK_PREFIX + mVersion + DEFAULT_APK_SUFFIX;
        String filePath = DownloadHelper.getDownloadFolder(DEFAULT_FOLDER) + "/" + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        long requestId = DownloadHelper.addDownloadTask(url, fileName, DownloadHelper.getDownloadFolder(DEFAULT_FOLDER));
        if (requestId > 0L) {
            onDownloadStarted(requestId);
        } else {
            UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_Download, requestId);
            idle(StatsConst.UPDATE_DOWNLOAD_FAILED, StatsConst.DOWNLOAD_FAILED_CODE + requestId);
        }

    }

    void applyDownload(DownloadSetup.JDownloadInfo info) {
        if (null == mPatch) {
            setCurrentState(States.state(States.install));
            mCurrentState.handleEvent(this, buildEvent(UpdateEvent.Event.install, info.mHint));
            return;
        }

        JBsDiffTool.BsApllyPatchTask task = new JBsDiffTool.BsApllyPatchTask();
        task.id = (int) info.mId;
        task.newMd5 = (TextUtils.isEmpty(mPatch.patchInfo.target_md5) ? mUpdateData.md5 : mPatch.patchInfo.target_md5);
        task.newPath = DownloadHelper.getDownloadFolder(DEFAULT_FOLDER) + "/" + DEFAULT_APK_PREFIX + mUpdateData.version + DEFAULT_APK_SUFFIX;
        task.oldMd5 = mPatch.patchInfo.apk_md5;
        task.oldPath = mPatch.path;
        task.patchMd5 = mPatch.patchInfo.patch_md5;
        task.patchPath = info.mHint.length() > 7 && info.mHint.startsWith("file://") ? info.mHint.substring(7) :
                JStringUtils.combineStr(DownloadHelper.getDownloadFolder(DEFAULT_FOLDER),
                        "/", DEFAULT_PATCH_NAME, mPatch.patchInfo.patch_version, "_", mUpdateData.version, DEFAULT_PATCH_SUFFIX);

        UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_Apply, UpdateEvent.Apply_Start);
        JBsDiffTool.applyPatch(task, new JBsDiffTool.BsApllyPatchCallback() {
            @Override
            public void onPatchResult(JBsDiffTool.BsApllyPatchTask task, JBsDiffTool.BsErrCode code) {
                switch (code) {
                    case BsErrCode_OK:
                        stats(StatsConst.UPDATE_APPLY_SUCCESS, null);
                        UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_Apply, UpdateEvent.Apply_Success);
                        setCurrentState(States.state(States.install));
                        mCurrentState.handleEvent(UpdateModule.this, buildEvent(UpdateEvent.Event.install, task.newPath));
                        break;
                    default:
                        UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_Apply, UpdateEvent.Apply_Failed);
                        idle(StatsConst.UPDATE_APPLY_FAILED, StatsConst.APPLY_ERROR + code);
                        break;
                }
            }

            @Override
            public void onPatchProgress(JBsDiffTool.BsApllyPatchTask task, JBsDiffTool.BsProgressPhase phase) {

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

    private void onDownloadStarted(long id) {
        mBinder.singleBindSourceTo("update_download", DownloadHelper.getDownloadInfo(id));
    }

    private void onDownloadSuccess(DownloadSetup.JDownloadInfo info) {
        mBinder.clearAllKvoConnections();

        if (null == info) {
            UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_Download, UpdateEvent.DownloadUpdate_Failed);
            idle(StatsConst.UPDATE_DOWNLOAD_FAILED, StatsConst.DOWNLOAD_FAILED_CODE + "null_info");
            return;
        }

        stats(StatsConst.UPDATE_DOWNLOAD_SUCCESS, "");
        setCurrentState(States.state(States.apply));
        mCurrentState.handleEvent(this, buildEvent(UpdateEvent.Event.apply, info));
    }

    private void onDownloadError(DownloadSetup.JDownloadInfo info) {
        mBinder.clearAllKvoConnections();
        UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_Download, UpdateEvent.DownloadUpdate_Failed);
        DownloadHelper.removeDownloadTask(info.mId);
        idle(StatsConst.UPDATE_DOWNLOAD_FAILED, StatsConst.DOWNLOAD_FAILED_CODE + info.mStatus);
    }

    @KvoAnnotation(name = DownloadSetup.JDownloadInfo.Kvo_status, targetClass = DownloadSetup.JDownloadInfo.class, thread = ThreadBus.Working)
    public void onDownloadStateChanged(Kvo.KvoEvent event) {
        int status = event.caseNewValue(Integer.class, 0);
        switch (status) {
            case Downloads.STATUS_PENDING:
                break;
            case Downloads.STATUS_RUNNING:
                UpdateEvent.notifyEvent(this, UpdateEvent.UpdateEvent_Download, UpdateEvent.DownloadUpdate_Started);
                break;
            case Downloads.STATUS_SUCCESS:
            case Downloads.STATUS_FILE_ALREADY_EXISTS_ERROR:
                onDownloadSuccess(DownloadSetup.JDownloadInfo.class.cast(event.from));
                DModule.ModuleDownload.cast(DownloadInterface.class).tryStopSyncList();
                break;
            default:
                if (Downloads.isStatusError(status) || status == Downloads.STATUS_WAITING_TO_RETRY) {
                    onDownloadError(DownloadSetup.JDownloadInfo.class.cast(event.from));
                    DModule.ModuleDownload.cast(DownloadInterface.class).tryStopSyncList();
                }
                break;
        }
    }

    private void stats(String eid, String label) {
        JLog.warn(this, "UpdateModule stats " + eid + "; " + (null != label ? label : ""));
        StatsHelper.reportTimesEvent(gMainContext, LoginHelper.getUid(), eid, label);
    }

}

