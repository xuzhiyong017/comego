package com.xuzhiyong.comego.module.app;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;

import com.duowan.fw.root.BaseApp;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JUtils;

public class ComegoApp extends BaseApp {

    @Override
    public void onCreate() {
        if(JUtils.isRemoteProcess(this)) {
            return;
        }

        super.onCreate();

	    initApp();
    }

	/**
	 * for multi dex
	 * 使用Application中的静态变量时要注意，在使用静态变量时，类可能还没有加载
	 */
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
// FIXME: 16-10-25
//		MultiDex.install(this);
	}

	private void initApp() {
		long initStart = System.currentTimeMillis();
		JLog.info(this, "init app start");

		//check memory leak
//		LeakCanary.install(this);

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
