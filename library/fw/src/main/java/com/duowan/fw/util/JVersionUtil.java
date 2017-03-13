package com.duowan.fw.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class JVersionUtil {
	static int sLocalVer[] = null;
	static String sLocalName = null;
	static String sLocalNameOrigin = null;
	
	private static final String DOT = ".";
	public static JVer getVerFromStr(String version) {
	    if (version.matches("\\d{1,}.\\d{1,}.\\d{1,}")) {
	        JVer ver = new JVer();
	        int dotPos = version.indexOf(DOT);
	        int prevPos = 0;
	        ver.mMajor = Integer.valueOf(version.substring(prevPos, dotPos));
	        prevPos = dotPos + 1;
	        dotPos = version.indexOf(DOT, prevPos);
	        ver.mMinor = Integer.valueOf(version.substring(prevPos, dotPos));
	        prevPos = dotPos + 1;
	        ver.mBuild = Integer.valueOf(version.substring(prevPos));
	        return ver;
	    }
	    return null;
	}
	
	public static JVer getLocalVer(Context c) {
        JVer v = new JVer();
        int ver[] = JVersionUtil.getLocal(c);
        v.mMajor = ver[0];
        v.mMinor = ver[1];
        v.mBuild = ver[2];
        return v;
    }

	public static String getLocalName(Context c){
		if( sLocalName != null ){
			return sLocalName;
		}
		
		loadLoaclVer(c);
		
		return sLocalName;
	}
	
	public static String getLocalNameOrigin(Context c){
		if (sLocalNameOrigin != null) {
			return sLocalNameOrigin;
		}
		
		loadLoaclVer(c);
		
		return sLocalNameOrigin;
	}
	
	public static int[] getLocal(Context c){
		if( sLocalVer != null ){
			return sLocalVer;
		}
		
		loadLoaclVer(c);

		return sLocalVer;
	}
	
	static void loadLoaclVer(Context c){
		
		try {
			sLocalName = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName;
			sLocalNameOrigin = sLocalName; 
		} catch (NameNotFoundException e) {
			throw new RuntimeException("Local Ver Package Error");
		}
		
		if( sLocalName == null ){
			throw new RuntimeException("Local Ver VersionName Not Exist");
		}
		
		// handle maven standard version like this "1.0.0-SNAPSHOT";
		int pos = sLocalName.indexOf('-');
		if (pos != -1) {
			sLocalName = sLocalName.substring(0, pos);
		}
		String verStr[] = sLocalName.split("\\.");
		
		if( verStr.length != 3 ){
			throw new RuntimeException("Local Ver VersionName Error");
		}
		
		sLocalVer = new int[3];
		
		try{
			for( int i = 0; i < 3; i++ ){
				sLocalVer[i] = Integer.parseInt(verStr[i]);
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("Local Ver VersionName Error");
		}
	}
}
