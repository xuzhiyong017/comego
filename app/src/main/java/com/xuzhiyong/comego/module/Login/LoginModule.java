package com.xuzhiyong.comego.module.Login;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.duowan.fw.FwEvent;
import com.duowan.fw.FwEventAnnotation;
import com.duowan.fw.Module;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.root.BaseContext;
import com.duowan.fw.util.JConfig;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JNetworkUtil;
import com.duowan.fw.util.JStringUtils;
import com.duowan.fw.util.JUtils;

import com.google.gson.Gson;
import com.xuzhiyong.comego.module.AdaConfig;
import com.xuzhiyong.comego.module.DConst;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.Ln;
import com.xuzhiyong.comego.module.app.AppInterface;
import com.xuzhiyong.comego.module.datacenter.tables.JLoginHistoryItem;
import com.xuzhiyong.comego.module.Login.LoginModuleData.LoginState;
import com.xuzhiyong.comego.module.net.NetHelper;
import com.xuzhiyong.comego.module.net.NetInterface;
import com.xuzhiyong.comego.module.net.NetModule;
import com.xuzhiyong.comego.module.net.Proto;

import java.io.IOException;

import protocol.ErrCode;
import protocol.LoginBy;
import protocol.PType;
import protocol.SPLogin;
import protocol.UserLoginReq;


public class LoginModule extends Module implements LoginInterface {

	public interface NetChannelSendResultInterface {
		void onNetError(int error, IOException exception);

		void onResult();
	}

	private LoginModuleData mData;
	private JLog.JLogModule mLog;

	private int mAppSessionLoginSeq = 0;

	private final long DEFAULT_RELOGIN_INTERVAL = 2000L;

	private long mReloginInterval = DEFAULT_RELOGIN_INTERVAL;

	public LoginModule() {
		mData = new LoginModuleData();
		DData.loginModuleData.link(this, mData);
		mLog = NetModule.KNet; //JLog.KDefault;
		LoginTask.sLog = NetModule.KNet;

		DEvent.autoBindingEvent(this);

		initLoginHistory();
	}

	/**
	 * 从数据库取出上次登录的用户id和cookies赋值到当前的JLoginData中
	 */
	private void initLoginHistory() {
		JLoginHistoryItem lastLogin = JLoginHistoryItem.getLastLoginItem();
		if (lastLogin != null) {
			if (lastLogin.state == JLoginHistoryItem.State_autologin
					&& lastLogin.cookie != null) {
				mData.setValue(LoginModuleData.Kvo_currentLoginData,
						JLoginData.cookieLoginData(lastLogin.uid, lastLogin.cookie));
			}
		}
	}

	private void userLogin(boolean strongNewSession) {
		// mac address
		mData.justMakeSafeAddress();

		switch (mData.currentLoginData.loginBy) {
			case LoginBy_Cookie:
				userLoginTypeCookie(mData.currentLoginData, strongNewSession);
				break;
			case LoginBy_Mac:
//				userLoginTypeMac(mData.currentLoginData, strongNewSession);
				break;
			case LoginBy_QQ:
            case LoginBy_WeiXin:
				userLoginTypeTencent(mData.currentLoginData, strongNewSession);
				break;
			case LoginBy_Sina:
				// TODO: 2016/3/30
				break;
			case LoginBy_MobilePhone:
                userLoginTypeMobilePhone(mData.currentLoginData, strongNewSession);
				break;
		}
	}

	private void userLoginTypeCookie(JLoginData loginData, boolean strongNewSession) {
		if (TextUtils.isEmpty(loginData.cookie) || loginData.uid == 0L) {
			//may be happen ?
			sendEvent(DEvent.E_LoginError);
			return;
		}

		JLoginHistoryItem loginItem = JLoginHistoryItem.info(loginData.uid);

//		UserLoginReq.Builder loginRequesBuilder = UserLoginReq.newBuilder();
//
//		loginRequesBuilder.loginBy(loginData.loginBy)
//				.uid(loginData.uid)
//				.macid(mData.macAddress)
//				.finger(getDeviceFingerStr())
//				.osinfo(VERSION.CODENAME + ":" + VERSION.SDK_INT)
//				.devtype("android:" + mData.device)
//				.protoVersion(protocol.ProtoVersion.ProtoVersion_Current)
//				.cookie(loginData.cookie)
//				.pushSerialnum(loginItem.pushseq);
//
//		if (DConst.sChannelID != null) {
//			loginRequesBuilder.fromStore(DConst.sChannelID);
//		}
//
//		loginRequesBuilder.newsession(strongNewSession);
//
//		UserLoginReq loginRequest = loginRequesBuilder.build();
//		Proto proto = NetHelper.buildProto(PType.PLogin,
//				SPLogin.PUserLoginReq,
//				NetHelper.pbb().userLoginReq(loginRequest).build());
//
//		mData.setValue(LoginModuleData.Kvo_loginState, LoginModuleData.LoginState.Login_Ing);
//
//		LoginTask.startNewLoginTask(proto);
	}

