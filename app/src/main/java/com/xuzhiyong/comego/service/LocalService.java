package com.xuzhiyong.comego.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.duowan.fw.util.JLog;


/**
 * Created by hydra on 2015/10/12.
 *
 */
public class LocalService extends Service {

    public static final String ACTION = "com.duowan.ada.service.LocalService";

    public static final String LocalService_Op_Key = "LocalService_Op_Key";

    public static final int Local_Op_HeartBeat = 0;
    public static final int Local_Op_CheckUpdate = 1;
	public static final int Local_Op_getLocation = 2;
	public static final int Local_Op_ClearCache = 3;
	public static final int Local_Op_ReportDeviceIdAndMac = 4;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        JLog.info(this, "LocalService ::onBind");
        return null;
    }

	@Override
	public boolean onUnbind(Intent intent) {
		JLog.info(this, "LocalService ::onUnbind");
		return false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Bundle extras = intent.getExtras();

		if (extras != null) {
			int op = extras.getInt(LocalService_Op_Key);

			JLog.info(this, "LocalService ::onStartCommand op : " + op);
// FIXME: 16-10-25
//			switch (op) {
//				case Local_Op_HeartBeat:
//					NetHelper.sendHeartBeat();
//					break;
//				case Local_Op_getLocation:
//					DModule.ModuleUser.cast(UserInterface.class).startLocation();
//					break;
//				case Local_Op_CheckUpdate:
//					DModule.ModuleUpdate.cast(UpdateInterface.class).updateVersionData(true);
//					break;
//				case Local_Op_ClearCache:
//					DModule.ModuleDataCenter.cast(DataCenterInterface.class).clearDataBase();
//					JFileUtils.clearOldCache();
//					break;
//				case Local_Op_ReportDeviceIdAndMac:
//					DModule.ModuleAnalysis.cast(AnalysisInterface.class).reportDeviceIdAndMac();
//					break;
//				default:
//					JLog.error(this, "we do not the local operator: %d", op);
//					break;
//			}
		}

		stopSelf();

		return START_NOT_STICKY;
	}
}
