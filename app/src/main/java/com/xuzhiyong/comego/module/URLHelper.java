package com.xuzhiyong.comego.module;

import com.duowan.fw.util.JConfig;
import com.duowan.fw.util.JStringUtils;

/**
 * Created by hydra on 2015/10/14.
 *
 */
public class URLHelper {

	public static final String URL_HOST = "http://win.yy.com/";
    public static final String URL_HOST_TEST = "http://test.win.yy.com/";
    public static final String URL_HOST_HTTPS = "https://win.yy.com/";
    public static final String URL_HOST_HTTPS_TEST = "https://test.win.yy.com/";

    public static final String URL_HOST_TEST_SHARE = "http://test.win.yy.com/";

	public static final String CONFIG = "app/config.json";
    public static final String ABOUT = "app/aboutus.html";
    public static final String LICENSE = "app/agreement.html";
    public static final String SOCIETY_RULES = "app/rule.html";
    public static final String FEEDBACK = "app/feedback.html";
    public static final String HELP = "app/help.html";
    public static final String HELP_PERMISSION = "app/#/help1";
    public static final String WITHDRAW_CASH_RULES = "app/settlement.html";
    public static final String WITHDRAW_CASH_LICENCE = "app/cash.html";
    public static final String LEVEL = "app/lv.html?uid=%d&lv=%d&current=%d&next=%d";
    public static final String SHARE = "act/share";
    public static final String UPDATE = "app/adaupdate.json";

	public static String getUrl() {
//		final String address = JConfig.getString(NetClient.sAddressKey, NetClient.sAddressStr);
//		//正式服务器
//		if (address.equals(NetClient.sAddressStr)) {
//			return URL_HOST;
//		}

		//测试服务器 固定端口号
        return URL_HOST_TEST;
	}

    public static String getUrlHttps() {
//        final String address = JConfig.getString(NetClient.sAddressKey, NetClient.sAddressStr);
//        //正式服务器
//        if (address.equals(NetClient.sAddressStr)) {
//            return URL_HOST_HTTPS;
//        }

        //测试服务器 固定端口号
        return URL_HOST_HTTPS_TEST;
    }

    public static String getUrl(String suffix) {
        return JStringUtils.combineStr(getUrl(), suffix);
    }

    public static String getUrlHttps(String suffix) {
        return JStringUtils.combineStr(getUrlHttps(), suffix);
    }

    public static String getShareUrl(String suffix){
        //测试服务器 固定端口号
        return JStringUtils.combineStr(URL_HOST_TEST_SHARE, suffix);
    }

    public static String getShareUrl(long uid,long gid){
        String url = getShareUrl(URLHelper.SHARE+"?uid="+uid+"&gid="+gid+"&t="+System.currentTimeMillis());
        return url;
    }

    public static String getPreShareUrl(long uid,long gid){
        return getShareUrl(uid,gid)+"&pre=1";
    }

    public static String getLevelUrl(long uid, int level, long expCurrent, long expNext) {
        return String.format(JStringUtils.combineStr(URL_HOST_TEST, LEVEL), uid, level, expCurrent, expNext);
    }

    public static String getUpdateUrl() {
        return getUrl(UPDATE);
    }
}
