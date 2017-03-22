package com.xuzhiyong.comego.module.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.duowan.fw.util.JNetworkUtil;

/**
 * Created by hydra on 2015/11/12.
 *
 */
public class NetStateBroadcastReceiver extends BroadcastReceiver {

	private static boolean sNetworkAvailable = JNetworkUtil.isNetworkAvailable();

    @Override
    public void onReceive(Context context, Intent intent) {
	    //在NetModule里动态注册，会收不到通知
	    boolean available = JNetworkUtil.isNetworkAvailable();
// FIXME: 16-10-25
//		if(!available){
//			NetWorkEvent.notifyEvent(this,NetWorkEvent.NetWorkEvent_no_network_disconnect);
//		}else{
//			NetWorkEvent.notifyEvent(this,NetWorkEvent.NetWorkEvent_no_network_connect);
//		}
//	    if(sNetworkAvailable != available) {
//		    sNetworkAvailable = available;
//		    DModule.ModuleLogin.cast(LoginInterface.class).changeNetValiable(available);
//	    }
    }
}
