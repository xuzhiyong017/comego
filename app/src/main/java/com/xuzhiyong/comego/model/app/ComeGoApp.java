package com.xuzhiyong.comego.model.app;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.duowan.fw.root.BaseApp;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JUtils;
import com.squareup.leakcanary.LeakCanary;
import com.xuzhiyong.comego.model.analysis.UncaughtExceptionHandler;

/**
 * Created by 91299 on 2017/3/12   0012.
 */

public class ComeGoApp extends BaseApp {

    @Override
    public void onCreate() {

        if(JUtils.isRemoteProcess(this)){
            return;
        }

        super.onCreate();

        initApp();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initApp() {
        long initStart = System.currentTimeMillis();
        JLog.info(this, "init app start");

        //check memory leak
        LeakCanary.install(this);

        ComegoStartup.startUp(this);

        do_registerComponentCallbacks();

        JLog.info(this, "init app end time : " + (System.currentTimeMillis() - initStart));


    }

    private void do_registerComponentCallbacks(){
        registerComponentCallbacks(new ComponentCallbacks() {

            @Override
            public void onLowMemory() {
                // clear the memory cache
                // TODO: 2016/3/28  clear memory image cache hear
                JLog.info(this, "onLowMemory: clear the image memory cache");
            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
            }
        });
    }
}
