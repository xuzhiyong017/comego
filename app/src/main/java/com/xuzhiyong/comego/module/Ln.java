package com.xuzhiyong.comego.module;

//import android.text.TextUtils;
//import android.util.SparseArray;
//
//import com.duowan.fw.FwEvent;
//import com.duowan.fw.Module;
//import com.duowan.fw.ModuleCenter;
//import com.duowan.fw.ThreadBus;
//import com.duowan.fw.util.JLog;
//import com.duowan.fw.util.JNetworkUtil;
//import com.duowan.fw.util.JThreadUtil;
//import com.duowan.gamego.R;
//import com.duowan.gamego.module.net.NetClient;
//import com.duowan.gamego.ui.base.GToast;
//
//import protocol.ErrCode;
//import protocol.ProtoBody;
//import protocol.Result;

import protocol.ProtoBody;
import protocol.Result;

public class Ln {
	
//	private static SparseArray<Integer> sDefaultErrorMsgs;
//
//	private static void initDefaultErrorMsgs() {
//		if(sDefaultErrorMsgs != null) {
//			return;
//		}
//
//		sDefaultErrorMsgs = new SparseArray<>();
//
//		sDefaultErrorMsgs.put(ErrCode.ServerError.getValue(), R.string.exception_server_error);
//		sDefaultErrorMsgs.put(ErrCode.DatabaseError.getValue(), R.string.exception_server_databaseerror);
//
//		sDefaultErrorMsgs.put(NetClient.LocalErrCode_LoginFailed, R.string.exception_local_code_login_failed);
//		sDefaultErrorMsgs.put(NetClient.LocalErrCode_ActiveFailed, R.string.exception_local_code_active_failed);
//		sDefaultErrorMsgs.put(NetClient.LocalErrCode_ActiveSuccess, R.string.exception_local_code_active_success);
//		sDefaultErrorMsgs.put(NetClient.LocalErrCode_LoginNetErr, R.string.exception_local_code_login_net_error);
//	}
//
    public static void doDealWithException(Object object, String message, int errCode, ProtoBody protoBody, Result result) {
//    	initDefaultErrorMsgs();
//
//        String showMessage = message;
//
//        FwEvent.EventArg eventArg = FwEvent.EventArg.
//        		buildEventWithArg(object, DEvent.E_HandleErrCode, errCode, protoBody);
//        ModuleCenter.sendEvent(eventArg);
//
//        if (eventArg.haveDone()) {
//            return;
//        }
//
//        if (null == showMessage ) {
//            switch (errCode) {
//                default:
//                	Integer defaultMsgRes = sDefaultErrorMsgs.get(errCode);
//                	if(defaultMsgRes != null) {
//                		showMessage = Module.gMainContext.getString(defaultMsgRes);
//                	} else {
//                        if (!JNetworkUtil.isNetworkAvailable()) {
//                            showMessage = Module.gMainContext.getString(R.string.exception_net_problem);
//                            JLog.error(null, "unknown net error");
//                        } else {
//                            showMessage = Module.gMainContext.getString(R.string.exception_handle_failed);
//                        }
//                	}
//                    break;
//            }
//        }
//        if (null != result) {
//            switch (errCode) {
//                case ErrCode.GroupNotExist_VALUE:
//                    if (null != result.gid) {
//                        ModuleCenter.sendEventTo(DEvent.E_ERR_GroupNotExist, result.gid);
//                    }
//                    break;
//            }
//        }
//
//        GToast.show(showMessage);
//        //TODO:20160428
//
//        JLog.error(object, "Exception ErrCode = " + errCode);
    }
//
    public static void dealWithException(final Object object, final String message, final Result result, final ProtoBody protoBody) {
//    	final int code = result.code == null ? Result.DEFAULT_CODE.getValue()
//                : result.code.getValue();
//    	final String showMessage = TextUtils.isEmpty(message) ? result.reason : message;
//
//        if (JThreadUtil.isInMainThread()) {
//            doDealWithException(object, showMessage, code, protoBody, result);
//        } else {
//        	ThreadBus.bus().post(ThreadBus.Main, new Runnable() {
//
//                @Override
//                public void run() {
//                	doDealWithException(object, showMessage, code, protoBody, result);
//                }
//            });
//        }
    }
//
    public static void dealWithException(Object object, Result result) {
//        dealWithException(object, null, result, null);
    }
//
    public static void dealWithException(Object object, ProtoBody protoBody) {
//        dealWithException(object, null, protoBody.result, protoBody);
    }
}


