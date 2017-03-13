package com.duowan.fw.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import com.duowan.fw.root.BaseContext;

import java.net.InetSocketAddress;

/**
 * Use to listen the status change of network.
 *
 */
public class JNetworkUtil {
	
	/**
	 * Tell whether the current network is Wifi.
	 * NOTE this can only be called when YYMobile.gContext is set.
	 * @return True if wifi is active, false otherwise.
	 */
	public static boolean isWifiActive() {
		return isWifiActive(BaseContext.gContext);
	}

	public static boolean isWifiActive(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
	}
	
	public static boolean is2GOr3GActive() {
		return is2GOr3GActive(BaseContext.gContext);
	}

	public static boolean is2GOr3GActive(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
	}

	public static boolean isNetworkStrictlyAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) BaseContext.gContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.isConnected()) {
			return true;
		}
		else {
			String info = null;
			if (ni != null) {
				info = "network type = " + ni.getType() + ", "
						+ (ni.isAvailable() ? "available" : "inavailable")
						+ ", " + (ni.isConnected() ? "" : "not") + " connected";
			} else {
				info = "no active network";
			}
			JLog.info("network", info);
			return false;
		}
	}

	public static boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) BaseContext.gContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
		if (ni == null) {
			return false;
		}
		return ni.isConnected()
				|| (ni.isAvailable() && ni.isConnectedOrConnecting());
	}
    
    public static void openNetworkConfig(Context c) {
        Intent i = null;
        if (android.os.Build.VERSION.SDK_INT > 10) {
            i = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        }
        else {
            i = new Intent();
            i.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
            i.setAction(Intent.ACTION_MAIN);
        }
        try {
            c.startActivity(i);
        }
        catch (Exception e) {
        }
    }

    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;
    private static final int DEFAULT_PROXY_PORT = 80;
    
    public static InetSocketAddress getTunnelProxy() {
        if (BaseContext.gContext.checkCallingOrSelfPermission("android.permission.WRITE_APN_SETTINGS") == 
            PackageManager.PERMISSION_DENIED) {
            return null;
        }
        ConnectivityManager cm = (ConnectivityManager) BaseContext.gContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null) {
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return null;
            }
        }
        String proxy = "";
        String portStr = "";
        Uri uri = Uri.parse("content://telephony/carriers/preferapn");
        Cursor cr = BaseContext.gContext.getContentResolver().query(uri, null,
                                                                 null, null, null);
        if (cr != null && cr.moveToNext()) {
            proxy = cr.getString(cr.getColumnIndex("proxy"));
            portStr = cr.getString(cr.getColumnIndex("port"));
            JLog.info("getTunnelProxy", JUtils.getOperator() + ", proxy = " + proxy + ", port = " + portStr);
            if (proxy != null && proxy.length() > 0) {
                cr.close(); 
                cr = null;
                int port;
                try {
                    port = Integer.parseInt(portStr);
                    if (port < MIN_PORT || port > MAX_PORT) {
                        port = DEFAULT_PROXY_PORT;
                    }
                }
                catch (Exception e) {
                    JLog.info("getTunnelProxy", "port is invalid, e = " + e);
                    port = DEFAULT_PROXY_PORT;
                }
                InetSocketAddress addr = null;
                try {
                    addr = new InetSocketAddress(proxy, port);
                }
                catch (Exception e) {
                    JLog.info("getTunnelProxy", "create address failed, e = " + e);
                }
                return addr;
            }
        }
        if (cr != null) {
            cr.close(); 
            cr = null;
        }
        return null;
    }

    public static enum MobileIMSI{
        MobileIMSI_Unknown,
    	MobileIMSI_Mobile,
        MobileIMSI_UniCom,
    	MobileIMSI_TelCom,
    }
    
    public static MobileIMSI getMobileIMSI(Context context){
    	TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    	String imsi;
        try {
            imsi = telManager.getSubscriberId();
        } catch (SecurityException e) {
            JLog.error("NetworkUtil.getMobileIMSI", "READ_PHONE_STATE permission denied:" + e.getMessage());
            imsi = null;
        }
    	if(imsi!=null){  
            if(imsi.startsWith("46000") 
            		|| imsi.startsWith("46002") 
            		|| imsi.startsWith("46007")){
            	//因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号  
                //中国移动  
            	return MobileIMSI.MobileIMSI_Mobile;
            }else if(imsi.startsWith("46001")){  
                //中国联通  
            	return MobileIMSI.MobileIMSI_UniCom;
            }else if(imsi.startsWith("46003")){  
                //中国电信  
            	return MobileIMSI.MobileIMSI_TelCom;
            }  
        } 
    	
    	return MobileIMSI.MobileIMSI_Unknown;
    }
    
