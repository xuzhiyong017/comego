package com.duowan.fw;

import java.util.HashMap;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.duowan.fw.FwEvent.EventArg;
import com.duowan.fw.FwEvent.FwEventDispatcher;
import com.duowan.fw.util.JFlag;
import com.duowan.fw.util.JThreadUtil;
import com.duowan.fw.util.JUtils;

/**
 * Design by JerryZhou@outlook.com, v3.0.0
 *  (public) ----- (private) --- (public)
 *   Data   -----   Module  --- ModuleCenter
 *   
 *   (Reason)   -----------      ( Result)
 *   Module --- drived the data to refresh ----- Data
 *   
 *   Data  --- (binding) --- UI
 *   
 *   Module --- (Event, Interface ) ----(adapter by ModuleCenter) --- biz and ui
 *   
 *   all the details are hidden in every little module from biz and ui
 *   kind of the mvc structor in android
 *   (data) - (ui) - (module and modulecenter)
 *    modle - view -     control
 * */
public class Module
{
	// global data
	public static Handler gMainHandler = new Handler(Looper.getMainLooper());
	public static long gMainThreadId = Looper.getMainLooper().getThread().getId();
	public static Application gMainContext = null;
	public static final String gDefaultName = "Module";
	
	// module data members
	protected String mName;
	protected HashMap<String, Module> mChildren;
	protected HashMap<String, ModuleData> mDatas;
	protected FwEventDispatcher mDispatcher = FwEvent.dispatcher;
	
	// flags
	public static final long SupportMetaEvent = (1 << 1);
	protected long mModuleFlag;
	
	// default constructor
	public Module(){
		mName = gDefaultName;
		mChildren = new HashMap<String, Module>();
		mDatas = new HashMap<String, ModuleData>();
		mModuleFlag = 0;
	}

	// should assign name in constructor
	public String getName(){
		return mName;
	}
	
	public void setName(String n){
		mName = n;
	}
	
	// children manager
	public void register(Module m){
		synchronized(mChildren){
			JUtils.jAssert(!mChildren.containsKey(m.getName()));
			mChildren.put(m.getName(), m);
		}
		if (JFlag.isFlag(mModuleFlag, SupportMetaEvent)) {
			notifyEvent(E_Module.E_Register, new Object[]{m});
		}
	}
	
	public void unRegister(Module m){
		synchronized(mChildren){
			JUtils.jAssert(mChildren.containsKey(m.getName()));
			mChildren.remove(m.getName());
		}
		if (JFlag.isFlag(mModuleFlag, SupportMetaEvent)) {
			notifyEvent(E_Module.E_UnRegister, new Object[]{m});
		}
	}
	
	public Module findModule(String name){
		return mChildren.get(name);	
	}
	
	// data manager
	public void addData(ModuleData data){
		JUtils.jAssert(!mDatas.containsKey(data.getName()));
		mDatas.put(data.getName(), data);
		data.setModule(this);
		
		if (JFlag.isFlag(mModuleFlag, SupportMetaEvent)) {
			notifyEvent(E_Module.E_AddData, new Object[]{data});
		}
	}
	public void removeData(ModuleData data){
		JUtils.jAssert(mDatas.containsKey(data.getName()));
		mDatas.remove(data.getName());
		data.setModule(null);
		
		if (JFlag.isFlag(mModuleFlag, SupportMetaEvent)) {
			notifyEvent(E_Module.E_RemoveData, new Object[]{data});
		}
	}
	public boolean containsData(ModuleData data){
		return mDatas.containsKey(data.getName());
	}
	public ModuleData lookupData(String name){
		return mDatas.get(name);
	}

	// event delegates
	public void sendEvent(Object evt, Object... args){
		notifyEvent(evt, args);
	}
	
	public void sendEventMain(Object evt, Object... args) {
		final Object evt_ = evt;
		final Object[] args_ = args;
		// to main loop
		JThreadUtil.runMainThread(new Runnable(){
			@Override
			public void run() {
				sendEvent(evt_, args_);
			}
		});
	}
	
	public void addEventDelegate(Object evt, Object obj, String n){
		addEvent(evt, obj, n);
		if (JFlag.isFlag(mModuleFlag, SupportMetaEvent)) {
			notifyEvent(E_Module.E_AddEventDelegate, new Object[]{evt, obj, n});
		}
	}
	
	public void removeEventDelegate(Object evt, Object obj, String n){
		removeEvent(evt, obj, n);
		if (JFlag.isFlag(mModuleFlag, SupportMetaEvent)) {
			notifyEvent(E_Module.E_RemoveEventDelegate, evt, obj, n);
		}
	}
	
	public static void runAsync(Runnable r) {
		gMainHandler.post(r);
	}
	public static void runAsyncDelayed(Runnable r, long delayMillis){
		gMainHandler.postDelayed(r, delayMillis);
	}
	public static void removeAsyncRunnale(Runnable r){
		gMainHandler.removeCallbacks(r);
	}

    public void notifyEvent(EventArg event) {
        mDispatcher.notifyEvent(event);
    }

	public void notifyEvent(Object name, Object ...args){
		EventArg event = EventArg.buildEventWithArg(this, name, args);
		mDispatcher.notifyEvent(event);
	}
	public void notifyEventWithArgs(Object name, HashMap<String, Object> args){
		EventArg event = new EventArg();
		event.event = name;
		event.source = this;
		event.args = args;
		mDispatcher.notifyEvent(event);
	}
	
	public void addEvent(Object name, Object target, String method){
		mDispatcher.addBinding(name, target, method);
	}
	
	public void removeEvent(Object name, Object target, String method){
		mDispatcher.removeBinding(name, target, method);
	}
	
	public void setEventDispatcher(FwEventDispatcher dispatcher){
		mDispatcher = dispatcher;
	}
	
	public FwEventDispatcher dispatcher(){
		return mDispatcher;
	}
}
