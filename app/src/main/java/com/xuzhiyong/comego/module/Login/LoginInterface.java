package com.xuzhiyong.comego.module.Login;



public interface LoginInterface {

	void changeNetValiable(boolean networkAvailable);

	void login();

	void login(boolean strongNewSession);

    void getUserVerifyCode(String phoneNumber, boolean checkPhoneNumRepeat,
						   LoginModule.NetChannelSendResultInterface tri);
// FIXME: 16-10-25
//	void register(String identifyCode, protocol.UserInfo info, String password,
//                  boolean resetPassword, LoginModule.NetChannelSendResultInterface rri);

	boolean logout();

	boolean needLogoutWhenLoginFailed(int result);

	void setLoginState(LoginModuleData.LoginState state);

    String getDefaultPhoneNumber();

    void savePhoneNumber(String number);

	String getDeviceFingerStr();
}
