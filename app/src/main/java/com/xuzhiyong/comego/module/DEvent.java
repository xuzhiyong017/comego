package com.xuzhiyong.comego.module;

import com.duowan.fw.FwEvent;

public class DEvent {

	public static final String E_AllModuleCreated = "E_AllModuleCreated";

	public static final String E_DB_Error_Report = "E_DB_Error_Report";

	/**
	 * When Activity State Changed
	 *
	 * @param Activity activity = arg.arg0(Activity.class);
	 * @param AppModuleData.ActivityState state = arg.arg1(AppModuleData.ActivityState.class);
	 */
	public static final String E_ActivityStateChanged = "E_ActivityStateChanged";

	/**
	 * When The Activity Have Got Or Lost Focus
	 *
	 * @Param Activity act = arg.arg0(Activity.class);
	 * @Param Boolean focus = arg.arg1(Boolean.class);
	 *
	 * */

	/**
	 * When the app have been enter foreground
	 */
	public static final String E_App_EnterForeground = "E_App_EnterForeground";

	public static final String E_NetBroken = "E_NetBroken";

	/**
	 * When the app have been enter background
	 */
	public static final String E_App_EnterBackground = "E_App_EnterBackground";

	public static final String E_ActivityFocusChanged = "E_ActivityFocusChanged";

	public static final String E_NetPinSlow = "E_NetPinSlow";

	public static final String E_LoginFailed = "E_LoginFailed";

	public static final String E_LoginSuccessful = "E_LoginSuccessful";

	public static final String E_StartAutoLogin = "E_StartAutoLogin";

    public static final String E_AppStartup = "E_AppStartup";

	public static final String E_OnNeedLogin = "E_OnNeedLogin";

	public static final String E_OnLogout = "E_OnLogout";

    public static final String E_ForceLogout = "E_ForceLogout";

	public static final String E_LoginTask_Successful = "E_LoginTask_Successful";
	public static final String E_LoginTask_Failed = "E_LoginTask_Failed";
	public static final String E_LoginTask_ActiveSuccessful = "E_LoginTask_ActiveSuccessful";
	public static final String E_LoginTask_ActiveFailed = "E_LoginTask_ActiveFailed";

	/**
	 * 登录过程中发生错误
	 */
	public static final String E_LoginError = "E_LoginError";

	//DataCenterModule
	public static final String E_DataCenter_UserDBChanged_Before = "E_DataCenter_UserDBChanged_Before";

	public static final String E_DataCenter_UserDBChanged = "E_DataCenter_UserDBChanged";

	public static final String E_DataCenter_UserDBChanged_After = "E_DataCenter_UserDBChanged_After";

	/**
	 * 这个事件的接收，是用于在同一次push中把增量更新以及内容都带上了
	 */
	public static final String E_ProtoArrivedLocalClient = "E_ProtoArrivedLocalClient";

	//UserModule
	public static final String E_UserChange_Before = "E_UserChange_Before";

	public static final String E_UserChange = "E_UserChange";

	public static final String Event_SwitchEnvTest = "SwitchEnvTest";

	public static final String Event_SwitchEnvOfficial = "SwitchEnvOfficial";






	// auto binding all annotation events
	public static final void autoBindingEvent(Object target) {
		FwEvent.autoBindingEvent(target);
	}

	// auto remove all annotation events
	public static final void autoRemoveEvent(Object target) {
		FwEvent.autoRemoveEvent(target);
	}
}
