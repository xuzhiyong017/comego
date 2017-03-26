package com.xuzhiyong.comego.module.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;

import com.duowan.fw.FwEvent;
import com.duowan.fw.FwEventAnnotation;
import com.duowan.fw.Module;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.kvo.Kvo;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JNetworkUtil;
import com.duowan.fw.util.JPolling;
import com.duowan.fw.util.JTimeUtils;
import com.google.gson.Gson;
import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.Login.LoginHelper;
import com.xuzhiyong.comego.module.Login.LoginInterface;
import com.xuzhiyong.comego.module.Login.LoginModuleData;
import com.xuzhiyong.comego.service.LocalService;
import com.xuzhiyong.comego.ui.base.GToast;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NetModule extends Module implements NetInterface {

    private static HashSet<Integer> sExcludeCacheProto = new HashSet<>();
    private static HashSet<Integer> sForceReloginProto = new HashSet<>();

    static {
	    // TODO: 2016/3/29 not cached protos
//        sExcludeCacheProto.add(NetHelper.makeUri(PType.PGroupMsg_VALUE,
//                SPGroupMsg.PGroupMsgSendReq_VALUE));
//        sExcludeCacheProto.add(NetHelper.makeUri(PType.PUserMsg_VALUE,
//                SPUserMsg.PUserMsgSendReq_VALUE));
//        sExcludeCacheProto.add(NetHelper.makeUri(PType.PUser_VALUE,
//                SPUser.PUserInfoModifyReq_VALUE));
//        sExcludeCacheProto.add(NetHelper.makeUri(PType.PTexas_VALUE,
//                SPTexas.PTexasQueueQueryReq_VALUE));
//        sExcludeCacheProto.add(NetHelper.makeUri(PType.PGift_VALUE,
//                SPGift.PGiftDonateReq_VALUE));
//        sForceReloginProto.add(NetHelper.makeUri(PType.PUser_VALUE,
//                SPUser.PUserSearchReq_VALUE));
    }

    // log
    public static JLog.JLogModule KNet;

    // init
    static {
        KNet = JLog.JLogModule.makeOne("Net");
    }


    private static final long KMaxSendCacheDis = 1000L;
	private static final long KMaxSendInterval = 100L;
	private static final long KMinSendInterval = 10l;
	private static final long KSendingTimeout = 1 * 60 * 1000; //  1 minute

	private static final boolean OpenCacheLog = false;

    private Module mProtoDispatcher = new Module();

    private BroadcastReceiver mReceiver = null;

    // may clear when user changed
    private ConcurrentLinkedQueue<Proto> mCacheSends;

    private AtomicBoolean mSending = new AtomicBoolean(false);

    private long mSendInterval = KMaxSendInterval;

    private Object mClientLocker = new Object();

    private long mLastConnectPumpMillis;
    private List<Proto> mCacheRecvs;

    private final Object lockRecvCache = new Object();

    private NetModuleData mData;

    private Gson mGson;

    public NetModule(){
        mData = new NetModuleData();
        DData.netModuleData.link(this, mData);

        mLastConnectPumpMillis = System.currentTimeMillis();

        mCacheSends = new ConcurrentLinkedQueue<Proto>();
        mCacheRecvs = new LinkedList<Proto>();
        mGson = new Gson();

        mProtoDispatcher.setEventDispatcher(new FwEvent.FwEventDispatcher(DNetDelegateDestination.BUILDER));

        DEvent.autoBindingEvent(this);

       NetAddressChooser.initRetrofit();

    }



    @FwEventAnnotation(event = DEvent.Event_SwitchEnvTest)
    public void onSwitchEnvironmentTest(FwEvent.EventArg eventArg) {
        NetAddressChooser.switchEnvTest();
        GToast.show(R.string.switch_env_test);
    }

    @FwEventAnnotation(event = DEvent.Event_SwitchEnvOfficial)
    public void onSwitchEnvironmentOfficial(FwEvent.EventArg eventArg) {
        NetAddressChooser.switchEnvOfficial();
        GToast.show(R.string.switch_env_official);
    }



	@Override
    public void sendProto(Proto proto) {
        if(LoginHelper.isOnLine() || LoginHelper.isLoginIng()){
            sendProtoDirect(proto);
        } else {
            if(!sExcludeCacheProto.contains(proto.head.getUri())) {
                if(OpenCacheLog){
                    JLog.info(NetModule.KNet, "[NET CACHE] Add: "+proto.head.toString());
                }
                if (proto.ts == 0) {
                    proto.ts = System.currentTimeMillis();
                }
                mCacheSends.add(proto);
            } else {
                if(OpenCacheLog){
                    JLog.info(NetModule.KNet, "[NET CACHE] Not Add: "+proto.head.toString());
                }
            }
            if (sForceReloginProto.contains(proto.head.getUri())
                    && JNetworkUtil.isNetworkAvailable()
                    && LoginHelper.loginState() == LoginModuleData.LoginState.Login_Offline.ordinal()) {
                LoginHelper.login();
            }
            mSending.set(false);
        }
    }


	SendingRunnable mSendRunnable = new SendingRunnable();
	class SendingRunnable implements Runnable {

		@Override
		public void run() {
			if (mSending.get() == false) {
				return;
			}
			Proto proto = mCacheSends.poll();
			if (proto != null ) {
				if(System.currentTimeMillis() - proto.ts < KSendingTimeout) {
					NetModule.this.sendProto(proto);

					if(OpenCacheLog) {
						JLog.info(NetModule.KNet, "[NET CACHE] Sending: "+proto.head.toString());
					}
				}
			}

			if(!mCacheSends.isEmpty()) {
				mSendInterval = Math.min(Math.max(KMinSendInterval, KMaxSendCacheDis / 1024), KMaxSendInterval);
				ThreadBus.bus().postDelayed(ThreadBus.Net, this, mSendInterval);
				if(OpenCacheLog){
					JLog.debug(NetModule.KNet, "[NET CACHE]  caches: " + mCacheSends.size() + " With interval " + mSendInterval + " delays");
				}
			}else {
				mSending.set(false);
			}
		}
	}

    @FwEventAnnotation(event=DEvent.E_LoginSuccessful)
    public void onLoginSuccessful(FwEvent.EventArg event){
        if (!mCacheSends.isEmpty() && mSending.compareAndSet(false, true)) {
            ThreadBus.bus().postDelayed(ThreadBus.Net, mSendRunnable, mSendInterval);
        }
    }

    @Override
    public void sendProtoDirect(Proto proto) {
        if(proto.head.seq < 100000) {
            JLog.debug(NetModule.KNet, "[NetSend:sendProtoDirect] %s", proto.toString());
        }

        synchronized (mClientLocker) {
            internalSendProto(NetAddressChooser.getUrl(), proto);
        }

    }



    @Override
	public FwEvent.FwEventDispatcher netDispatcher(){
		return mProtoDispatcher.dispatcher();
	}

	@Override
	public void addProtoDelegate(Integer uri, Object obj, String n){
        mProtoDispatcher.dispatcher().addBinding(uri, obj, n);
	}

	@Override
	public void removeProtoDelegate(Integer uri, Object obj, String n){
        mProtoDispatcher.dispatcher().removeBinding(uri, obj, n);
	}

    @Override
    public void switchEnvironment() {
        if (NetAddressChooser.isInTestEnvironment()) {
            onSwitchEnvironmentOfficial(null);
        } else {
            onSwitchEnvironmentTest(null);
        }
    }

    @Override
    public void sendProtoWithToken( Proto proto) {
        internalSendProtoWithToken(proto);
    }

    private void internalSendProtoWithToken(Proto proto) {
    }

    private void internalSendProto(String url, Proto proto) {
        RetrofitService service = NetAddressChooser.getRetrofit().create(RetrofitService.class);
        String json = mGson.toJson(proto);
        service.exchangeProto(json)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Proto>() {
                    @Override
                    public void call(Proto proto) {
                        onProto(proto);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        JLog.error("GameGoConfig", "get AppConfig failed:" + throwable.getMessage());
                        GToast.show(throwable.getMessage());
                    }
                });

    }

    void onProto(Proto proto) {
        int uri = NetHelper.makeUri(proto.head.group,proto.head.sub);
        mProtoDispatcher.sendEvent(uri, proto);
    }
}
