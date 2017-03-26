package com.xuzhiyong.comego.module.Login;

import android.text.TextUtils;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.net.DNetAnnoation;
import com.xuzhiyong.comego.module.net.NetHelper;
import com.xuzhiyong.comego.module.net.NetInterface;
import com.xuzhiyong.comego.module.net.NetRequest;
import com.xuzhiyong.comego.module.net.Proto;

import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import protocol.ErrCode;
import protocol.LoginBy;
import protocol.PType;
import protocol.SPLogin;

/**
 * Created by hydra on 2015/10/13.
 *
 */
public class LoginTask extends Thread {

    private static final String DISPATCH_SERVER_BASE_URL = "http://win.yy.com/";
    private static final String DISPATCH_SERVER_URL_PATH = "servers.json";

	private static AtomicInteger sLoginSeq = new AtomicInteger(0);

	private static LoginTask sCurrentLoginTask;

	private long mDispatchServerSid = 0;

	private AtomicBoolean mIsGetServerList = new AtomicBoolean(false);

	public static JLog.JLogModule sLog; // init by login module

	public static void startNewLoginTask(Proto proto) {
		if(sCurrentLoginTask != null) {
			NetHelper.autoRemoveProto(sCurrentLoginTask);
		}

		int newSeq = sLoginSeq.addAndGet(1);

		JLog.info(sLog, String.format(Locale.getDefault(), "[LOGIN] [0] start %d login: %s",
				newSeq, proto.toString()));

		sCurrentLoginTask = new LoginTask(newSeq, proto);
		sCurrentLoginTask.start();
	}

	/*********************Instance Field and Methods*******************************/

	private int mSeq;
	private Proto mLoginSendProto;

	private LoginTask(int seq, Proto proto) {
		mSeq = seq;
		mLoginSendProto = proto;

		NetHelper.autoBindingProto(this);
	}

	@DNetAnnoation(group= PType.PLogin_VALUE, sub= SPLogin.PUserLoginRes_VALUE, thread= ThreadBus.Whatever, order = 0)
	public void onLoginAck(Proto proto) {
		JLog.debug(sLog, String.format(Locale.getDefault(), "[LOGIN] Receive Ack %d, %d, %s",
				mSeq, sLoginSeq.get(), proto.toString()));
		if(mSeq == sLoginSeq.get()
				&& proto.body.result.code == ErrCode.Success) {
			DModule.ModuleDataCenter.module().sendEvent(DEvent
					.E_LoginTask_Successful, proto, mSeq);
		}
	}

	@Override
	public void run() {
		if (mSeq != sLoginSeq.get()) {
			JLog.error(sLog, "there is another new login thread is running");
			return;
		}


		DModule.ModuleNet.cast(NetInterface.class).sendProto(mLoginSendProto);

		if(mSeq == sLoginSeq.get()) {

		} else {
			JLog.error(sLog, "Login Failed invalid seq: " + mSeq + " currentLoginSeq: " + sLoginSeq.get());
		}
	}

}

