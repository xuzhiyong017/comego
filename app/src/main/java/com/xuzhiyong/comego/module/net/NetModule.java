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
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.Login.LoginHelper;
import com.xuzhiyong.comego.module.Login.LoginInterface;
import com.xuzhiyong.comego.module.Login.LoginModuleData;
import com.xuzhiyong.comego.service.LocalService;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import protocol.PType;
import protocol.SPLogin;
import protocol.UserHeartBeatReq;
import protocol.UserHeartBeatRes;

public class NetModule extends Module implements NetInterface, NetModuleData.NetDataInterface {

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
    private NetClient mClient;

    private long mLastConnectPumpMillis;
    private List<Proto> mCacheRecvs;

    private final Object lockRecvCache = new Object();

    private AtomicBoolean mPumping = new AtomicBoolean(false);

    private boolean mHasBoundNetEvent;

    private long mDelayShowTick = 0;

    private NetModuleData mData;

    public NetModule(){
        mData = new NetModuleData();
        DData.netModuleData.link(this, mData);

        mLastConnectPumpMillis = System.currentTimeMillis();

        mCacheSends = new ConcurrentLinkedQueue<Proto>();
        mCacheRecvs = new LinkedList<Proto>();

        mProtoDispatcher.setEventDispatcher(new FwEvent.FwEventDispatcher(DNetDelegateDestination.BUILDER));

        DEvent.autoBindingEvent(this);

        Kvo.autoBindingTo(NetPing.np(), this);
    }

	@Override
	public void onData(NetDataChannel channel, Proto proto) {
		dealWithArrivedProto(proto);
	}

	private long mLogTick = 0;

	PumpRunnable mPumpRunnable = new PumpRunnable();
	class PumpRunnable implements Runnable {
		@Override
		public void run() {
			List<Proto> sendings = null;
			synchronized(lockRecvCache){
				if (!mCacheRecvs.isEmpty()) {
					sendings = mCacheRecvs;
					mCacheRecvs = new LinkedList<Proto>();
				}
			}

			mPumping.set(false);

			if (sendings != null && !sendings.isEmpty()  ) {
				Iterator<Proto> ite = sendings.iterator();
				while(ite.hasNext()) {
					Proto proto = ite.next();
					++mDealCacheSize;
					int uri = proto.getHead().getUri();
					mProtoDispatcher.sendEvent(uri, proto);
				}
			}
		}
	}

    @FwEventAnnotation(event=DEvent.E_ProtoArrivedLocalClient)
    public void onProtoArrivedLocalClient(FwEvent.EventArg event){
        Object[] args = FwEvent.EventArg.vars(event);
        Proto proto = (Proto)(args[0]);

        dealWithArrivedProto(proto);
    }

    private volatile int mRecvCacheSize = 0;
	private volatile int mDealCacheSize = 0;

    private void dealWithArrivedProto(final Proto proto) {
		mLastConnectPumpMillis = System.currentTimeMillis();

		++mRecvCacheSize;

		synchronized(lockRecvCache) {
			mCacheRecvs.add(proto);
		}

		if(mPumping.compareAndSet(false, true)){
			// post all proto to startup thread default
			ThreadBus.bus().post(ThreadBus.Net, mPumpRunnable);
		}

		long cur = System.currentTimeMillis();
		int xcachesize = mRecvCacheSize - mDealCacheSize;
		if (xcachesize  > 0 && cur - mLogTick >= 10000) { // 10s
			JLog.info(NetClient.KNet, "[Socket-Buffer] Socket Proto Recv: " + mRecvCacheSize
					+ " Deal: " + mDealCacheSize);
			mLogTick = cur;
		}
	}

	@Override
	public void onException(NetDataChannel channel, int error, IOException exception) {
		JLog.error(NetClient.KNet, String.format("net exception: error(%d) exception(%s)",
                error, exception.getMessage()));

		synchronized (mClientLocker) {
			if (mClient != null && channel != mClient.channel()) {
				JLog.error(NetClient.KNet, "NET EXCEPTION HAPPEND IN A GHOST CHANNEL");
				return;
			}
		}

		sendEvent(DEvent.E_NetBroken, error, channel);
	}

