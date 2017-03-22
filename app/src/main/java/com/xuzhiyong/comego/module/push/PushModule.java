package com.xuzhiyong.comego.module.push;


import com.duowan.fw.Module;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hydra on 2015/10/12.
 *
 */
public class PushModule extends Module implements PushInterface {



	public PushModule(){
		PushModuleData data = new PushModuleData();
		DData.pushModuleData.link(this, data);

		DEvent.autoBindingEvent(this);

// FIXME: 16-10-25
//		DLocalCommands.env().addLocalCommand("sessionchange", new DLocalCommands.LocalCommand() {
//
//			@Override
//			public Object dealWithCommand(DLocalCommands.DLocalCommandEnv env, String[] args,
//			                              Context context) {
//				JDb.post(new Runnable() {
//
//					@Override
//					public void run() {
//						sendEvent(DEvent.E_SessionChange);
//					}
//				}, 1000L);
//				return null;
//			}
//		});
	}

}
