package com.duowan.fw.root;

import android.app.Application;

import com.duowan.fw.util.JConfig;
import com.duowan.fw.util.JLog;

/**
 * Design by JerryZhou@outlook.com, v3.0.0
 * */
public class BaseApp extends Application implements Thread.UncaughtExceptionHandler {

	Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		// init the global app
		BaseContext.gContext = this;
		// init the config preference
		JConfig.start();

		// exceptions
		mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
    }

	// take the exception
	public void uncaughtException(Thread thread, Throwable ex) {

		// call self
		onCrash(thread, ex);

		// next default handler
		if (mDefaultExceptionHandler != null) {
			mDefaultExceptionHandler.uncaughtException(thread, ex);
		}
	}

	// The Sub Class Override
	public void onCrash(Thread thread, Throwable ex) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JLog.writeAllBufferToFile();
			}
		}).start();
	}
}
