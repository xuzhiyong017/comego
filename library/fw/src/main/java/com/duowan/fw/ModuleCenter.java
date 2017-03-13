package com.duowan.fw;

import android.app.Application;
import android.os.CountDownTimer;

import com.duowan.fw.util.JUtils;


/**
 * Design by JerryZhou@outlook.com, v3.0.0
 * This is a public adapter that will hide all module details from users.
 * We only care about what interfaces, events and moduledatas 
 * that be priveded by the modulecenter. 
 * */
public class ModuleCenter extends Module {
	// the update interval
	public static final long Interval = 10000;
	// data members
	private CountDownTimer mCDTimer = null;
	// the root module
	public ModuleCenter(){
		mName = "yy";
		// new the timer in main thread
		mCDTimer = new CountDownTimer(Long.MAX_VALUE, Interval){
			@Override
			public void onTick(long millisUntilFinished) {
				notifyEvent(E_Module.E_ModuleUpdate);
			}
			@Override
			public void onFinish() {
				JUtils.jAssert(false);
			}
		};
	}
	
	// start
	public void start(){
		if (mCDTimer != null){
			notifyEvent(E_Module.E_ModuleStart);
			
			mCDTimer.start();
		}
	}
	
	// start
	public void startWithContext(Application context){
		// set the evn
		gMainContext = context;
		// start all the module
		start();
	}
	
	// stop
	public void stop(){
		if (mCDTimer != null){
			notifyEvent(E_Module.E_ModuleStop);
			
			mCDTimer.cancel();
			mCDTimer = null;
		}
	}
	
	// singleton
	public static ModuleCenter gCenter;
	static{
		gCenter = new ModuleCenter();
		E_Module.ModuleRoot.link(gCenter);
	}
	
	// module manager
	public static Module findByPath(String path){
		String[] paths = path.split("\\.");
		if(paths.length > 1){
			Module parent = gCenter;
			for(int i=1; i<paths.length && parent != null; ++i){
				parent = parent.findModule(paths[i]);
			}
			return parent;
		}
		return gCenter;
	}
	
	public static boolean register(Module m, String path){
		Module parent = findByPath(path);
		if(parent != null){
			parent.register(m);
			return true;
		}
		return false;
	}
	
	public static boolean unRegister(String path){
		Module target = findByPath(path);
		if ( target != null){
			Module parent = findByPath(path.substring(0, path.lastIndexOf('.')));
			if ( parent != null && parent != target){
				parent.unRegister(target);
				return true;
			}
		}
		return false;
	}
	
	// event stuffs
	public static void addEventTo(Object name, Object target, String method){
		ModuleCenter.gCenter.addEvent(name, target, method);
	}
	
	public static void removeEventFrom(Object name, Object target, String method){
		ModuleCenter.gCenter.removeEvent(name, target, method);
	}

    public static void sendEvent(FwEvent.EventArg event) {
        ModuleCenter.gCenter.notifyEvent(event);
    }

	public static void sendEventTo(Object name, Object... args){
		ModuleCenter.gCenter.sendEvent(name, args);
	}

	public static void sendEventMainTo(Object evt, Object... args){
		ModuleCenter.gCenter.sendEventMain(evt, args);
	}
	
	// module data
	public static void addDataTo(ModuleData data, String path){
		Module target = findByPath(path);
		if(target != null){
			target.addData(data);
		}
	}
	public static void removeDataFrom(ModuleData data, String path){
		Module target = findByPath(path);
		if(target != null){
			target.removeData(data);
		}
	}
	public static ModuleData lookupData(String name, String path){
		Module target = findByPath(path);
		if(target != null){
			return target.lookupData(name);
		}
		return null;
	}
	public static ModuleData lookupData(E_Data_I d){
		ModuleData data = d.data();
		if (data != null) {
			return data;
		}
		
		Module target = d.emodule().module();
		if (target != null) {
			return target.lookupData(d.dataName());
		}else {
			return lookupData(d.dataName(), d.path());
		}
	}
}
