package com.duowan.fw.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentHashMap;

public class JDayDayUp {
	private ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>> mDaydayUpCache = 
			new ConcurrentHashMap<Long, ConcurrentHashMap<String,Object>>();
	
	public Object objectForKey(String key){
		long curDay = day(0);
		return objectForKey(key, curDay);
	}
	
	public Object objectForKey(String key, long day){
		ConcurrentHashMap<String, Object> dayUps = mDaydayUpCache.get(day);
		if (dayUps != null) {
			return dayUps.get(key);
		}
		return null;	
	}
	
	public boolean hasDone(String key){
		long curDay = day(0);
		return hasDone(key, curDay);
	}
	
	public boolean hasDone(String key, long day){
		return objectForKey(key, day) != null;
	}
	
	public void done(String key, Object object){
		long curDay = day(0);
		done(key, object, curDay);
	}
	
	public void done(String key, Object object, long day){
		ConcurrentHashMap<String, Object> dayUps = mDaydayUpCache.get(day);
		if (dayUps == null) {
			dayUps = new ConcurrentHashMap<String, Object>();
			mDaydayUpCache.put(day, dayUps);
		}
		dayUps.put(key, object);
	}
	
	public void clear(){
		mDaydayUpCache.clear();
	}
	
	public int size(){
		return mDaydayUpCache.size();
	}
	
	public boolean isEmpty(){
		return mDaydayUpCache.isEmpty();
	}
	
	static public long day(long offset){
		GregorianCalendar calendar = new GregorianCalendar();
		long day = calendar.get(Calendar.YEAR) * 365 + calendar.get(Calendar.DAY_OF_YEAR);
		return day + offset;
	}
}