//    ﻿﻿public static boolean isFastMobileNetwork(Context context) {
//    	TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//    	switch (telephonyManager.getNetworkType()) {
//    	case TelephonyManager.NETWORK_TYPE_1xRTT:
//    		return false; // ~ 50-100 kbps
//    	case TelephonyManager.NETWORK_TYPE_CDMA:
//    		return false; // ~ 14-64 kbps
//    	case TelephonyManager.NETWORK_TYPE_EDGE:
//    		return false; // ~ 50-100 kbps
//    	case TelephonyManager.NETWORK_TYPE_EVDO_0:
//    		return true; // ~ 400-1000 kbps
//    	case TelephonyManager.NETWORK_TYPE_EVDO_A:
//    		return true; // ~ 600-1400 kbps
//    	case TelephonyManager.NETWORK_TYPE_GPRS:
//    		return false; // ~ 100 kbps
//    	case TelephonyManager.NETWORK_TYPE_HSDPA:
//    		return true; // ~ 2-14 Mbps
//    	case TelephonyManager.NETWORK_TYPE_HSPA:
//    		return true; // ~ 700-1700 kbps
//    	case TelephonyManager.NETWORK_TYPE_HSUPA:
//    		return true; // ~ 1-23 Mbps
//    	case TelephonyManager.NETWORK_TYPE_UMTS:
//    		return true; // ~ 400-7000 kbps
//    	case TelephonyManager.NETWORK_TYPE_EHRPD:
//    		return true; // ~ 1-2 Mbps
//    	case TelephonyManager.NETWORK_TYPE_EVDO_B:
//    		return true; // ~ 5 Mbps
//    	case TelephonyManager.NETWORK_TYPE_HSPAP:
//    		return true; // ~ 10-20 Mbps
//    	case TelephonyManager.NETWORK_TYPE_IDEN:
//    		return false; // ~25 kbps
//    	case TelephonyManager.NETWORK_TYPE_LTE:
//    		return true; // ~ 10+ Mbps
//    	case TelephonyManager.NETWORK_TYPE_UNKNOWN:
//    		return false;
//    	default:
//    		return false;
//    	}
//    }
    
    public static String getIpAddress(Context context){
    	WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    	return ip;
    }

    private static final int CONNECTION_TYPE_NONE = -1;
    private static final int CONNECTION_TYPE_PLUS_FACTOR = 10;
    private static final int CONNECTION_TYPE_SHIFT_FACTOR = 100000;

    public static int getCustomNetworkType() {
        int type = CONNECTION_TYPE_NONE;
        ConnectivityManager connectivityManager = (ConnectivityManager)
                BaseContext.gContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (null != networkInfo) {
            type = networkInfo.getType();
            int subType = 0;
            if (type == ConnectivityManager.TYPE_MOBILE) {
                subType = networkInfo.getSubtype();
            }
            type = (type + CONNECTION_TYPE_PLUS_FACTOR) * CONNECTION_TYPE_SHIFT_FACTOR + subType;
        }
        return type;
    }
}
