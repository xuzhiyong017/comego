package com.duowan.fw.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.duowan.fw.root.BaseContext;
import com.duowan.jni.JEnv;

/**
 * Design by JerryZhou@outlook.com, v3.0.0
 * All config save by JConfig will be encrypted.
 * The encrypt Key is returned from the jni code.
 * */
public class JConfig {
	/// name
	private final static String CONFIG_PREFERENCE_NAME = "com.duowan.ada.config";
	
	/// data members
	private static String seed = "com.duowan.ada.jconfig.crypt";
	private static SharedPreferences sPrefs;
	
	/// init function
	public static void start(){
		// global preferences
		sPrefs = BaseContext.gContext.getSharedPreferences(CONFIG_PREFERENCE_NAME ,
                Context.MODE_PRIVATE);
		// get the crypt seed
		seed = JEnv.getCryptSeed();
	}
	
	/// put the string to pref, will encrypt
	public static void putString(final String key, String value){
		try {
			sPrefs.edit().putString(key, JCrypt.encrypt(seed, value)).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/// get the string from the pref, will decrypt
	public static String getString(String key, String defValue){
		String v = sPrefs.getString(key, null);
		if (v != null) {
			try {
				return JCrypt.decrypt(seed, v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defValue;
	}
	
	public static void remove(String key){
		sPrefs.edit().remove(key).commit();
	}
}
