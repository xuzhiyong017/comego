package com.duowan.fw.kvo;

import java.util.HashMap;
import java.util.Map.Entry;

import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.util.JLog;

/**
 * Design by JerryZhou@outlook.com, v4.0.0
 * the stragetry is singleton module, every Name will be a singleton source tag,
 * every time a source is bind to target ,will remove the old source with same name
 * */
public class KvoBinder {
	private Object mTarget;
	private HashMap<String, KvoSource> mKvoSources = new HashMap<String, Kvo.KvoSource>();
	private static final boolean KC_OpenBinderLog = false;
	
	public KvoBinder(Object target){
		mTarget = target;
	}
	
	public boolean singleBindSourceToClassObj(KvoSource source){
		if (source != null) {
			return singleBindSourceTo(source.getClass().getSimpleName(), source);
		}
		return false;
	}
	
	public synchronized boolean singleBindSourceTo(String name, KvoSource source){
		KvoSource oldSource = mKvoSources.get(name);
		if (oldSource == source) { return false;
		}
		long ts;
		if (KC_OpenBinderLog) {
			ts = System.currentTimeMillis();
			JLog.info(this, "[KvoBinder] singleBindSourceTo BEGIN rebind: " + mTarget.getClass().getName() 
					+ " from:" + (oldSource!=null?oldSource.toString():"null") 
					+ " to:" + (source!=null?source.toString():"null")
						+ " ts:" + ts);	
		}
		
		if (oldSource != null) {
			Kvo.autoUnbindingFrom(oldSource, mTarget);
		}
		if (source != null) {
			Kvo.autoBindingTo(source, mTarget);
		}
		mKvoSources.put(name, source);
		
		if (KC_OpenBinderLog) {
			long ts_diff = System.currentTimeMillis() - ts; 
			JLog.info(this, "[KvoBinder] singleBindSourceTo END rebind" + " ts_diff:" + ts_diff);	
		}
		
		return true;
	}
	
	public synchronized void clearKvoConnection(String name) {
		KvoSource source = mKvoSources.remove(name);
		if(source != null) {
			Kvo.autoUnbindingFrom(source, mTarget);
		}
	}
	
	public synchronized void clearAllKvoConnections(){
		if (mKvoSources.size() > 0) {
			for(Entry<String, KvoSource> entry : mKvoSources.entrySet()) {
			    KvoSource value = entry.getValue();
			    if (value != null) {
					Kvo.autoUnbindingFrom(value, mTarget);
				}
			}
			mKvoSources.clear();
		}
	}
}