    private void userLoginTypeMobilePhone(JLoginData loginData, boolean strongNewSession) {
        if (TextUtils.isEmpty(loginData.mobilePhone) || TextUtils.isEmpty(loginData.password)) {
            //may be happen ?
            sendEvent(DEvent.E_LoginError);
            return;
        }

//        UserLoginReq.Builder loginRequestBuilder = UserLoginReq.newBuilder();
//
//        loginRequestBuilder.loginBy(loginData.loginBy)
//                .mobilephone(loginData.mobilePhone)
//                .password(loginData.password)
//                .macid(mData.macAddress)
//                .finger(getDeviceFingerStr())
//                .osinfo(VERSION.CODENAME + ":" + VERSION.SDK_INT)
//                .devtype("android:" + mData.device)
//                .protoVersion(protocol.ProtoVersion.ProtoVersion_Current);
//
//        if (DConst.sChannelID != null) {
//            loginRequestBuilder.fromStore(DConst.sChannelID);
//        }
//
//        loginRequestBuilder.newsession(strongNewSession);
//
//        UserLoginReq loginRequest = loginRequestBuilder.build();
//        Proto proto = NetHelper.buildProto(PType.PLogin,
//                SPLogin.PUserLoginReq,
//                NetHelper.pbb().userLoginReq(loginRequest).build());
//
//        mData.setValue(LoginModuleData.Kvo_loginState, LoginModuleData.LoginState.Login_Ing);
//
//        LoginTask.startNewLoginTask(proto);
    }

	private void userLoginTypeMac(JLoginData loginData, boolean strongNewSession) {
		if (TextUtils.isEmpty(mData.macAddress)) {
			//may be happen ?
			sendEvent(DEvent.E_LoginError);
			return;
		}

//		UserLoginReq.Builder loginRequesBuilder = UserLoginReq.newBuilder();
//		loginRequesBuilder.loginBy(loginData.loginBy)
//				.macid(mData.macAddress)
//				.finger(getDeviceFingerStr())
//				.osinfo(VERSION.CODENAME + ":" + VERSION.SDK_INT)
//				.devtype("android:" + mData.device)
//				.protoVersion(protocol.ProtoVersion.ProtoVersion_Current);
//
//		// channel id: from
//		if (DConst.sChannelID != null) {
//			loginRequesBuilder.fromStore(DConst.sChannelID);
//		}
//
//		loginRequesBuilder.newsession(strongNewSession);
//
//		UserLoginReq loginRequest = loginRequesBuilder.build();
//		Proto proto = NetHelper.buildProto(PType.PLogin,
//				SPLogin.PUserLoginReq,
//				NetHelper.pbb().userLoginReq(loginRequest).build());
//
//		mData.setValue(LoginModuleData.Kvo_loginState, LoginModuleData.LoginState.Login_Ing);
//
//		LoginTask.startNewLoginTask(proto);
	}

