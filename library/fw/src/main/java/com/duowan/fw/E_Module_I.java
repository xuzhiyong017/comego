package com.duowan.fw;


/**
 * Design by JerryZhou@outlook.com, v4.0.0
 * */public interface E_Module_I {
	// the module from
	public abstract String epath();
	// the module name
	public abstract String name();
	// the related module
	public Module module();
	// register the module
	public abstract void link(Module m);
}
