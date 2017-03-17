package com.xuzhiyong.comego.ui.utils;

import android.text.TextUtils;
import android.text.format.Time;

import com.duowan.ada.R;
import com.duowan.fw.Module;
import com.duowan.fw.util.JLog;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeStamp {

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private int mSecond;
	
	//private static final String DATA_PATTERN = "(\\d+)-(\\d+)-(\\d+) (\\d+):(\\d+):(\\d+)";
	
	public static final String YEAR_MONTH_DAY_FORMAT = "%1$d-%2$02d-%3$02d";
	
	public static final String TODAY_FORMAT = "%1$02d:%2$02d";
	public static final String YESTERDAY_FORMAT = "%1$s %2$02d:%3$02d";
	public static final String YEAR_FORMAT = "%1$02d-%2$02d %3$02d:%4$02d";
	public static final String PRE_YEAR_FORMAT = "%1$d-%2$02d-%3$02d";
	public static final String MONTH_DAY_FORMAT = "%1$02d-%2$02d";
	public static final String HOUR_MINUTE_SECOND_FORMAT = "%1$02d:%2$02d:%3$02d";
	
	public TimeStamp(){}
	
	public TimeStamp(int year, int month, int day, int hour, int minute, int second){
		mYear = year;
		mMonth = month;
		mDay = day;
		mHour = hour;
		mMinute = minute;
		mSecond = second;
	}
	
    public static TimeStamp parseFromUnixTime(String x){
		Long l  = 0L;
		if (x != null && !x.isEmpty()) {
			try {
				l = Long.valueOf(x);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		return parseFromUnixTime(l);
	}
	
	public static TimeStamp parseFromUnixTime(long ux){// seconds
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(ux*1000);// seconds since Jan. 1, 1970
		TimeStamp ts = new TimeStamp();
		ts.mYear = calendar.get(Calendar.YEAR);
		ts.mMonth = calendar.get(Calendar.MONTH) + 1;
		ts.mDay = calendar.get(Calendar.DAY_OF_MONTH);
		ts.mHour = calendar.get(Calendar.HOUR_OF_DAY);
		ts.mSecond = calendar.get(Calendar.SECOND);
		ts.mMinute = calendar.get(Calendar.MINUTE);
		return ts;
	}
	
	public static String smartTimeStamp(String str){
		TimeStamp ts = parseFromUnixTime(str);
		if (ts!=null) {
			return ts.toSmartTime();
		}
		return "";
	}
	
	public static String smartTimeStamp(long lts){
		TimeStamp ts = parseFromUnixTime(lts);
		if (ts!=null) {
			return ts.toSmartTime();
		}
		return "";
	}

	public static String hourMinuteSecondFormat(long timeInSec) {
		long sec = timeInSec % 60;
		long minute = timeInSec / 60 % 60;
		long hour = timeInSec / (60 * 60);
		return String.format(Locale.getDefault(), HOUR_MINUTE_SECOND_FORMAT, hour, minute, sec);
	}
	
	public static String smartTimeStampInterval(String str){
		if(TextUtils.isEmpty(str)) {
			return "";
		}
		try {
			long lts = Long.valueOf(str);
			return smartTimeStampInterval(lts);
		} catch(Exception e) {
			JLog.error(null, "error time stamp : " + str);
			return "";
		}
	}

	public static String smartTimeStampInterval(long lts){
		long currentTs = System.currentTimeMillis()/1000;
		long interval = currentTs - lts;
		if(interval < 60) {
			return Module.gMainContext.getString(R.string.ts_just_before);
		} else if(interval < 60 * 60) {
			return interval / 60 + Module.gMainContext.getString(R.string.ts_minute_before);
		} else if(interval < 60 * 60 * 24) {
			return interval / (60 * 60) + Module.gMainContext.getString(R.string.ts_hour_before);
		} else if(interval < 60 * 60 * 24 * 30) {
			return interval / (60 * 60 * 24)  + Module.gMainContext.getString(R.string.ts_day_before);
		} else if(interval < 60 * 60 * 24 * 30 * 12) {
			return interval / (60 * 60 * 24 * 30)  + Module.gMainContext.getString(R.string.ts_month_before);
		} else {
			return interval / (60 * 60 * 24 * 30 * 12)  + Module.gMainContext.getString(R.string.ts_year_before);
		}
	}
	
	public static int compare(TimeStamp a, TimeStamp b){
		if (a.getYear() != b.getYear()){
			return a.getYear() - b.getYear();
		} else if (a.getMonth() != b.getMonth()){
			return a.getMonth() - b.getMonth();
		} else if (a.getDay() != b.getDay()){
			return a.getDay() - b.getDay();
		} else if (a.getHour() != b.getHour()){
			return a.getHour() - b.getHour();
		} else if (a.getMinute() != b.getMinute()){
			return a.getMinute() - b.getMinute();
		} else {
			return a.getSecond() - b.getSecond();
		}
	}
	
	public static String getMidDay(TimeStamp ts){
		if (ts.mHour < 12){
			return Module.gMainContext.getString(R.string.ts_a_m);
		} else if(ts.mHour < 18){
			return Module.gMainContext.getString(R.string.ts_p_m);
		} else {
			return Module.gMainContext.getString(R.string.ts_night);
		}
	}
	
	public static String getSmartTime(TimeStamp ts){
		Time today = new Time();
		today.setToNow();
		today.set(today.monthDay, today.month, today.year);
		
		Time time = new Time();
		time.set(ts.getDay(), ts.getMonth() - 1, ts.getYear());
		long dif = today.toMillis(true) - time.toMillis(true);
		
		if (dif <= 0 && TimeUnit.MILLISECONDS.toDays(-dif) < 1){
			return String.format(Locale.CHINA, TODAY_FORMAT, 
					ts.getHour(),
					ts.getMinute());
		} else if (TimeUnit.MILLISECONDS.toDays(dif) <= 1){
			String yesterday = Module.gMainContext.getString(R.string.ts_yesterday);
			return String.format(Locale.CHINA, YESTERDAY_FORMAT,
					yesterday,
					ts.getHour(),
					ts.getMinute());
		} else if (ts.getYear() == today.year){
			return String.format(Locale.CHINA, YEAR_FORMAT, 
					ts.getMonth(),
					ts.getDay(), 
					ts.getHour(), 
					ts.getMinute());
		} else {
			return String.format(Locale.CHINA, PRE_YEAR_FORMAT, 
					ts.getYear(),
					ts.getMonth(),
					ts.getDay());
		}
	}
	
	// str is the milliseconds since Jan. 1, 1970
	public static String getSmartTime(String str){
		return getSmartTime(TimeStamp.parseFromUnixTime(str));
	}
	
	public String toSmartTime() {
		return getSmartTime(this);
	}
	
	public void setYear(int year){
		mYear = year;
	}
	
	public int getYear(){
		return mYear;
	}
	
	public void setMonth(int month){
		mMonth = month;
	}
	
	public int getMonth(){
		return mMonth;
	}
	
	public void setDay(int day){
		mDay = day;
	}
	
	public int getDay(){
		return mDay;
	}

	public void setHour(int hour){
		mHour = hour;
	}
	
	public int getHour(){
		return mHour;
	}
	
	public void setMinute(int minute){
		mMinute = minute;
	}
	
	public int getMinute(){
		return mMinute;
	}
	
	public void setSecond(int second){
		mSecond = second;
	}
	
	public int getSecond(){
		return mSecond;
	}
}
