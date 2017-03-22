package com.xuzhiyong.comego.module.Login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xuzhiyong.comego.module.DConst;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DModule;

import java.util.regex.Pattern;


public class LoginHelper {

    public static final long LOGIN_TIMEOUT_DURATION = 35000L;

    public static long getUid() {
        LoginModuleData loginData = (LoginModuleData) (DData.loginModuleData.data());
        return loginData.currentLoginData.uid;
    }

    public static int loginState() {
        return DData.loginModuleData.cast(LoginModuleData.class).loginState.ordinal();
    }

    public static boolean isMacUser() {
        return isMacUser(getUid());
    }

    public static boolean isMacUser(long uid) {
        return uid >= DConst.MAC_USER_ID_START;
    }

    // is online
    public static boolean isOnLine() {
        return loginState() == LoginModuleData.LoginState.Login_Online.ordinal();
    }

    public static boolean canAutoLogin() {
        JLoginData loginData = curLoginData();
        return loginData.uid > 0L && !TextUtils.isEmpty(loginData.cookie);
    }

    public static void getUserVerifyCode(String phoneNumber, boolean checkRepeat, LoginModule.NetChannelSendResultInterface i) {
        DModule.ModuleLogin.cast(LoginInterface.class).getUserVerifyCode(phoneNumber, checkRepeat, i);
    }

    public static void register(String identifyCode, String password, LoginModule.NetChannelSendResultInterface rri) {
        // FIXME: 16-10-25
//        DModule.ModuleLogin.cast(LoginInterface.class).register(identifyCode,
//                protocol.UserInfo.newBuilder().uid(0L).build(), password, false, rri);
    }

    public static void resetPassword(String identifyCode, String password, LoginModule.NetChannelSendResultInterface rri) {
        // FIXME: 16-10-25
//        DModule.ModuleLogin.cast(LoginInterface.class).register(identifyCode,
//                protocol.UserInfo.newBuilder().uid(0L).build(), password, true, rri);
    }

    public static void login() {
        DModule.ModuleLogin.cast(LoginInterface.class).login();
    }

    public static void login(boolean strongNewSession) {
        DModule.ModuleLogin.cast(LoginInterface.class).login(strongNewSession);
    }

    public static boolean logout() {
        return DModule.ModuleLogin.cast(LoginInterface.class).logout();
    }

	public static JLoginData setNewLoginData(JLoginData loginData) {
		DData.loginModuleData.cast(LoginModuleData.class).setValue
				(LoginModuleData.Kvo_currentLoginData, loginData);

		return loginData;
	}

	@NonNull
	public static JLoginData curLoginData() {
		return DData.loginModuleData.cast(LoginModuleData.class).currentLoginData;
	}

	public static void loginWithMacAddress() {
		setNewLoginData(JLoginData.macLoginData());

		login();
	}

    private static final String PHONE_NUMBER_PATTERN = "^1\\d{10}$";
    private static final Pattern sPhotoNumberPattern = Pattern.compile(PHONE_NUMBER_PATTERN);
    public static boolean isValidPhoneNumber(String number) {
        return sPhotoNumberPattern.matcher(number).matches();
    }

    //还要改AndroidManifest.xml
    public static final String QQ_LOGIN_SCOPE = "all";
    public static final String QQ_LOGIN_ACCESSTOKEN_KEY = "access_token";
	public static final String QQ_LOGIN_OPENID_KEY = "openid";
	public static final String QQ_LOGIN_USERNAME_KEY = "nickname";