	@Override
    public void sendProto(Proto proto) {
        if(LoginHelper.isOnLine()){
            sendProtoDirect(proto);
        } else {
            if(!sExcludeCacheProto.contains(proto.head.getUri())) {
                if(OpenCacheLog){
                    JLog.info(NetClient.KNet, "[NET CACHE] Add: "+proto.head.toString());
                }
                if (proto.ts == 0) {
                    proto.ts = System.currentTimeMillis();
                }
                mCacheSends.add(proto);
            } else {
                if(OpenCacheLog){
                    JLog.info(NetClient.KNet, "[NET CACHE] Not Add: "+proto.head.toString());
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

    @KvoAnnotation(targetClass=NetPing.class, name=NetPing.Kvo_nping)
    public void onPingChange(Kvo.KvoEvent event) {
        // five miniute
        if(System.currentTimeMillis() - this.mDelayShowTick < 1*60*1000) {
            return;
        }
        // [0 ~ 300] [300 ~ 500] [500 ~ 1000] [1000 ~ ]
        long ping = event.caseNewValue(Long.class);
        if(ping < 300) {
            return;
        }
        this.mDelayShowTick = System.currentTimeMillis();

	    sendEvent(DEvent.E_NetPinSlow, ping);
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
						JLog.info(NetClient.KNet, "[NET CACHE] Sending: "+proto.head.toString());
					}
				}
			}

			if(!mCacheSends.isEmpty()) {
				mSendInterval = Math.min(Math.max(KMinSendInterval, KMaxSendCacheDis / 1024), KMaxSendInterval);
				ThreadBus.bus().postDelayed(ThreadBus.Net, this, mSendInterval);
				if(OpenCacheLog){
					JLog.debug(NetClient.KNet, "[NET CACHE]  caches: " + mCacheSends.size() + " With interval " + mSendInterval + " delays");
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
            JLog.debug(NetClient.KNet, "[NetSend:sendProtoDirect] %s", proto.toString());
        }

        synchronized (mClientLocker) {
            if (mClient != null) {
                mClient.sendProto(proto);
            }
        }

        long cur = System.currentTimeMillis();
        //心跳已经失效
        if (cur - mLastConnectPumpMillis > NetDataChannel.HeartbeatInterval * 2 // did not received a pump
                && LoginHelper.isOnLine() 				 		  // but the program did not throw any exception
                && JNetworkUtil.isNetworkAvailable()) {   // still we have a connected network

            mLastConnectPumpMillis = cur;

            JLog.error(NetClient.KNet, "some error happend to net connect: start a positive reconnect" +
                    ", net exception: error: -1, "
                    + "exception: do positive net request when do not recived " +
                    "any proto after 50s");

            DModule.ModuleLogin.cast(LoginInterface.class).setLoginState(LoginModuleData.LoginState.Login_Offline);

            //网络还可用，重登陆
            sendEvent(DEvent.E_OnNeedLogin);
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
    public void clearClient() {
	    synchronized (mClientLocker) {
		    if (mClient != null) {
			    // close the client in background thread
			    final NetClient toCloseClient = mClient;
			    new Thread() {

				    @Override
				    public void run() {
					    toCloseClient.close();
				    }
			    }.start();
			    mClient = null;
		    }
	    }
    }

    @Override
    public void setClient(NetClient client) {
	    synchronized (mClientLocker) {
		    if (client != mClient) {
			    if (mClient != null) {
				    // close the client in background thread
				    final NetClient toCloseClient = mClient;
				    new Thread() {

					    @Override
					    public void run() {
						    toCloseClient.close();
					    }

				    }.start();
				    mClient = null;
			    }
			    mClient = client;
		    }
	    }
    }

    @Override
    public NetClient newClient() {
	    return new NetClient(this);
    }

    @Override
    public void startHeartBeat() {
	    if (!mHasBoundNetEvent) {
		    NetHelper.autoBindingProto(this);
		    mHasBoundNetEvent = true;
	    }
	    stopHeartBeat();
	    scheduleHeartBeat();
    }

    @Override
    public void stopHeartBeat() {
	    JLog.info(NetClient.KNet, "stopHeartBeat");

	    Bundle extras = new Bundle();
	    extras.putInt(LocalService.LocalService_Op_Key,
			    LocalService.Local_Op_HeartBeat);
	    JPolling.stopPollingService(gMainContext, LocalService.class,
			    LocalService.ACTION, extras, LocalService.Local_Op_HeartBeat);
    }

    private void scheduleHeartBeat() {
        JLog.info(NetClient.KNet, "scheduleHeartBeat");

        long repeat = JTimeUtils.toMillis(15 * 60);
        Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTimeInMillis(System.currentTimeMillis() + repeat);

        Bundle extras = new Bundle();
        extras.putInt(LocalService.LocalService_Op_Key,
                LocalService.Local_Op_HeartBeat);

        JPolling.startTriggerService(gMainContext,
		        cur_cal.getTimeInMillis(), repeat,
		        LocalService.class, LocalService.ACTION, extras,
		        LocalService.Local_Op_HeartBeat);
    }

    @Override
    public void sendHeartBeat() {
        JLog.info(NetClient.KNet, "sendHeartBeat");

        PowerManager pm = (PowerManager) gMainContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "sendHeartBeat");
        wl.acquire();

        UserHeartBeatReq heartbeatReq = new UserHeartBeatReq.Builder()
                .flags(0)
                .currenttime(JTimeUtils.getCurrentTime())
                .build();

        Proto proto = NetHelper.buildProto(PType.PLogin,
                SPLogin.PUserHeartBeatReq,
                NetHelper.pbb().userHeartBeatReq(heartbeatReq).build());
        try {
            sendProto(proto);
        } finally {
            //TODO FIXME sendProto handled in socket write
            try {
                wl.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @DNetAnnoation(group=PType.PLogin_VALUE, sub= SPLogin.PUserHeartBeatRes_VALUE, thread= ThreadBus.Whatever)
    public void onHeartBeat(Proto proto){
        UserHeartBeatRes heartBeat = proto.getBody().userHeartBeatRes;

        JLog.info(NetClient.KNet, "onHeartBeat : " + heartBeat);
        // config the server time
        NetTime.HeartBeat(heartBeat.currenttime);

        sendEvent(DEvent.E_GiftListBeatHeart,heartBeat.giftListVersion == null ? 0L:heartBeat.giftListVersion);
    }
}
