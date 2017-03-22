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

	public static final String E_UserChange_After = "E_UserChange_After";

	public static final String E_EmptyUserInfo = "E_EmptyUserInfo";

	public static final String E_SessionChange = "E_SessionChange";

	public static final String E_HandleErrCode = "E_HandleErrCode";

	//TexasHoldemModule
	public static final String E_OnTexasGameSeatPush = "E_OnTexasGameSeatPush";
	public static final String E_OnTexasQueuePush = "E_OnTexasQueuePush";

	public static final String E_OnTexasJoinFailedOtherRoom = "E_OnTexasJoinFailedOtherRoom";

	public static final String E_OnTexasQueueEnterSuc = "E_OnTexasQueueEnterSuc";

    public static final String E_ChipsNotEnough = "E_ChipsNotEnough";

	public static final String E_LiveRoomEnterGroupFailed = "E_LiveRoomEnterGroupFailed";
	public static final String E_LiveRoomLoginYCloudFailed = "E_LiveRoomLoginYCloudFailed";
	public static final String E_LiveRoomLoginYCloudSuc = "E_LiveRoomLoginYCloudSuc";

	public static final String E_OnQuitLiveRoom = "E_OnQuitLiveRoom";

    public static final String E_ReportVideo = "E_ReportVideo";

	/**
	 *  开启摄像头失败
	 */
    public static final String E_OpenCameraFailed = "E_OpenCameraFailed";


    /**
     * 自己destroy自己的直播间
     */
	public static final String E_OnDestroyLiveRoom = "E_OnDestroyLiveRoom";
    /**
     * 直播间被destroy了
     */
    public static final String E_OnLiveRoomDestroyed = "E_OnLiveRoomDestroyed";

    public static final String E_GiftDialogShow = "E_GiftDialogShow";

	public static final String E_GiftListBeatHeart= "E_GiftListBeatHeart";

    public static final String E_GiftDialogDismiss = "E_GiftDialogDismiss";

	public static final String E_OnCameraPreviewCreated = "E_OnCameraPreviewCreated";

	public static final String E_OnCameraPreviewStopped = "E_OnCameraPreviewStopped";

    public static final String E_OnTexasResult = "E_OnTexasResult";

	public static final String E_OnVideoStreamArrived = "E_OnVideoStreamArrived";

	//group message
	public static final String E_GroupMessage_SendingResult = "E_GroupMessage_SendingResult";

    public static final String E_giftMsg_Arrived = "E_giftMsg_Arrived";

    public static final String E_OnRedPacket = "E_OnRedPacket";

    public static final String E_OnGiftSketchMsg = "E_OnGiftSketchMsg";

    public static final String E_OnValuableGiftMsg = "E_OnValuableGiftMsg";

	// user startLive
	public static final String E_UserStartLive = "E_UserStartLive";

	//top notify
	public static final String E_UserPrompt = "E_UserPrompt";

	public static final String E_CloseAnimSet = "E_close_animSet";

    public static final String E_OnUserNotice = "E_OnUserNotice";

    public static final String E_OnWithdrawCashAccountBind = "E_OnWithdrawCashAccountBind";

	public static final String E_OnTexasQueueUpdate = "E_OnTexasQueueUpdate";

    public static final String E_FinishActivity = "E_FinishActivity";

    public static final String E_ERR_GroupNotExist = "E_ERR_GroupNotExist";

	// auto binding all annotation events
	public static final void autoBindingEvent(Object target) {
		FwEvent.autoBindingEvent(target);
	}

	// auto remove all annotation events
	public static final void autoRemoveEvent(Object target) {
		FwEvent.autoRemoveEvent(target);
	}
}