    public interface LoginWithQQResultListener {
        // FIXME: 16-10-25
//        void onError(UiError error);
        void onCancel();
    }
// FIXME: 16-10-25
//    public static void loginWithQQ(final Tencent tencent, final Activity act, final LoginWithQQResultListener listener) {
//
//        final JLoginData loginData = setNewLoginData(JLoginData.qqLoginData());

//	    try {
//		    tencent.login(act, QQ_LOGIN_SCOPE, new IUiListener() {
//
//			    @Override
//			    public void onComplete(Object arg0) {
//				    if (tencent.isSessionValid()) {
//					    try {
//						    JSONObject result = (JSONObject) arg0;
//
//						    String accessToken = result.getString(QQ_LOGIN_ACCESSTOKEN_KEY);
//						    String openId = result.getString(QQ_LOGIN_OPENID_KEY);
//
//						    loginData.setValue(JLoginData.Kvo_qqOpenId, openId);
//						    loginData.setValue(JLoginData.Kvo_qqAccessToken, accessToken);
//
//						    onQQLoginComplete(loginData, result, act, tencent, listener);
//					    } catch (JSONException e) {
//						    JLog.error(LoginHelper.class, "qq login result json error : " + arg0.toString());
//
//						    onQQLoginError(null, Module.gMainContext.
//								    getString(R.string.login_failed_parse_json_error), listener);
//					    }
//				    } else {
//					    onQQLoginError(null, Module.gMainContext.
//							    getString(R.string.login_failed_invalid_session), listener);
//				    }
//			    }
//
//			    @Override
//			    public void onError(UiError uiError) {
//				    onQQLoginError(uiError, null, listener);
//			    }
//
//			    @Override
//			    public void onCancel() {
//				    onQQLoginCancel(listener);
//			    }
//		    });
//		    //这是崩溃上报的一个崩溃，QQ登录SDK内部的
//	    } catch (IndexOutOfBoundsException e) {
//		    JLog.error(LoginHelper.class, "qq login error : " + e.toString());
//
//		    onQQLoginError(null, Module.gMainContext
//				    .getString(R.string.qq_login_failed_qqsdk_error), listener);
//	    }
//    }

//	private static void onQQLoginComplete(final JLoginData loginData, final JSONObject qqLoginResult, final Context context,
//	                                      final Tencent tencent, final LoginWithQQResultListener listener) {
//
//		final com.tencent.connect.UserInfo userInfo = new UserInfo(context, tencent.getQQToken());
//		userInfo.getUserInfo(new IUiListener() {
//
//			@Override
//			public void onComplete(Object o) {
//				JSONObject qqUserInfo = (JSONObject)o;
//				try {
//					String userName = qqUserInfo.has(QQ_LOGIN_USERNAME_KEY) ?
//							qqUserInfo.getString(QQ_LOGIN_USERNAME_KEY) : null;
//
//					loginData.setValue(JLoginData.Kvo_userName, userName);
//
//					login();
//				} catch (JSONException e) {
//					JLog.error(this, "get qq user name error:" + e.toString()
//							+ " qqLoginResult:" + qqLoginResult.toString()
//							+ "qqUserInfo:" + qqUserInfo.toString());
//
//					onQQLoginError(null, Module.gMainContext.
//							getString(R.string.login_failed_parse_json_error), listener);
//				}
//			}
//
//			@Override
//			public void onError(UiError uiError) {
//
//			}
//
//			@Override
//			public void onCancel() {
//
//			}
//		});
//	}

//	private static void onQQLoginError(UiError error, String message, LoginWithQQResultListener listener) {
//		String showMessage;
//
//		if (error == null) {
//			showMessage = message == null ?
//					Module.gMainContext.getString(R.string.login_failed_unkonw) : message;
//		} else {
//			JLog.error(UiError.class, "qq login error code = " +
//					error.errorCode + " detail:" + error.errorDetail +
//					"  message:" + error.errorMessage);
//
//			showMessage = error.errorMessage;
//		}
//
//		GToast.show(showMessage);
////        FloatManager.getInstance().showFloatView(showMessage);
//		if (listener != null) {
//			listener.onError(error);
//		}
//	}
//
//	private static void onQQLoginCancel(LoginWithQQResultListener listener) {
//		GToast.show(R.string.login_is_cancelled);
////        FloatManager.getInstance().showFloatView(R.string.login_is_cancelled);
//		if (listener != null) {
//			listener.onCancel();
//		}
//	}
//
//    public static final String WEIXIN_LOGIN_SCOPE = "snsapi_userinfo";
//    public static String sWeixinLoginState;
//    public static JLoginData sWeixinLoginData;
//    public static boolean sWeixinGetCode;
//    public static final String WEIXIN_API_BASE_URI = "https://api.weixin.qq.com/";
//    public static final String WEIXIN_API_ACCESS_TOKEN_PATH = "sns/oauth2/access_token";
//
//    public static void loginWithWeixin(IWXAPI api) {
//        JLoginData loginData = JLoginData.weixinLoginData();
//        sWeixinLoginData = loginData;
//        setNewLoginData(loginData);
//        if (!api.isWXAppInstalled()) {
//            ThreadBus.bus().post(ThreadBus.Main, new Runnable() {
//                @Override
//                public void run() {
//                    onWeixinLoginError(null, Module.gMainContext.getString(R.string.weixin_not_installed));
//                }
//            });
//            return;
//        }
//
//        SendAuth.Req req = new SendAuth.Req();
//        req.scope = WEIXIN_LOGIN_SCOPE;
//        sWeixinLoginState = String.valueOf(System.currentTimeMillis());
//        req.state = sWeixinLoginState;
//        sWeixinGetCode = false;
//
//        api.sendReq(req);
//    }
//
//    public static void onGetWeixinCode(BaseResp resp) {
//        sWeixinGetCode = true;
//        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
//            if (resp instanceof SendAuth.Resp) {
//                SendAuth.Resp sResp = (SendAuth.Resp) resp;
//                if (sWeixinLoginState.equals(sResp.state)) {
//                    getWeixinAccessToken(sResp.code);
//                } else {
//                    onWeixinLoginError(null, Module.gMainContext.getString(R.string.exception_weixin_state_not_equal));
//                }
//            }
//        } else {
//            onWeixinLoginError(resp, null);
//        }
//    }
//
//    public static void onWeixinLoginCancel() {
//        sWeixinLoginData = null;
//    }
//
//    interface GetAccessToken {
//        @GET(WEIXIN_API_ACCESS_TOKEN_PATH)
//        Observable<ResponseBody> getToken(@QueryMap Map<String, String> map);
//    }
//
//    public static void getWeixinAccessToken(String code) {
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(WEIXIN_API_BASE_URI)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
//        GetAccessToken getAccessToken = retrofit.create(GetAccessToken.class);
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("appid", ThirdParty.APPID_WEIXIN);
//        map.put("secret", ThirdParty.APPSECRET_WEIXIN);
//        map.put("code", code);
//        map.put("grant_type", "authorization_code");
//        getAccessToken.getToken(map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(HandlerScheduler.from(ThreadBus.bus().getHandler(ThreadBus.Working)))
//                .subscribe(new Action1<ResponseBody>() {
//                               @Override
//                               public void call(ResponseBody body) {
//                                   try {
//                                       String s = body.string();
//                                       JSONObject jo = new JSONObject(s);
//                                       String accessToken = jo.getString("access_token");
//                                       String openId = jo.getString("openid");
//                                       if (curLoginData() != sWeixinLoginData) {
//                                           setNewLoginData(sWeixinLoginData);
//                                       }
//                                       curLoginData().setValue(JLoginData.Kvo_qqOpenId, openId);
//                                       curLoginData().setValue(JLoginData.Kvo_qqAccessToken, accessToken);
//                                       login();
//                                       sWeixinLoginData = null;
//                                   } catch (Exception e) {
//                                       e.printStackTrace();
//                                       onWeixinLoginError(null, Module.gMainContext.
//                                               getString(R.string.exception_get_weixin_accessetoken));
//                                   }
//                               }
//                           },
//                        new Action1<Throwable>() {
//                            @Override
//                            public void call(Throwable throwable) {
//                                onWeixinLoginError(null, Module.gMainContext.
//                                        getString(R.string.exception_get_weixin_accessetoken));
//                            }
//                        });
//
//    }
//
//    private static void onWeixinLoginError(BaseResp resp, String reason) {
//        if (TextUtils.isEmpty(reason)) {
//            int errCode = Integer.MAX_VALUE;
//            if (null != resp) {
//                errCode = resp.errCode;
//            }
//            switch (errCode) {
//                case BaseResp.ErrCode.ERR_USER_CANCEL:
//                    reason = Module.gMainContext.getString(R.string.exception_weixin_usercancel);
//                    break;
//                case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                    reason = Module.gMainContext.getString(R.string.exception_weixin_auth_denied);
//                    break;
//                case BaseResp.ErrCode.ERR_SENT_FAILED:
//                    reason = Module.gMainContext.getString(R.string.exception_weixin_sent_failed);
//                    break;
//                case BaseResp.ErrCode.ERR_COMM:
//                    reason = Module.gMainContext.getString(R.string.exception_weixin_comm);
//                    break;
//                case BaseResp.ErrCode.ERR_UNSUPPORT:
//                    reason = Module.gMainContext.getString(R.string.exception_weixin_unsupport);
//                    break;
//                default:
//                    reason = Module.gMainContext.getString(R.string.login_failed_unkonw);
//                    break;
//            }
//        }
//        JLog.error("WeixinLogin", "Weixin login error:" + reason);
//        sWeixinLoginData.setResult(false, reason);
//        sWeixinLoginData = null;
//    }

}
