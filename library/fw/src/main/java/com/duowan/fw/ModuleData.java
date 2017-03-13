package com.duowan.fw;

import java.lang.ref.WeakReference;

import com.duowan.fw.kvo.Kvo.KvoSource;

/**
 * Design by JerryZhou@outlook.com, v3.0.0
 * This is a public part in the framework, 
 * all field in ModuleData are maintained by module
 * */
public class ModuleData extends KvoSource{
	// data members
	protected String mName;
	protected WeakReference<Module> mModule;
	protected static final String DefaultName = "data";
	
	// default constructor
	public ModuleData(){
		mName = DefaultName;
		mModule = new WeakReference<Module>(null);
	}
	
	// should assign name in constructor
	public String getName(){
		return mName;
	}
	public void setName(String n){
		mName = n;
	}
	
	// used to communicate with the module
	public void setModule(Module m){
		mModule = new WeakReference<Module>(m);
	}
}
