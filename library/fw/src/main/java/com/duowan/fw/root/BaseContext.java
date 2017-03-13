package com.duowan.fw.root;

import android.app.Application;

/**
 * Design by JerryZhou@outlook.com, v3.0.0
 * */
public class BaseContext {
	/// the application context
	public static Application gContext = null;
	
	/// start the app, then set company name
	public static String gCompanyName = "duowan";
	/// start the app, then set the app name
	public static String gAppName = "ada";
	/// start the app, then set the signature
	public static String gAppSignature = "1.0.0";
	/// start the app, then set the usr key whenever a user is applyed
	public static String gUsrKey = "0";
	/// start the app, then set the report platform
	public static String gPlatform = "android_ada";
	/// start the app, when logined set the uid
	public static long gUid = 0;
	
}
