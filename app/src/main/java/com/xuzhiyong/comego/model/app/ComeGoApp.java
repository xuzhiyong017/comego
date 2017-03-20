package com.xuzhiyong.comego.model.app;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by 91299 on 2017/3/12   0012.
 */

public class ComeGoApp extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        initApp();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void initApp() {
        long initStart = System.currentTimeMillis();

        do_registerComponentCallbacks();

    }

    private void do_registerComponentCallbacks(){
        registerComponentCallbacks(new ComponentCallbacks() {

            @Override
            public void onLowMemory() {
                // clear the memory cache
                // TODO: 2016/3/28  clear memory image cache hear
            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
            }
        });
    }
}
