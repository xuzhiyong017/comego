package com.duowan.fw.kvo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.duowan.fw.bind.E_Property_I;
import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.util.JUtils;

/**
 * Design by JerryZhou@outlook.com, v4.0.0
 * [the stragetry is singleton module, every Name will be a singleton source tag,
 * every time a source is bind to target ,will remove the old source with same name]
 * The difference is source define, we group the propertys from multiple kvoSource as the abstract kvo Source
 * */
public class KvoBinderProperty{
	
	public void beginTarget(String name){
		JUtils.jAssert(mTargets.get(name) == null);
		JUtils.jAssert(mEditTarget == null);
		mEditTarget = allocPropertyTarget(name);
	}
	
	public void addToTarget(String kvoFieldName, Object target, E_Property_I property){
		JUtils.jAssert(mEditTarget != null);
		PropertyTargetItem item = new PropertyTargetItem();
		item.kvoFieldName = kvoFieldName;
		item.target = target;
		item.property = property;
		mEditTarget.items.add(item);
	}
	
	public void endTarget(){
		mEditTarget = null;
	}
	
	public void singleBindSourceTo(String name, KvoSource source, String target){
		JUtils.jAssert(mTargets.get(target) != null);
		PropertyTarget xTarget = allocPropertyTarget(target);
		sigleBindTo(name, source, xTarget);
	}
	
	public synchronized void clearAllKvoConnections(){
		if (mKvoSources.size() > 0 && mConnections.size() > 0) {
			for(Entry<String, KvoSource> entry : mKvoSources.entrySet()) {
			    KvoSource value = entry.getValue();
			    if (value != null) {
			    	ArrayList<PropertyTarget> targets = mConnections.get(entry.getKey());
			    	for (PropertyTarget target : targets) {
			    		for (PropertyTargetItem item : target.items) {
			    			KvoProperty.removeKvoBinding(value, item.kvoFieldName , item.target, item.property);
			    		}
			    	}	
				}
			}
			mKvoSources.clear();
			mConnections.clear();
			mTargets.clear();
		}
	}
	
	private synchronized PropertyTarget allocPropertyTarget(String name){
		PropertyTarget target = mTargets.get(name);
		if (target == null) {
			target = new PropertyTarget();
			target.name = name;
			mTargets.put(name, target);
		}
		return target;
	}
	
	private synchronized void sigleBindTo(String name, KvoSource source, PropertyTarget target){
		KvoSource oldSource = mKvoSources.get(name);
		if (oldSource == source) {
			return;
		}
		ArrayList<PropertyTarget> targets = mConnections.get(name);
		if (oldSource != null) {
			if (targets != null && targets.contains(target)) {
				for (PropertyTargetItem item : target.items) {
					KvoProperty.removeKvoBinding(oldSource, item.kvoFieldName , item.target, item.property);
				}
				targets.remove(target);
			}
		}
		if (source != null) {
			if (targets == null) {
				targets = new ArrayList<KvoBinderProperty.PropertyTarget>();
				mConnections.put(name, targets);
			}
			for (PropertyTargetItem item : target.items) {
				KvoProperty.addKvoBinding(source, item.kvoFieldName , item.target, item.property);
			}
			targets.add(target);
		}
		mKvoSources.put(name, source);	
	}
	
	public static class PropertyTargetItem{
		public String kvoFieldName;
		public Object target;
		public E_Property_I property;	
	}
	
	public static class PropertyTarget{
		public String name;
		public ArrayList<PropertyTargetItem> items;
	}
	
	private HashMap<String, ArrayList<PropertyTarget>> mConnections = new HashMap<String, ArrayList<PropertyTarget>>();
	private HashMap<String, KvoSource> mKvoSources = new HashMap<String, Kvo.KvoSource>();
	private HashMap<String, PropertyTarget> mTargets = new HashMap<String, KvoBinderProperty.PropertyTarget>();
	private PropertyTarget mEditTarget = null;
}
