package com.duowan.fw;


/**
 * Design by JerryZhou@outlook.com, v4.0.0
 * */public interface E_Data_I {
	// name
	public abstract String dataName();
	// path
	public abstract String path();
	// module
	public abstract E_Module_I emodule();
	// module data
	public abstract ModuleData data();
}
