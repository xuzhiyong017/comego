package com.xuzhiyong.comego.model.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.duowan.ada.module.AdaConfig;
import com.duowan.ada.module.DData;
import com.duowan.ada.module.DEvent;
import com.duowan.ada.module.analysis.StatsHelper;
import com.duowan.fw.FwEvent;
import com.duowan.fw.FwEvent.EventArg;
import com.duowan.fw.FwEventAnnotation;
import com.duowan.fw.Module;
import com.duowan.fw.ModuleCenter;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.root.BaseContext;
import com.duowan.fw.util.JANR;
import com.duowan.fw.util.JUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observable;

/**
 * Created by jerryzhou on 15/11/2.
 *
 */
public class AppModule extends Module implements AppInterface {

    // data members
    private WeakReference<Activity> mCurrentActivity;
    private BroadcastReceiver mAppStateReceiver;
    private AppModuleData mData;
    private AppStartupManager mStartupManager;

    // constructor
    public AppModule(String name, String company, String platform) {
        // init the basic information
        BaseContext.gAppName = name;
        BaseContext.gCompanyName = company;
        BaseContext.gPlatform = platform;

        // basic data
        mData = new AppModuleData();
        DData.appData.link(this, mData);
        mData.appOnForeground = true;

        // event
        FwEvent.autoBindingEvent(this);

        // register
        registerAppStateChange();

        // basic
        JUtils.openStrictMode(BaseContext.gContext);
// FIXME: 16-10-25
        // notification center
//        NotificationCenter.init();

        initFresco();

        mStartupManager = new AppStartupManager();

        // delay init
        ThreadBus.bus().postDelayed(ThreadBus.Main, new Runnable() {
            @Override
            public void run() {
                // server config
                AdaConfig.syncConfigFromServer();
            }
        }, 5000);
    }

    private void initFresco() {
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(gMainContext, new OkHttpClient())
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(gMainContext, config);
    }

    ///////////////////////////////////////////////
    // implements from AdInterface Begin
    ///////////////////////////////////////////////

    @Override
    public void startup(Activity activity) {
        mStartupManager.startup(activity);
    }

    @Override
    public Observable<Class<? extends Activity>> splashing(Activity activity) {
        return mStartupManager.splashing(activity);
    }

    @Override
    public void onMainActivityCreate(Activity activity) {
        mStartupManager.onMainActivityCreate(activity);
    }

    @Override
    public void startupDone(Activity activity) {
        mStartupManager.startupDone(activity);
    }

    @Override
    public void onLogout() {
        mStartupManager.onLogout();
    }

    @Override
    public synchronized  Activity getCurrentActivity() {
        if (mCurrentActivity != null) {
            return mCurrentActivity.get();
        }
        return null;
    }

    @Override
    public boolean isAppOnForeground() {
        return mData.appOnForeground;
    }

    ///////////////////////////////////////////////
    // implements Event Begin
    ///////////////////////////////////////////////

    @FwEventAnnotation(event = DEvent.E_ActivityFocusChanged)
    public void onFocusChanged(EventArg arg) {
        updateAppOnForeground();
    }

    @FwEventAnnotation(event = DEvent.E_ActivityStateChanged)
    public void onActivityChanged(EventArg arg) {
        Activity activity = arg.arg0(Activity.class);
        AppModuleData.ActivityState state = arg.arg1(AppModuleData.ActivityState.class);

        if (state == AppModuleData.ActivityState.ActivityStateOnResume) {
            setCurrentActivity(activity);
        } else {
            if (activity == getCurrentActivity()) {
                setCurrentActivity(null);
            }
        }

        switch (state) {
            case ActivityStateOnCreate:
                break;
            case ActivityStateOnResume:
                StatsHelper.onResume(activity);
                break;
            case ActivityStateOnPause:
                StatsHelper.onPause(activity);
            case ActivityStateOnStop:
            case ActivityStateOnDestroy:
                break;
        }

        updateAppOnForeground();
    }

    @FwEventAnnotation(event=DEvent.E_NetPinSlow, thread = ThreadBus.Main)
    public void onNetPinSlow(EventArg event) {

    }

    ///////////////////////////////////////////////
    // implements Private
    ///////////////////////////////////////////////

    private void registerAppStateChange() {
        mAppStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateAppOnForeground();
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);

        Module.gMainContext.registerReceiver(mAppStateReceiver, filter);
    }

    private Runnable mAppStateChangeRunnable = new Runnable() {

        @Override
        public void run() {
            boolean cur = currentAppOnForeground();
            if (cur != mData.appOnForeground) {
                // do change event
                mData.setValue(AppModuleData.Kvo_appOnForeground, cur);
                if (cur) {
                    ModuleCenter.sendEventTo(DEvent.E_App_EnterForeground);

                    JANR.startANRCheck(1000);
                } else {
                    ModuleCenter.sendEventTo(DEvent.E_App_EnterBackground);

                    JANR.stopANRCheck();
                }
            }
        }
    };

    private void updateAppOnForeground(){
        //要延迟500毫秒，因为updateAppOnForeground是在activity的pause和resume时
        //这时去判断isAppOnForeground，系统的状态很有可能还未更新，此时的状态就有问题
        ThreadBus.bus().postDelayed(ThreadBus.Working, mAppStateChangeRunnable, 1000);
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    private static boolean currentAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) BaseContext.gContext.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = BaseContext.gContext.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    private synchronized void setCurrentActivity(Activity activity) {
        mCurrentActivity = new WeakReference<>(activity);
    }
}
