package com.duowan.fw.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import java.util.Calendar;

public class JPolling {

	//开启轮询服务
	public static void startPollingService(Context context, int seconds, 
			Class<?> cls, String action, Bundle extras, int requestCode) {
		//获取AlarmManager系统服务
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		
		//包装需要执行Service的Intent
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		intent.putExtras(extras);
		PendingIntent pendingIntent = PendingIntent.getService(context, requestCode,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		//触发服务的起始时间
		long triggerAtTime = SystemClock.elapsedRealtime();
		
		//使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
				seconds * 1000, pendingIntent);
	}

	//停止轮询服务
	public static void stopPollingService(Context context, Class<?> cls, 
			String action, Bundle extras, int requestCode) {
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		intent.putExtras(extras);
		PendingIntent pendingIntent = PendingIntent.getService(context, requestCode,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//取消正在执行的服务
		manager.cancel(pendingIntent);
	}
	
	public static void startTriggerService(Context context, Long triggerAtTime, 
			Long repeat, Class<?> cls, String action, Bundle extras, int requestCode) {
		//获取AlarmManager系统服务
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		
		//包装需要执行Service的Intent
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		intent.putExtras(extras);
		PendingIntent pendingIntent = PendingIntent.getService(context, requestCode,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		//使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
		manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime,
				repeat, pendingIntent);
	}
	
	// get the minute
	public static long millisOf(int hourOfDay, int minute){
		Calendar setCal = Calendar.getInstance();  
        // 根据用户选择的时间设置定时器时间  
        setCal.set(Calendar.HOUR_OF_DAY, hourOfDay);  
        setCal.set(Calendar.MINUTE, minute); 
        return setCal.getTimeInMillis();
	}
}
