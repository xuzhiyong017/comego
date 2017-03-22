package com.xuzhiyong.comego.module;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.duowan.fw.Module;
import com.duowan.fw.util.JLog;

public class DConst {

	public static final long MAC_USER_ID_START = 1000000000L;

    public static final int UDB_APP_ID = 5633;

	/// 网络操作默认超时时间
	public static final int KC_MaxNetOperatorTimeOut = 15000;

	/// 客户端协议本号
	public static final int KC_ClientVersion;

    public static final int KC_VersionCode;

    static {
        int versionCode = 1;
        try {
            PackageInfo pi = Module.gMainContext.getPackageManager().getPackageInfo(Module.gMainContext.getPackageName(), 0);
            versionCode = pi.versionCode;
            JLog.debug("DConst", "getVersionCode:" + versionCode);
        } catch (Exception e) {
            JLog.warn("DConst", "getVersionCode Exception:" + e.toString());
        }
        KC_VersionCode = versionCode;
        KC_ClientVersion = 0x01000000 + versionCode;
    }

    /// 默认的页大小
    public static final int KC_PageCount = 10;

	/**
	 * 默认是官方包
	 */
	public static String sChannelID;
	
	static {
		sChannelID = "official";
		try {
			Object object = Module.gMainContext.getPackageManager().
					getApplicationInfo(Module.gMainContext.getPackageName(),
							PackageManager.GET_META_DATA).metaData.
							get(".*_CHANNEL");
            if (null != object) {
                sChannelID = object.toString();
            } else {
                JLog.error(null, "can't get channel ID;");
            }
		} catch (NameNotFoundException e) {
			JLog.error(null, "can't get channel ID : " + e.toString());
		}
	}
}
