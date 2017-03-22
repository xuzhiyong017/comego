package com.xuzhiyong.comego.module.Login;

import android.text.TextUtils;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JConfig;
import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.module.DConst;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.net.DNetAnnoation;
import com.xuzhiyong.comego.module.net.NetClient;
import com.xuzhiyong.comego.module.net.NetHelper;
import com.xuzhiyong.comego.module.net.NetInterface;
import com.xuzhiyong.comego.module.net.Proto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import protocol.ErrCode;
import protocol.LoginBy;
import protocol.PType;
import protocol.ProtoVersion;
import protocol.SPLogin;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by hydra on 2015/10/13.
 *
 */
public class LoginTask extends Thread {

    private static final String DISPATCH_SERVER_BASE_URL = "http://win.yy.com/";
    private static final String DISPATCH_SERVER_URL_PATH = "servers.json";
//	private static final String DISPATCH_SERVER_URL = "http://appsvr.g.yy.com/servers.json"
//			+ "?sid=%d&vr=%d&dt=%d&at=%d&fs=%s";
	//private static final String TEST_DISPATCH_SERVER_URL = "http://test.wasai.yy.com:10080/servers.json?sid=%d&vr=%d&dt=%d";

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

        NetClient client = DModule.ModuleNet.cast(NetInterface.class).newClient();
		DModule.ModuleNet.cast(NetInterface.class).setClient(client);

		mIsGetServerList.set(false);

		//获取分发服务器列表
		LoginBy loginBy = mLoginSendProto.getBody().userLoginReq.loginBy;
		if(loginBy != null) {
			getDispatchServerList(loginBy.getValue());
		} else {
			getDispatchServerList(0);
		}

		//保证同步
		for(;;) {
			if(mIsGetServerList.compareAndSet(true, false)) {
				break;
			}
		}

		InetSocketAddress socketAddress = NetClient.sISAAddress;

		if(socketAddress == null || TextUtils.isEmpty(socketAddress.getHostName())) {
			JLog.error(this, "NetClient socket Address is null");

			DModule.ModuleDataCenter.module().sendEvent(DEvent
					.E_LoginTask_Failed, NetClient.LocalErrCode_LoginFailed, mSeq);
			return;
		}

		String hostName = NetClient.sISAAddress.getHostName();

		// add a random choose address before connect
		NetClient.chooseBestAddress(NetClient.dispatchIps(hostName));

		int result = (int)(client.login(NetClient.sISAAddress,
				mLoginSendProto, NetClient.fallbackIps(hostName)));

		if(mSeq == sLoginSeq.get()) {
			//成功的事件在收到ACK时发
			if(result != ErrCode.Success_VALUE) {
				JLog.error(sLog, "Login Failed result: " + result + " seq: " + mSeq);

				DModule.ModuleNet.cast(NetInterface.class).clearClient();

				if (result == NetClient.LocalErrCode_LoginFailed ||
						result == NetClient.LocalErrCode_LoginNetErr
						|| result > 0) {
					DModule.ModuleDataCenter.module().sendEvent(DEvent
							.E_LoginTask_Failed, result, mSeq);
				}else if (result == NetClient.LocalErrCode_ActiveFailed) {
					DModule.ModuleDataCenter.module().sendEvent(DEvent
							.E_LoginTask_ActiveFailed, result, mSeq);
				}else if (result == NetClient.LocalErrCode_ActiveSuccess) {
					DModule.ModuleDataCenter.module().sendEvent(DEvent
							.E_LoginTask_ActiveSuccessful, result, mSeq);
				}
			}
		} else {
			JLog.error(sLog, "Login Failed invalid seq: " + mSeq + " currentLoginSeq: " + sLoginSeq.get());
		}
	}

    interface GetDispatchList {
        @GET(DISPATCH_SERVER_URL_PATH)
        Observable<ResponseBody> getList(@QueryMap Map<String, String> map);
    }

	public void getDispatchServerList(final int loginBy) {
        if(NetClient.sTestAddressStr.equals(JConfig.getString(NetClient.sAddressKey, NetClient.sAddressStr))) {
            JLog.debug(sLog, "no need get dispatch server list from %s", DISPATCH_SERVER_BASE_URL);
            //测试环境不走dispatchServer逻辑
            NetClient.fillDispatchIps(null);
            mIsGetServerList.set(true);
            return;
        }

        JLog.debug(sLog, "need get dispatch server list from %s", DISPATCH_SERVER_BASE_URL);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DISPATCH_SERVER_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        GetDispatchList getDispatchList = retrofit.create(GetDispatchList.class);
        Map<String, String> queryMap = new HashMap<>(5);
        queryMap.put("sid", String.valueOf(mDispatchServerSid));
        queryMap.put("vr", String.valueOf(ProtoVersion.ProtoVersion_Current_VALUE));
        queryMap.put("dt", String.valueOf(1));
        queryMap.put("at", String.valueOf(loginBy));
        queryMap.put("fs", DConst.sChannelID);
        getDispatchList.getList(queryMap).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        mIsGetServerList.set(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        NetClient.fillDispatchIps(null);
                        JLog.error(sLog, "get getDispatchServerList failed:" + e.getMessage());
                        mIsGetServerList.set(true);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            JSONObject js = new JSONObject(responseBody.string());
                            JSONArray iplist = js.getJSONArray("iplist");
                            ArrayList<String> ips = new ArrayList<>();
                            for(int i = 0; i < iplist.length(); i++) {
                                ips.add(iplist.getString(i));
                            }
                            NetClient.fillDispatchIps(ips);
                            mDispatchServerSid = js.getLong("sid");
                            JLog.debug(sLog, "success get dispatch server list %s in sid %d",
                                    NetClient.getIpString(ips), mDispatchServerSid);
                        } catch (Exception e) {
                            NetClient.fillDispatchIps(null);
                            JLog.error(sLog, "get getDispatchServerList failed:" + e.getMessage());
                        }
                    }
                });
	}
}
