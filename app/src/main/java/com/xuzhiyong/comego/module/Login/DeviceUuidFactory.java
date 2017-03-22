package com.xuzhiyong.comego.module.Login;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.duowan.fw.root.BaseContext;
import com.duowan.fw.util.JConfig;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JStringUtils;
import com.duowan.fw.util.JUtils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class DeviceUuidFactory {
	/// data members
    protected static final String PREFS_UUID_ID = "com.duowan.ada.uuid_id";
    
    /// uuid
    protected static UUID uuid;

    /// default constructor
    public DeviceUuidFactory() {
    	// global context
    	Context context = BaseContext.gContext;
    	// gen uuid
        if( uuid ==null ) {
            synchronized (DeviceUuidFactory.class) {
                if( uuid == null) {
                    final String id = JConfig.getString(PREFS_UUID_ID, null);

                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                    	try {
	                        uuid = UUID.fromString(id);
						} catch (Exception e) {
							// TODO: handle exception
							uuid = UUID.randomUUID();
							// Write the value out to the prefs file
	                        JConfig.putString(PREFS_UUID_ID, uuid.toString());
						}
                    } else {

                    	// android id
                        final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
                        // unless it's not available, then fallback on mac address
                        // then fallback on serial number
                        // then fallback on a random number which we store
                        // to a prefs file
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                                JLog.info(this, String.format("gen uuid from ANDROID_ID: %s", androidId));
                            } else {
                            	// serial number
                        		final String serialNumber = android.os.Build.SERIAL;
                        		if (serialNumber != null) {
	                        		JLog.info(this, String.format("gen uuid from serialNumber: %s", serialNumber));
									uuid = UUID.nameUUIDFromBytes(serialNumber.getBytes("utf8"));
								}else {
	                            	// device id
	                                final String deviceId = JUtils.getImei(context);
	                                if (!JStringUtils.isNullOrEmpty(deviceId)) {
										uuid = UUID.nameUUIDFromBytes(deviceId.getBytes("utf8"));
										JLog.info(this, String.format("gen uuid from deviceId: %s", deviceId));
									}else {
										// mac address
										WifiManager wifi = (WifiManager) BaseContext.gContext.getSystemService(Context.WIFI_SERVICE);
										WifiInfo info = wifi.getConnectionInfo();
										String mac = info.getMacAddress();
										if (mac != null) {
											uuid = UUID.nameUUIDFromBytes(mac.getBytes("utf8"));
											JLog.info(this, String.format("gen uuid from mac address: %s", mac));
										}else {
											// random uuid
											uuid = UUID.randomUUID();
											JLog.info(this, String.format("gen uuid from randomUUID: %s", uuid.toString()));
										}
									}
								}
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        // Write the value out to the prefs file
                        JConfig.putString(PREFS_UUID_ID, uuid.toString());
                    }

                }
            }
        }

    }


    /**
     * Returns a unique UUID for the current android device.  As with all UUIDs, this unique ID is "very highly likely"
     * to be unique across all Android devices.  Much more so than ANDROID_ID is.
     *
     * The UUID is generated by using ANDROID_ID as the base key if appropriate, falling back on
     * TelephonyManager.getDeviceID() if ANDROID_ID is known to be incorrect, 
     * and if deviceId is incrorrect then falling back on mac address, and finally falling back
     * on a random UUID that's persisted to SharedPreferences if getDeviceID() does not return a
     * usable value.
     *
     * In some rare circumstances, this ID may change.  In particular, if the device is factory reset a new device ID
     * may be generated.  In addition, if a user upgrades their phone from certain buggy implementations of Android 2.2
     * to a newer, non-buggy version of Android, the device ID may change.  Or, if a user uninstalls your app on
     * a device that has neither a proper Android ID nor a Device ID, this ID may change on reinstallation.
     *
     * Note that if the code falls back on using TelephonyManager.getDeviceId(), the resulting ID will NOT
     * change after a factory reset.  Something to be aware of.
     *
     * Works around a bug in Android 2.2 for many devices when using ANDROID_ID directly.
     *
     * http://code.google.com/p/android/issues/detail?id=10603
     *
     * @return a UUID that may be used to uniquely identify your device for most purposes.
     */
    public UUID getDeviceUuid() {
        return uuid;
    }
    
    /**
     * @return device uuid string without '-'
     * */
    public String getDeviceUuidString(){
    	return JStringUtils.removeChar(uuid.toString(), '-');
    }
    
    /**
     * @return the serial number of device
     * */
    public String deviceSerialNumber() {
		return android.os.Build.SERIAL;
	}
    
    /**
     * @return the default display of device
     * */
    public String deviceDisplay() {
		return android.os.Build.DISPLAY;
	}
    
    /**
     * @return the manufacture of device
     * */
    public String manufacture(){
    	return android.os.Build.MANUFACTURER;
    }
    
    /**
     * @return the model of device
     * */
    public String model(){
    	return android.os.Build.MODEL;
    }
    
    /**
     * @return the device kind 
     * */
    public String device() {
		return android.os.Build.DEVICE;
	}
    
    
    /**
     * @return the line 1 number as the phone number
     * */
    public String phoneNumber(){
    	String phone = JUtils.getPhoneNumber();
    	if (phone == null) {
			phone = "";
		}
    	return phone;
    }
    
    /**
     * @return the mac address if exits
     * */
    public String macAddress() {
    	WifiManager wifi = (WifiManager) BaseContext.gContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String mac = info.getMacAddress();
		return mac;
	}
    
    /**
     * Show the device info
     * */
    public void logDeviceInfo(){
    	logDeviceIds();
    	logDeviceBuildInfo();
        logTelephonyInfo();
	}
    
    /**
     * Show ANDROID_ID and Mac address
     * */
    public void logDeviceIds(){
    	JLog.info(this, "\n***********************DEVICE ID_MAC INFO*******************");
	    // android id
        final String androidId = Secure.getString(BaseContext.gContext.getContentResolver(), Secure.ANDROID_ID);
        JLog.info(this, String.format("AndroidId = %s", androidId != null ? androidId : ""));
        
        // mac address
        WifiManager wifi = (WifiManager) BaseContext.gContext.getSystemService(Context.WIFI_SERVICE);
		String mac = wifi.getConnectionInfo().getMacAddress();
		JLog.info(this, String.format("Mac address = %s", mac != null ? mac : ""));
    }
    
    
    /**
     * Show Device Build information
     * */
    public void logDeviceBuildInfo(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n***********************DEVICE BUILD INFO*******************");
    	sb.append("\nSerialNumber = " + android.os.Build.SERIAL);
    	sb.append("\nHardware = " + android.os.Build.HARDWARE);
    	sb.append("\nID = " + android.os.Build.ID);
    	sb.append("\nDisplay = " + android.os.Build.DISPLAY);
    	sb.append("\nDevice = " + android.os.Build.DEVICE);
    	sb.append("\nProduct = " + android.os.Build.PRODUCT);
    	sb.append("\nBoard = " + android.os.Build.BOARD);
    	sb.append("\nManufacture = " + android.os.Build.MANUFACTURER);
    	sb.append("\nBrand = " + android.os.Build.BRAND);
    	sb.append("\nModel = " + android.os.Build.MODEL);
    	JLog.info(this, sb.toString());
    }
    
    /**
     * Show the telephony information
     * */
    public void logTelephonyInfo() {
    	TelephonyManager tm = (TelephonyManager) BaseContext.gContext.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();
        sb.append("\n***********************DEVICE TELEPHONY INFO*******************");
        try {
            sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
            sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
            sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
            sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
            sb.append("\nLine1Number = " + tm.getLine1Number());
            sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        } catch (SecurityException e) {
            sb.append("\nREAD_PHONE_STATE permission denied;");
        }
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimState = " + tm.getSimState());
        JLog.info(this, sb.toString());
    }
}
