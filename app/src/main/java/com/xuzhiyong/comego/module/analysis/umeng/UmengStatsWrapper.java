package com.xuzhiyong.comego.module.analysis.umeng;

import android.app.Activity;
import android.content.Context;

import com.duowan.fw.Module;
import com.xuzhiyong.comego.module.analysis.StatsConst;
import com.xuzhiyong.comego.module.analysis.base.ICrashReportInterface;
import com.xuzhiyong.comego.module.analysis.base.IStatsInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class UmengStatsWrapper implements IStatsInterface, ICrashReportInterface {

    private AtomicBoolean mInitialized = new AtomicBoolean(false);

    private static final class Holder {
        private static final UmengStatsWrapper mInstance = new UmengStatsWrapper();
    }

    public static UmengStatsWrapper getInstance() {
        return Holder.mInstance;
    }

    /// from IStatsInterface
    @Override
    public void initialize() {
        if (!mInitialized.getAndSet(true)) {
            String appKey = "5618c271e0f55abf66004d40";
            long sessionInterval = 120000L;

//            AnalyticsConfig.setAppkey(appKey);
//            AnalyticsConfig.setChannel(DConst.sChannelID);
//            MobclickAgent.setSessionContinueMillis(sessionInterval);
//            MobclickAgent.updateOnlineConfig(Module.gMainContext);
//            MobclickAgent.setCatchUncaughtExceptions(GameGoConfig.config.ym_crash);
        }
    }

    @Override
    public void onPause(Activity activity) {
//        MobclickAgent.onPause(activity);
    }

    @Override
    public void onResume(Activity activity) {
//        MobclickAgent.onResume(activity);
    }

    @Override
    public void reportLogin(long uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(StatsConst.UID, String.valueOf(uid));
//        MobclickAgent.onEvent(Module.gMainContext, StatsConst.USER_LOGIN, map);
    }

    @Override
    public void reportTimesEvent(Context context, long uid, String eventId) {
//        MobclickAgent.onEvent(context, eventId);
    }

    @Override
    public void reportTimesEvent(Context context, long uid, String eventId, String label) {
        if (null == label) {
//            MobclickAgent.onEvent(context, eventId);
            return;
        }
//        MobclickAgent.onEvent(context, eventId, label);
    }

    @Override
    public void reportTimesEvent(Context context, long uid, String eventId, String label, Map<String, String> property) {
        if (null == property) {
//            MobclickAgent.onEvent(context, eventId);
            return;
        }
//        MobclickAgent.onEvent(context, eventId, property);
    }

    @Override
    public void reportCountEvent(Context context, long uid, String eventId, double value, Map<String, String> property) {
//        MobclickAgent.onEventValue(context, eventId, property, (int) value);
    }

    @Override
    public void initCrashReport() {
        initialize();
    }

    /// from ILogReportInterface
    @Override
    public void reportCrash(Context context, Throwable t) {
        if (mInitialized.get()) {
//            MobclickAgent.reportError(context, t);
        }
    }

}