	private void userLoginTypeTencent(JLoginData loginData, boolean strongNewSession) {
		if (TextUtils.isEmpty(loginData.qqOpenId)
				|| TextUtils.isEmpty(loginData.qqAccessToken)) {
			//may be happen ?
			sendEvent(DEvent.E_LoginError);
			return;
		}

		UserLoginReq.Builder loginRequesBuilder = UserLoginReq.newBuilder();

		loginRequesBuilder.loginBy(loginData.loginBy)
				.macid(mData.macAddress)
				.finger(getDeviceFingerStr())
				.osinfo(VERSION.CODENAME + ":" + VERSION.SDK_INT)
				.devtype("android:" + mData.device)
				.protoVersion(protocol.ProtoVersion.ProtoVersion_Current)
				.identifyingCode(JStringUtils.combineStr(loginData.qqOpenId,
						"#", loginData.qqAccessToken)).build();

		// channel id: from
		if (DConst.sChannelID != null) {
			loginRequesBuilder.fromStore(DConst.sChannelID);
		}

		loginRequesBuilder.newsession(strongNewSession);

		UserLoginReq loginRequest = loginRequesBuilder.build();
		Proto proto = NetHelper.buildProto(PType.PLogin,
				SPLogin.PUserLoginReq,
				NetHelper.pbb().userLoginReq(loginRequest).build());

		mData.setValue(LoginModuleData.Kvo_loginState, LoginModuleData.LoginState.Login_Ing);

		LoginTask.startNewLoginTask(proto);
	}

	@Override
	public void login() {
		userLogin(false);
	}

	@Override
	public void login(boolean strongNewSession) {
		userLogin(strongNewSession);
	}

	@FwEventAnnotation(event = DEvent.E_UserChange)
	public void onUserChange(FwEvent.EventArg event) {
		// se the userkey, used in crash report
		BaseContext.gUsrKey = String.valueOf(LoginHelper.getUid());
		BaseContext.gUid = LoginHelper.getUid();

		// clear the session login seq
		mAppSessionLoginSeq = 0;
	}

	@Override
	public void setLoginState(LoginModuleData.LoginState state) {
		mData.setValue(LoginModuleData.Kvo_loginState, state);
	}

    @Override
    public String getDefaultPhoneNumber() {
        return JConfig.getString(LoginModuleData.KEY_PHONE_NUMBER, "");
    }

    @Override
    public void savePhoneNumber(String number) {
        JConfig.putString(LoginModuleData.KEY_PHONE_NUMBER, number);
    }

    @FwEventAnnotation(event = DEvent.E_DataCenter_UserDBChanged_After)
	public void onUserDBChangedAfter(FwEvent.EventArg event) {
		JLoginData loginData = mData.currentLoginData;
		if (loginData.uid > 0 && mData.loginState == LoginState.Login_Offline) {
			ThreadBus.bus().post(ThreadBus.Working, new Runnable() {

                @Override
                public void run() {
                    sendEvent(DEvent.E_StartAutoLogin);

                    login();
                }
            });
		}
	}

	@Override
	public void changeNetValiable(boolean valiable) {
		if (valiable) {
            if (mData.loginState.ordinal() != LoginState.Login_Ing.ordinal()) {
                login();
            }
		} else {
			mData.setValue(LoginModuleData.Kvo_loginState,
					LoginModuleData.LoginState.Login_Offline);
		}
	}

	@Override
	public boolean logout() {
		boolean logoutResult = logout(false);

		AdaConfig.syncConfigFromServer();

		return logoutResult;
	}

	public boolean logout(boolean clearCookie) {
		final long curUid = mData.currentLoginData.uid;
		if (curUid == 0L) {
			return false;
		}

		JLog.info(mLog, String.format("[LOGIN] [logout] with clear cookie %b ", clearCookie));

		sendLogoutReq();

        DModule.ModuleApp.cast(AppInterface.class).onLogout();

		// clear data
		mData.setValue(LoginModuleData.Kvo_currentLoginData, JLoginData.emptyLoginData());

		setLoginState(LoginState.Login_Offline);

		final JLoginHistoryItem histroy = JLoginHistoryItem.info(curUid);
		histroy.state = JLoginHistoryItem.State_logout;

		if (clearCookie) {
			histroy.cookie = null;
		}

		JLoginHistoryItem.save(histroy);

		// clear browser cookies
		ThreadBus.bus().post(ThreadBus.Main, new Runnable() {

			@Override
			public void run() {
				try {
					WebView webView = new WebView(gMainContext);
					if (VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
						CookieSyncManager.createInstance(gMainContext);
					}
					CookieManager.getInstance().removeAllCookie();
					webView.destroy();
				} catch (Exception e) {
					JLog.error(mLog, "Logout and clean browser cookies exception:" + e.toString());
				}
			}
		});

        mData.setLogoutFlag();
        sendEvent(DEvent.E_OnLogout);

		return true;
	}

