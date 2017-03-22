package com.xuzhiyong.comego.module.analysis.duowan;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.duowan.fw.Module;
import com.xuzhiyong.comego.module.DConst;
import com.xuzhiyong.comego.module.analysis.base.IStatsInterface;

import java.util.Map;

public class HiidoWrapper implements IStatsInterface {
// FIXME: 16-10-25 
//    protected static final HiidoSDK.PageActionReportOption REPORT = HiidoSDK.PageActionReportOption.REPORT_ON_FUTURE_RESUME;

    private static final class Holder {
        private static final HiidoWrapper mInstance = new HiidoWrapper();
    }

    public static HiidoWrapper getInstance() {
        return Holder.mInstance;
    }

    @Override
    public void initialize() {
        hiidoInitialization(Module.gMainContext);
    }

    @Override
    public void onPause(Activity activity) {
        try {
//            if (!(activity instanceof LiveActivity)) {
//                HiidoSDK.instance().onPause(activity, REPORT);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume(Activity activity) {
        try {
//            if (!(activity instanceof LiveActivity)) {
//                HiidoSDK.instance().onResume(BaseContext.gUid, activity);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reportLogin(long uid) {
//        HiidoSDK.instance().reportLogin(uid);
    }

    @Override
    public void reportTimesEvent(Context context, long uid, String eventId) {
//        HiidoSDK.instance().reportTimesEvent(uid, eventId);
    }

    @Override
    public void reportTimesEvent(Context context, long uid, String eventId, String label) {
//        HiidoSDK.instance().reportTimesEvent(uid, eventId, label);
    }

    @Override
    public void reportTimesEvent(Context context, long uid, String eventId, String label, Map<String, String> property) {
//        Property p = new Property();
//        if (!JFP.empty(property)) {
//            for (Map.Entry<String, String> entry : property.entrySet()) {
//                p.putString(entry.getKey(), entry.getValue());
//            }
//        }
//        HiidoSDK.instance().reportTimesEvent(uid, eventId, label, p);
    }

    @Override
    public void reportCountEvent(Context context, long uid, String eventId, double value, Map<String, String> property) {
//        Property p = new Property();
//        if (!JFP.empty(property)) {
//            for (Map.Entry<String, String> entry : property.entrySet()) {
//                p.putString(entry.getKey(), entry.getValue());
//            }
//        }
//        HiidoSDK.instance().reportCountEvent(uid, eventId, value, "", p);
    }

    // Part 1 : Initialization & MUSTS : call setContext to initialize.
    private void hiidoInitialization(Application c) {
        //appkey必须与后台申请的appkey保持一致，必选
        String appKey = "41263488fba2c67ddb25f0fe48bae3f7";
        //appId不设置默认获取包名，可选
        String appId = "5707";
        //from为发布的渠道，必选
        String from = DConst.sChannelID;

        //设置uid,默认为0，需客户端自己维护uid，如用户登录后把uid赋值。必选
//        OnStatisListener listener = new OnStatisListener() {
//            @Override
//            public long getCurrentUid() {
//                return BaseContext.gUid;
//            }
//        };
//        HiidoSDK.Options options = new HiidoSDK.Options();

        //设置发送机制为立即发送，默认为累计或定时发送,可选
        //options.behaviorSendThreshold = 0;
        //设置sesseion超时时间，默认为30秒，可选
        //options.backgroundDurationMillisAsQuit = 1000*30;
        //设置不开启自动崩溃上报,默认为开启
        //options.isOpenCrashMonitor = false;

        //！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        //设置测试服务器地址,方便客户端集成测试，可在http://log.hiido.net/查看上报的日志。
        //正式发布时，请把该参数注释掉
        //options.testServer = "http://tylog.hiido.com/c.gif";
        //！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！


        //设置开发者自己的崩溃处理
        //MyUncaughtExceptionHandler myUncaughtExceptionHandler = new MyUncaughtExceptionHandler(this);
        //Thread.setDefaultUncaughtExceptionHandler(myUncaughtExceptionHandler);

        //设置参数并初始化sdk，必选
//        HiidoSDK.instance().setOptions(options);
//        HiidoSDK.instance().appStartLaunchWithAppKey(c , appKey, appId, from,listener);
    }

}
