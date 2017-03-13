package com.duowan.fw.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class JThreadUtil {
	// main thread id
	public static long gMainThreadId = Looper.getMainLooper().getThread().getId();
	public static Handler gMainHandler = new Handler(Looper.getMainLooper());
	
	// used to check current thread is what we want
	public static boolean checkThreadSafe(long threadId, String msg, boolean in){
		boolean isInThread = (Thread.currentThread().getId() == threadId);
		if(isInThread != in){
			Log.e("ThreadSafeCheck", msg);
		}
		return isInThread;
	}
	// used to assert the thread is what we want
	public static void assertThreadSafe(long threadId, String msg, boolean in){
		JUtils.jAssert(checkThreadSafe(threadId, msg, in));
	}
	// used to assert the thread is the main thread
	public static void assertInMainThread(String msg){
		assertThreadSafe(gMainThreadId, msg, true);
	}
	// used to assert the thread is not the main thread
	public static void assertNotInMainThread(String msg){
		assertThreadSafe(gMainThreadId, msg, false);
	}
	// is in main thead
	public static boolean isInMainThread(){
		return Looper.getMainLooper() == Looper.myLooper();
	}
	// be sure in main thread
	public static void runMainThread(Runnable r){
		if (isInMainThread()) {
			r.run();
		}else {
			gMainHandler.post(r);
		}
	}
	public static void postMainThread(Runnable r){
		gMainHandler.post(r);
	}
}