	private void sendLogoutReq() {
//		NetRequest.newBuilder().setGroup(protocol.PType.PLogin)
//				.setReqSub(protocol.SPLogin.PUserLogoutReq)
//				.setMessage(NetHelper.pbb().userLogoutReq(UserLogoutReq.newBuilder().build()).build())
//				.setTimeOut(DConst.KC_MaxNetOperatorTimeOut).request();
	}

	private void reloginInternal() {
		runAsyncDelayed(new Runnable() {

            @Override
            public void run() {
                sendEvent(DEvent.E_OnNeedLogin);
            }
        }, mReloginInterval);

		mReloginInterval = mReloginInterval * 2;

		// 256: [2s, 512s] 2s, 4s, 8s, 16s, 32s, 64s, 128s, 256s, 512s
		// 8: [2s, 16s] 2s, 4s, 8s, 16s
		if (mReloginInterval > DEFAULT_RELOGIN_INTERVAL * 8) {
			mReloginInterval = DEFAULT_RELOGIN_INTERVAL;
		}
	}

	@FwEventAnnotation(event = DEvent.E_OnNeedLogin)
	public void onNeededRelogin(FwEvent.EventArg event) {
		login();
	}

	@FwEventAnnotation(event = DEvent.E_LoginTask_Successful)
	public void onLoginSuccessful(FwEvent.EventArg event) {
//
		Proto loginResult = event.arg0(Proto.class);

		setLoginState(LoginState.Login_Online);

		sendEvent(DEvent.E_LoginSuccessful, loginResult);

		++mAppSessionLoginSeq;
	}

//	private boolean isNewLoginUser(Proto proto) {
//		UserLoginRes res = proto.getBody().userLoginRes;
//		UserInfo info = res.userinfo;
//
//		if (info == null) {
//			return false;
//		}
//
//		if (info.loginTime == null || info.loginTime == 0) {
//			return true;
//		}
//
//		return false;
//	}
//
//	@DNetAnnoation(group = PType.PLogin_VALUE, sub = SPLogin.PUserLoginRes_VALUE, thread = ThreadBus.Whatever)
//	public void onLoginProtoAck(Proto proto) {
//		UserLoginRes loginRespond = proto.getBody().userLoginRes;
//		//协议改了，原来成功是 ErrCode.LoginSuccessed
//		if (proto.getBody().result.code == ErrCode.Success) {
//			JUserInfo.info(loginRespond.userinfo);
//
//			long uid = proto.getBody().result.uid;
//
//			JLoginHistoryItem history = JLoginHistoryItem.info(uid);
//
//			saveLoginDataToHistoryItem(history, loginRespond.cookie);
//
//			//refresh a new login data
//			mData.setValue(LoginModuleData.Kvo_currentLoginData, JLoginData.cookieLoginData(uid, loginRespond.cookie));
//
//			// set current time in net
//			NetTime.HeartBeat(loginRespond.currenttime);
//
//			// set the report uid
//			BaseContext.gUsrKey = String.valueOf(mData.currentLoginData.uid);
//			BaseContext.gUid = mData.currentLoginData.uid;
//			// multiple calls with same uid is OK.
//			StatsHelper.reportLogin(mData.currentLoginData.uid);
//
//			if (loginRespond.newsession != null && loginRespond.newsession) {
//				JLoginHistoryItem histroy = JLoginHistoryItem.info(mData.currentLoginData.uid);
//				histroy.pushseq = 0;
//				JLoginHistoryItem.save(histroy);
//			}
//		} else {
//            mData.currentLoginData.setValue(JLoginData.Kvo_loginResult, proto.body.result);
//			JLog.debug(mLog, String.format(Locale.getDefault(), "login ack failed (result:%d)",
//					Wire.get(proto.body.result.code, Result.DEFAULT_CODE).getValue()));
//		}
//
//		mReloginInterval = DEFAULT_RELOGIN_INTERVAL;
//	}

