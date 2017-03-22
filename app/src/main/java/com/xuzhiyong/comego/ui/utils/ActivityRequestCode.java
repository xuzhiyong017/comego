package com.xuzhiyong.comego.ui.utils;

import android.annotation.SuppressLint;

import java.util.HashMap;

/**
 * 集中定义RequestCode和ResultCode，避免在各个activity中相互跳转的时候
 * 分开定义可能会出现不同的requestCode相等，会出bug
 * 这样保证每个requestCode都不一样，并且避免和系统的相等
 * @author yujian
 *
 */
public enum ActivityRequestCode {

	/**
	 * {@link PictureSelectorDialog}
	 */
	PS_REQUEST_GALLERY,
	PS_REQUEST_TAKEPHOTO;

	@SuppressLint("UseSparseArrays") 
	private static HashMap<Integer, ActivityRequestCode> sRequestCodeMap 
				= new HashMap<Integer, ActivityRequestCode>();
	
	static {
		for(int i = 0; i < values().length; i++) {
			sRequestCodeMap.put(values()[i].intValue(), values()[i]);
		}
	}
	
	public static ActivityRequestCode valueOf(int requestCode) {
		return sRequestCodeMap.get(requestCode);
	}
	
	public int intValue() {
		return 10000 + ordinal();
	}
}
