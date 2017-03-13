package com.duowan.fw.util;

import com.duowan.fw.util.LogPiple.LogLevel;
import com.duowan.fw.util.LogPiple.WrapString;

/**
 * Design by JerryZhou@outlook.com, v3.0.0
 * */
// adapter to LogPiple
public class JLogObj {
	// log context
	public static LogPiple gLogPiple;
	
	// do some initialization
	static{
		// constructor
		gLogPiple = new LogPiple();
		// initialization
		LogPiple.WrapString defaultWrapString = new LogPiple.WrapString() {
			
			@Override
			public String content(Object obj) {
				return obj.toString();
			}
		};
		gLogPiple.wrap(String.class, defaultWrapString);
		gLogPiple.wrap(Object.class, defaultWrapString);
	}
	
	// log tag
	public static LogPiple tag(String tag){
		return gLogPiple.tag(tag);
	}
	
	// log level
	public static LogPiple level(LogLevel level){
		return gLogPiple.level(level);
	}
	
	// log wrap
	public static LogPiple wrap(Class<?> key, WrapString wrapString) {
		return gLogPiple.wrap(key, wrapString);
	}
	
	// log object
	public static <T extends Object>  LogPiple log(LogLevel level, T info){
		return gLogPiple.log(level, info);
	}
	
	public static <T extends Object> LogPiple log(T info){
		return gLogPiple.log(info);
	}
	
	// log content
	public static LogPiple logContent(LogLevel level, String content) {
		return gLogPiple.logContent(level, content);
	}
	public static LogPiple logContent(String content){
		return gLogPiple.logContent(content);
	}
}