	private void saveLoginDataToHistoryItem(JLoginHistoryItem history, String cookie) {
		history.ts = System.currentTimeMillis();
		history.cookie = cookie;
		history.state = JLoginHistoryItem.State_autologin;

		JLoginHistoryItem.save(history);
	}

//	@DNetAnnoation(group = protocol.PType.PPush_VALUE, sub = protocol.SPPush.PForceLogout_VALUE, thread = ThreadBus.Main)
//	public void onPushForceLogout(Proto proto) {
//		final protocol.ForceLogoutPush push = proto.getBody().forceLogoutPush;
//
//		if (push.blackmac.indexOf(mData.macAddress) != -1) {
//			if(logout()) {
//                ThreadBus.bus().postDelayed(ThreadBus.Main, new Runnable() {
//                    @Override
//                    public void run() {
//                        sendEvent(DEvent.E_ForceLogout, push.reason);
//                    }
//                }, 800L);
//			}
//		}
//		JLog.info(mLog, String.format("[LOGIN] [forcelogout] with clear cookie %s ",
//				push.toString()));
//	}

	@FwEventAnnotation(event = DEvent.E_App_EnterForeground)
	public void onAppEnterForeground(FwEvent.EventArg event) {
		if (mData.loginState == LoginState.Login_Offline
				&& JNetworkUtil.isNetworkAvailable()
				//这个要加一个过滤
				&& (mData.currentLoginData.loginBy == LoginBy.LoginBy_Cookie)) {
			login();
		}
	}

	@FwEventAnnotation(event = DEvent.E_NetBroken)
	public void onNetBroken(FwEvent.EventArg event) {
		setLoginState(LoginState.Login_Offline);

		if (JNetworkUtil.isNetworkAvailable() && mData.currentLoginData.loginBy != LoginBy.LoginBy_Mac) {
			JLog.info(mLog, "Start Relogin when net broken");
			reloginInternal();
		} else {
			JLog.error(mLog, "net error happend: net disable, stop relogin");
		}
	}

	@FwEventAnnotation(event = DEvent.E_LoginTask_Failed)
	public void onLoginFailed(FwEvent.EventArg event) {
		JLog.info(mLog, "Start Relogin when login failed");

		Integer result = event.arg0(Integer.class);

		sendEvent(DEvent.E_LoginFailed, result);

		setLoginState(LoginState.Login_Offline);

		//result < 0是客户端自己定义的错误码，如果是服务器返回的错误码，在NetClient中处理
		if (result < 0) {
			Ln.doDealWithException(this, null, result, null, null);
		}

//		if (result == NetClient.LocalErrCode_LoginFailed) {
//			reloginInternal();
//		} else {
//			JLog.info(mLog, "Login Failed: %d", result);
//
//			if (needLogoutWhenLoginFailed(result)) {
//				logout(true);
//			}
//		}
	}

	@Override
	public boolean needLogoutWhenLoginFailed(int result) {
		// TODO: 2015/11/9
//		return result == ErrCode.LoginFailedNotExist_VALUE
//				|| result == ErrCode.LoginFailedMacid_VALUE
//				|| result == ErrCode.LoginFailedIdentifyCode_VALUE
//				|| result == ErrCode.LoginFailedPassword_VALUE
//				|| result == ErrCode.LoginFailedServer_VALUE
//				|| result == ErrCode.LoginFailedForbidden_VALUE
//				|| result == ErrCode.TokenInvalid_VALUE;
		return result == ErrCode.TokenInvalid_VALUE ||
                result == ErrCode.CookieExpired_VALUE ||
                result == ErrCode.CookieInvalid_VALUE;
	}

