package com.xuzhiyong.comego.module;

import com.duowan.fw.E_Data;

public class DData {

    /// app
    public static final E_Data appData = new E_Data(DModule.ModuleApp);

    /// analysis
    public static final E_Data analysisData = new E_Data(DModule.ModuleAnalysis);

	/// data center
	public static final E_Data dataCenterModuleData = new E_Data(DModule.ModuleDataCenter);

	/// net
	public static final E_Data netModuleData = new E_Data(DModule.ModuleNet);

    /// login
    public static final E_Data loginModuleData = new E_Data(DModule.ModuleLogin);

    /// push
	public static final E_Data pushModuleData = new E_Data(DModule.ModulePush);

	/// download
	public static final E_Data downloadData = new E_Data(DModule.ModuleDownload);

	/// update
	public static final E_Data updateModuleData = new E_Data(DModule.ModuleUpdate);

	/// bmob
	public static final E_Data bmobModuleData = new E_Data(DModule.ModuleBmob);

}
