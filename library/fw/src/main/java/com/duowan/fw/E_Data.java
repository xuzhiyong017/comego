package com.duowan.fw;

/**
 * Design by JerryZhou@outlook.com, v4.0.0
 * */
public class E_Data implements E_Data_I {
	
	/// data members
	public String mName;
	public E_Module_I mEModule;
	public ModuleData mData;

	/// constructors
	public E_Data(String n, E_Module_I m){
		mName = n;
		mEModule = m;
	}
	public E_Data(E_Module_I m){
		mEModule = m;
		mName = m.name();
	}
	
	@Override
	public String dataName(){
		return mName;
	}
	
	@Override
	public String path(){
		return mEModule.epath();
	}
	
	// module
	public E_Module_I emodule(){
		return mEModule;
	}
	 
	// module data
	public synchronized ModuleData data(){
		if (mData == null) {
			mData = mEModule.module().lookupData(mName);
		}
		return mData;
	}
	
	// add a generics return 
	public <T extends ModuleData> T cast(Class<T> type){
		return type.cast(data());
	}

	// register the module
	public synchronized void link(Module module, ModuleData data){
		if(mData == null){
			mData = data;
			mData.setName(mName);
			module.addData(data);
		}
	}
}