	// 获取验证码
	@Override
	public void getUserVerifyCode(final String telephone, final boolean checkPhoneNumRepeat,
	                              final NetChannelSendResultInterface tri) {
//		new Thread() {
//			public void run() {
//
//				if (TextUtils.isEmpty(mData.macAddress)) {
//					mData.justMakeSafeAddress();
//				}
//
//				UserTokenReq req = UserTokenReq.newBuilder().macid(mData.macAddress)
//						.mobilephone(telephone)
//						.checkrepeat(checkPhoneNumRepeat).build();
//				Proto proto = NetHelper.buildProto(protocol.PType.PLogin,
//						protocol.SPLogin.PUserTokenReq, NetHelper.pbb()
//								.userTokenReq(req).build());
//				NetClient.newChannelSend(new NetModuleData.NetDataInterface() {
//
//					@Override
//					public void onException(NetDataChannel channel, int error,
//					                        IOException exception) {
//						tri.onNetError(error, exception);
//					}
//
//					@Override
//					public void onData(NetDataChannel channel, Proto proto) {
//						if (proto.getHead().getGroup() == protocol.PType.PLogin_VALUE
//								&& proto.getHead().getSub() == protocol.SPLogin.PUserTokenRes_VALUE) {
//                            if (proto.body.result.success
//                                    && null != proto.body.userTokenRes
//                                    && null != proto.body.userTokenRes.token) {
//                                mData.setValue(LoginModuleData.Kvo_userToken, proto.body.userTokenRes.token);
//                            }
//							tri.onResult(proto);
//						}
//					}
//				}, proto);
//			}
//		}.start();
			}

//	// 注册
//    // FIXME: 16-10-25
//    //@Override
//	public void register(final String identifyCode,
//	                     final protocol.UserInfo info,
//	                     final String password,
//	                     final boolean resetPassword,
//	                     final NetChannelSendResultInterface rri) {
//		new Thread() {
//			public void run() {
//                if (TextUtils.isEmpty(mData.macAddress)) {
//                    mData.justMakeSafeAddress();
//                }
//
//				UserActivateReq.Builder reqBuilder = UserActivateReq
//						.newBuilder().macid(mData.macAddress)
//						.identifyingCode(identifyCode).userInfo(info)
//						.password(password).token(mData.userToken)
//						.fromStore(DConst.sChannelID);
//				if (resetPassword) {
//					reqBuilder.resetpassword(true);
//				}
////				if (refereeUid != null && refereeUid != -1) {
////					reqBuilder.refereeUid(refereeUid);
////				}
//				UserActivateReq req = reqBuilder.build();
//				Proto proto = NetHelper.buildProto(protocol.PType.PLogin,
//						protocol.SPLogin.PUserActivateReq, NetHelper.pbb()
//								.userActivateReq(req).build());
//				NetClient.newChannelSend(new NetModuleData.NetDataInterface() {
//
//					@Override
//					public void onException(NetDataChannel channel, int error,
//					                        IOException exception) {
//						rri.onNetError(error, exception);
//					}
//
//					@Override
//					public void onData(NetDataChannel channel, Proto proto) {
//						if (proto.getHead().getGroup() == protocol.PType.PLogin_VALUE
//								&& proto.getHead().getSub() == protocol.SPLogin.PUserActivateRes_VALUE) {
//                            if (null != proto.body.userActivateRes) {
//                                if (null != proto.body.userActivateRes.userInfo) {
//                                    JUserInfo.info(proto.body.userActivateRes.userInfo);
//                                    if (null != proto.body.userActivateRes.cookie) {
//                                        mData.setValue(LoginModuleData.Kvo_currentLoginData,
//                                                JLoginData.cookieLoginData(proto.body.userActivateRes.userInfo.uid,
//                                                        proto.body.userActivateRes.cookie));
//                                    }
//                                }
//                            }
//
//							rri.onResult(proto);
//						}
//					}
//				}, proto);
//			}
//		}.start();
//	}

	@Override
	public String getDeviceFingerStr() {
		if (TextUtils.isEmpty(mData.deviceInfo)) {
			DeviceInfo info = new DeviceInfo();
			info.android_id = Settings.Secure.getString(Module.gMainContext.getContentResolver(), Settings.Secure.ANDROID_ID);
			info.imei = JUtils.getImei(Module.gMainContext);
			info.serial = Build.SERIAL;

			try {
				WifiManager wifi = (WifiManager) Module.gMainContext.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifi.getConnectionInfo();
				if (null != wifiInfo) {
					info.wlan_mac = wifiInfo.getMacAddress();
				}
			} catch (Exception e) {
				JLog.error(mLog, "getDeviceFingerStr exception:" + e);
			}

			try {
				BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if (null != bluetoothAdapter) {
					// FIXME: 16-10-25
//				info.bt_mac = bluetoothAdapter.getAddress();
				}
			} catch (Exception e) {
				JLog.error(mLog, "getDeviceFingerStr exception:" + e);
			}

			info.model = Build.MODEL;
			info.manufacturer = Build.MANUFACTURER;
			info.brand = Build.BRAND;
			info.board = Build.BOARD;
			info.product = Build.PRODUCT;
			info.device = Build.DEVICE;
			info.hardware = Build.HARDWARE;
			try {
				mData.deviceInfo = new Gson().toJson(info);
			} catch (Exception e) {
				JLog.error(mLog, "getDeviceFingerStr exception:" + e.toString());
				mData.deviceInfo = "";
			}
		}

		return mData.deviceInfo;
	}


}