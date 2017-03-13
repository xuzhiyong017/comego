package com.duowan.fw.util;

public class JFlag {
	
	public static long setFlag(long value, long flag){
		return value | flag;
	}
	
	public static long clearFlag(long value, long flag){
		return value & (~flag);
	}
	
	public static boolean isFlag(long value, long flag){
		return (value & flag) == flag;
	}

}
