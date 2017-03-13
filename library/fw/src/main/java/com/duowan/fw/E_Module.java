package com.duowan.fw;

/**
 * Design by JerryZhou@outlook.com, v4.0.0
 * */
public class E_Module implements E_Module_I{
	
	public static final E_Module ModuleRoot = new E_Module();

	@Override
	public String epath() {
		return mPath;
	}

	@Override
	public String name() {
		return mName;
	}
	
	@Override
	public Module module() {
		return mModule;
	}
	
	public String mPath;
	public String mName;
	public Module mModule;
	
	public E_Module(){
		mPath = ".";
		mName = "root";
		mModule = null;
	}
	
	public E_Module(String path){
		mPath = path; 
		mName = moduleName(path);
		mModule = null;
	}
	
	// register the module
	public synchronized void link(Module m){
		if(mModule == null){
			mModule = m;
			mModule.setName(mName);
			ModuleCenter.register(m, mPath);
		}
	}
	
	// add a generics return 
	public <T> T cast(Class<T> type){

		return type.cast(mModule);
	}
	
	// helper tools
	final public static String moduleName(String m){
		return m.substring(m.lastIndexOf('.')+1);
	}
	final public static String modulePath(String m){
		// String java.lang.String.substring(int start, int end)
		// Returns: a new string containing the characters from start to end - 1
		return m.substring(0, m.lastIndexOf('.'));
	}
	
	final public static String E_Register = "E_Register";
	final public static String E_UnRegister = "E_UnRegister";
	
	final public static String E_AddData = "E_AddData";
	final public static String E_RemoveData = "E_RemoveData";
	
	final public static String E_AddEventDelegate = "E_AddEventDelegate";
	final public static String E_RemoveEventDelegate = "E_RemoveEventDelegate";
	
	final public static String E_ModuleStart = "E_ModuleStart";
	final public static String E_ModuleUpdate = "E_ModuleUpdate";
	final public static String E_ModuleStop = "E_ModuleStop";

}
