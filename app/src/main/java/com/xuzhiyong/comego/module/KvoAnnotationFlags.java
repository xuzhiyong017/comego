package com.xuzhiyong.comego.module;

public class KvoAnnotationFlags {
	
	public static final int NOT_SAVE_DB = 0x01;
	public static final int DB_PRIMARY_KEY = 0x02;
	/**
	 * 设置选项的key带有后缀
	 */
	public static final int SETTING_KEY_HAS_SUFFIX = 0x04;

	public static boolean isSaveDb(int flag) {
		return (flag & NOT_SAVE_DB) != NOT_SAVE_DB;
	}
	
	public static boolean isDbPrimaryKey(int flag) {
		return (flag & DB_PRIMARY_KEY) == DB_PRIMARY_KEY;
	}

	public static boolean settingKeyHasSuffix(int flag) {
		return (flag & SETTING_KEY_HAS_SUFFIX) == SETTING_KEY_HAS_SUFFIX;
	}
}
