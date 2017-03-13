package com.duowan.fw.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;

public class LogPiple {
	// control the log level
	public static enum LogLevel{
		verbose,
		info,
		debug,
		warning,
		error,
	}
	
	// we can use 
	public interface WrapString{
		public String content(Object obj);
	}
	
	// data members
	private String mLogTag = "LogPiple";
	private LogLevel mAcceptLevel = LogLevel.verbose;
	private LogLevel mCurrentLevel = LogLevel.verbose;
	private Map<Class<?>, WrapString> mPiples = new ConcurrentHashMap<Class<?>, LogPiple.WrapString>();
	
	// log tag
	public synchronized LogPiple tag(String tag){
		mLogTag = tag;
		return this;
	}
	
	// log level
	public synchronized LogPiple level(LogLevel level){
		mCurrentLevel = level;
		return this;
	}
	
	// log wrap
	public LogPiple wrap(Class<?> key, WrapString wrapString) {
		mPiples.put(key, wrapString);
		return this;
	}
	
	// log object
	public <T extends Object> LogPiple log(LogLevel level, T info){
		if (isAccept(level)) {
			WrapString wrap = getWrapFromObject(info);
			return logReal(level, wrap.content(info));
		}
			
		return this;
	}
	
	public <T extends Object> LogPiple log(T info){
		return log(mCurrentLevel, info);
	}
	
	// log content
	public LogPiple logContent(LogLevel level, String content) {
		if (isAccept(level)) {
			return logReal(level, content);
		}
		return this;
	}
	public LogPiple logContent(String content){
		return logContent(mCurrentLevel, content);
	}
	
	// log real
	private LogPiple logReal(LogLevel level, String msg){
		if(level == LogLevel.verbose){
			Log.e(mLogTag, msg);
		}else if (level == LogLevel.info) {
			Log.i(mLogTag, msg);
		}else if (level == LogLevel.debug) {
			Log.d(mLogTag, msg);
		}else if (level == LogLevel.warning) {
			Log.w(mLogTag, msg);
		}else if (level == LogLevel.error) {
			Log.e(mLogTag, msg);
		}
		return this;
	}
	
	// is the log level grater than we accept level
	private boolean isAccept(LogLevel level){
		if (level.ordinal() >= mAcceptLevel.ordinal()) {
			return true;
		}
		return false;
	}
	
	// generic method to get the wrap
	private<T> WrapString getWrapFromObject(T info){
		return getWrap(info.getClass());
	}
	
	// visit the hash map, get the wrap
	private WrapString getWrap(Class<?> keyClass){
		Class<?> curKeyClass = keyClass;
		WrapString wrapInterface = null;
		do {
			wrapInterface = mPiples.get(curKeyClass);
			/* Returns the Class object which represents the superclass of the class represented by this Class. 
			 * If this Class represents the Object class, a primitive type, 
			 * an interface or void then the method returns null. 
			 * If this Class represents an array class then the Object class is returned.
			 * */
			curKeyClass = curKeyClass.getSuperclass();
		} while (wrapInterface == null && curKeyClass != Object.class && curKeyClass != null);
		
		if(wrapInterface == null){
			return new WrapString() {
				
				@Override
				public String content(Object obj) {
					return obj.toString();
				}
			}; 
		}
		return wrapInterface;
	}
}
