package com.duowan.jni;

public class JBsDiff {
	
	public static native int applyPatch(String oldapk, String newapk, String patch);
	
	static{
		System.loadLibrary("jbsdiff");
	}
}
