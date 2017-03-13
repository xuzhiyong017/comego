package com.duowan.jni;

/**
 * Design by JerryZhou@outlook.com, v3.0.0
 * */
public class JEnv {
	/// get the evniroment string
	public static native String getEnvironment();
	/// get the crypt seed string
	public static native String getCryptSeed();
	/// get the process raw stat string, only use for user process when not root
	public static native String getProcessRawStatString(long pid);
	/// get the process start time since from the epoch (time_t) (second)
	public static native long getProcessStartTimeSinceEpoch(long pid);
	
	/// setup the the jni environment
	public static native boolean setupJni(String apkDir, String params, Object assetManager);
	
	/// take screen shoot from native 
	public static class ScreenShoot{
		  public int xres;
		  public int yres;
		  public int bps;
		  public int gray;
		  public byte[] bytes;
	}
	/// TODO: fix , do not use, there is a dump
	public static native boolean taskScreenShoot(ScreenShoot screenShoot);
	
	/// load the jni lib
	static{
		System.loadLibrary("jenv");
	}
}
