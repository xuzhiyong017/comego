package com.xuzhiyong.comego.ui.utils;

import android.annotation.SuppressLint;

import java.util.HashMap;

/**
 * 在GFragmentActivity和GActivity中都覆盖了onActivityResult并设置为final
 * 因为在FragmentActivity中覆盖onActivityResult时，第一句一定要调用super.onActivityResult
 * 避免覆盖里面子Fragment的onActivityResult
 * 
 * 还有一定要判断resultCode != Activity.RESULT_CANCEL
 * 因为activity中点击返回键进行finish操作的时候，默认的resultCode就是Activity.RESULT_CANCEL
 * 
 * @author yujian
 *
 */
public enum ActivityResultCode {
	
	/**
	 * {@link com.duowan.more.ui.image.ImageSelectActivity}
	 */
	RESULT_COMPLETE,

	RESULT_VIDEO_TAKE;
	
	@SuppressLint("UseSparseArrays") 
	private static HashMap<Integer, ActivityResultCode> sResultCodeMap 
				= new HashMap<Integer, ActivityResultCode>();
	
	static {
		for(int i = 0; i < values().length; i++) {
			sResultCodeMap.put(values()[i].intValue(), values()[i]);
		}
	}
	
	public static ActivityResultCode valueOf(int requestCode) {
		return sResultCodeMap.get(requestCode);
	}
	
	public int intValue() {
		return ordinal() + 20000;
	}
}
