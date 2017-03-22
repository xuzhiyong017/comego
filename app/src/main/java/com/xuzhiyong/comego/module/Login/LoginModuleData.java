package com.xuzhiyong.comego.module.Login;

import android.text.TextUtils;

import com.duowan.fw.Module;
import com.duowan.fw.ModuleData;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.util.JConfig;
import com.duowan.fw.util.JStringUtils;
import com.duowan.fw.util.JUtils;

public class LoginModuleData extends ModuleData {

    private final String key_login_mac_address = "com.duowan.key_login_mac_address";

    static final String KEY_PHONE_NUMBER = "_key_phone_number";

    public enum LoginState {
        Login_Offline,
        Login_Ing,
        Login_Online, LoginState,
    }

    public static final String Kvo_loginState = "loginState";
    @KvoAnnotation(name = Kvo_loginState)
    public volatile LoginState loginState = LoginState.Login_Offline;

    public static final String Kvo_currentLoginData = "currentLoginData";
    @KvoAnnotation(name = Kvo_currentLoginData)
    public JLoginData currentLoginData = JLoginData.emptyLoginData();

    public static final String Kvo_device = "device";
    @KvoAnnotation(name = Kvo_device)
    public String device = JStringUtils.EMPTY_STR;

    public static final String Kvo_macAddress = "macAddress";
    @KvoAnnotation(name = Kvo_macAddress)
    public String macAddress = JStringUtils.EMPTY_STR;

    public static final String Kvo_deviceInfo = "deviceInfo";
    @KvoAnnotation(name = Kvo_deviceInfo)
    public String deviceInfo = JStringUtils.EMPTY_STR;

    public static final String Kvo_userToken = "userToken";
    @KvoAnnotation(name = Kvo_userToken)
    public String userToken = JStringUtils.EMPTY_STR;

    public volatile boolean logoutFlag = false;

    public void setLogoutFlag() {
        logoutFlag = true;
    }

    public void clearLogoutFlag() {
        logoutFlag = false;
    }

    public void justMakeSafeAddress() {
        if (macAddress == null || macAddress.length() == 0) {
            restoreMacAddress();
        }

        if (macAddress != null && macAddress.length() > 0) {
            saveMacAddress();
        }
    }

    public void restoreMacAddress() {
        // mac address
        String imei = JUtils.getImei(Module.gMainContext);
        String mac = JUtils.macAddress;

        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(imei)) {
            sb.append(imei);
        }

        if (!TextUtils.isEmpty(mac)) {
            if (sb.length() != 0) {
                sb.append("#");
            }
            sb.append(mac);
        }

        if (sb.length() == 0) {
	        //it may be null
	        if(DeviceUuidFactory.uuid != null) {
		        sb.append(DeviceUuidFactory.uuid.toString());
	        }
        }

        macAddress = JConfig.getString(key_login_mac_address, sb.toString());
    }

    public void saveMacAddress() {
        if (macAddress == null || macAddress.length() == 0) {
            return;
        }
        JConfig.putString(key_login_mac_address, macAddress);
    }
}
