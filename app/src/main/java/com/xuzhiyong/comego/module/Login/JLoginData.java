package com.xuzhiyong.comego.module.Login;

import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.util.JStringUtils;

import protocol.Result;

/**
 * Created by hydra on 2016/1/5.
 *
 */
public class JLoginData extends KvoSource {

    public static final String Kvo_uid = "uid";
    @KvoAnnotation(name=Kvo_uid)
    public long uid = 0L;

	public static final String Kvo_qqOpenId = "qqOpenId";
	@KvoAnnotation(name=Kvo_qqOpenId)
	public String qqOpenId = JStringUtils.EMPTY_STR;

	public static final String Kvo_qqAccessToken = "qqAccessToken";
	@KvoAnnotation(name=Kvo_qqAccessToken)
	public String qqAccessToken = JStringUtils.EMPTY_STR;

	public static final String Kvo_loginBy = "loginBy";
	@KvoAnnotation(name=Kvo_loginBy)
	public LoginBy loginBy = LoginBy.LoginBy_Mac;

    public static final String Kvo_userName = "userName";
    @KvoAnnotation(name=Kvo_userName)
    public String userName = JStringUtils.EMPTY_STR;

    public static final String Kvo_cookie = "cookie";
    @KvoAnnotation(name=Kvo_cookie)
    public String cookie = JStringUtils.EMPTY_STR;

    public static final String Kvo_loginStartTime = "loginStartTime";
    @KvoAnnotation(name=Kvo_loginStartTime)
    public long loginStartTime = 0L;

	public static final String Kvo_mobilePhone = "mobilePhone";
	@KvoAnnotation(name=Kvo_mobilePhone)
	public String mobilePhone = JStringUtils.EMPTY_STR;

	public static final String Kvo_password = "password";
	@KvoAnnotation(name=Kvo_password)
	public String password = JStringUtils.EMPTY_STR;

    public static final String Kvo_loginResult = "loginResult";
    @KvoAnnotation(name = Kvo_loginResult)
    public Result loginResult = null;

	// TODO: 2016/3/30 add weixin and weibo access token field

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof JLoginData)) {
            return false;
        }

        JLoginData another = (JLoginData) o;

        //这个判断，其实是不太严谨的，不过够用了
        return loginBy == another.loginBy && loginStartTime == another.loginStartTime
                && uid == another.uid && JStringUtils.equal(cookie, another.cookie);
    }

    @Override
    public String toString() {
        return JStringUtils.combineStr("uid = ", uid, " loginBy = ", loginBy, " username = ", userName);
    }

    public void setResult(boolean success, String reason) {
        setValue(Kvo_loginResult, Result.newBuilder().success(success).reason(reason).build());
    }

    public static JLoginData emptyLoginData() {
        return new JLoginData();
    }

    public static JLoginData qqLoginData() {
        JLoginData data = new JLoginData();

        data.setValue(Kvo_loginBy, LoginBy.LoginBy_QQ);
	    data.loginStartTime = System.currentTimeMillis();

        return data;
    }

    public static JLoginData macLoginData() {
        JLoginData data = new JLoginData();

        data.setValue(Kvo_loginBy, LoginBy.LoginBy_Mac);
	    data.loginStartTime = System.currentTimeMillis();

        return data;
    }

    public static JLoginData cookieLoginData(long uid, String cooike) {
        JLoginData data = new JLoginData();

	    data.setValue(Kvo_loginBy, LoginBy.LoginBy_Cookie);
        data.setValue(Kvo_uid, uid);
        data.setValue(Kvo_cookie, cooike);
	    data.loginStartTime = System.currentTimeMillis();

        return data;
    }

	public static JLoginData weixinLoginData() {
		JLoginData data = new JLoginData();

		data.setValue(Kvo_loginBy, LoginBy.LoginBy_WeiXin);
		data.loginStartTime = System.currentTimeMillis();

		return data;
	}

	public static JLoginData weiboLoginData() {
		JLoginData data = new JLoginData();

		data.setValue(Kvo_loginBy, LoginBy.LoginBy_Sina);
		data.loginStartTime = System.currentTimeMillis();

		return data;
	}

	public static JLoginData mobilePhoneLoginData(String mobilePhone, String password) {
		JLoginData data = new JLoginData();

		data.setValue(Kvo_loginBy, LoginBy.LoginBy_MobilePhone);
		data.setValue(Kvo_mobilePhone, mobilePhone);
		data.setValue(Kvo_password, password);
		data.loginStartTime = System.currentTimeMillis();

		return data;
	}
}
