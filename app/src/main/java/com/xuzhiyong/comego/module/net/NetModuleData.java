package com.xuzhiyong.comego.module.net;

import com.duowan.fw.ModuleData;

import java.io.IOException;

public class NetModuleData extends ModuleData {

	public interface NetDataInterface{
		void onData(NetDataChannel channel, final Proto proto);
		void onException(NetDataChannel channel, int error, IOException exception);
	}
}
