package com.xuzhiyong.comego.module.net;

import com.duowan.fw.util.JTimeUtils;

public class NetTime {
	// 单位秒
	public static long ServerTime = 0l;
	public static long LocalTime = 0l;
	
	// 服务器心跳的时候调用
	public static void HeartBeat(long ts) {
		ServerTime = ts;
		LocalTime = JTimeUtils.getCurrentTime();
	}

	// 获取绝对的服务器心跳时间
	public static long GetServerTime() {
		return ServerTime;
	}
	
	// 获取服务器逻辑预测时间
	public static long GetLogicServerTime() {
		return ServerTime + (JTimeUtils.getCurrentTime() -  LocalTime);
	}
}
