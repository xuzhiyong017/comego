package com.xuzhiyong.comego.module.net;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JUtils;
import com.squareup.wire.ProtoEnum;
import com.xuzhiyong.comego.module.DModule;

import java.lang.ref.WeakReference;

import protocol.ProtoBody;

public class NetRequest implements Runnable{
	// shut log trace
	private static boolean trackNetRequestLog = false;
	
	/// request thread
	private static final int sRequestThread = ThreadBus.gen();
	public static final int FLAG_WITH_COOKIE = 0x01;
	public static final int FLAG_WITH_TOKEN = 0x02;
	public static final int FLAG_SEND = 0x03;
	/// static block
	static{
		ThreadBus.bus().addThread(sRequestThread, "net-request-thread");
	}
	
	/// data members
	private int mGroup = -1;          		/*protocol group*/
	private int mReqSub = -1;				/*request sub*/
	private int mResSub = -1;				/*respond sub*/
	private ProtoBody mMessage;				/*body message*/
	private ProtoHandler 		mHandler;				/*respond handler*/
	private long				mTimeOut = -1;			/*time out in millis*/
	private Runnable 			mDelayRunnable = null;	/*used to caculator time out*/
	private Proto 				mProto;
	// net sequence, every same proto uri must not have same seq, resverd seq 0 for internal protocol
	private int 				mSeq = -1;

	private int mFlag = FLAG_SEND;
	
	public NetRequest(ProtoEnum group, ProtoEnum reqSub, ProtoBody message){
		mGroup = group.getValue();
		mReqSub = reqSub.getValue();
		mMessage = message;
	}
	
	public NetRequest(int group, int reqSub, int resSub, ProtoBody message){
		mGroup = group;
		mReqSub = reqSub;
		mResSub = resSub;
		mMessage = message;
	}
	
	public NetRequest(Proto proto){
		mProto = proto;
	}
	
	public NetRequest(){
	}
	
	public NetRequest setGroup(ProtoEnum group){
		mGroup = group.getValue();
		return this;
	}
	
	public NetRequest setResSub(ProtoEnum sub){
		mResSub = sub.getValue();
		return this;
	}
	
	public NetRequest setReqSub(ProtoEnum sub){
		mReqSub = sub.getValue();
		return this;
	}
	
	public NetRequest setMessage(ProtoBody message){
		mMessage = message;
		return this;
	}
	
	public NetRequest setHandler(ProtoHandler handler){
		mHandler = handler;
		return this;
	}
	
	public NetRequest setTimeOut(long timeOut){
		mTimeOut = timeOut;
		return this;
	}
	
	public NetRequest setProto(Proto proto){
		mProto = proto;
		return this;
	}

	public NetRequest setFlag(int flag){
		mFlag = flag;
		return this;
	}

	public  NetRequest request(){
		return request(null);
	}

	public NetRequest request(NetTracker tracker) {
		if (tracker != null) {
			if (mProto == null) {
				mProto = NetHelper.buildProto(mGroup, mReqSub, mMessage);
			}
			tracker.track(mProto);
		}

		ThreadBus.bus().post(sRequestThread, this);
		return this;
	}
	
	/// delay runnable
	public static class DelayRunnable implements Runnable{
		NetRequest mBuilder;
		
		public DelayRunnable(NetRequest builder){
			mBuilder = builder;
		}

		@Override
		public void run() {
			mBuilder.onTimeOut();
			mBuilder = null;
		}
	}

	// handler
	@Override
	public void run() {
		//listen uri if need
		if (mResSub != -1){
			JUtils.jAssert(mTimeOut > 0);
			NetHelper.addProtoDelegate(NetHelper.makeUri(mGroup, mResSub), this, RespondMethod);
			// keep runnable
			NetHelper.addNetRequestRunnables(this);

		}
		// send proto
		if (mProto == null) {
			mProto = NetHelper.buildProto(mGroup, mReqSub, mMessage);
			mSeq = mProto.head.seq;
		}else{
			if (mProto.getHead().seq > 0) {
				mSeq = mProto.getHead().seq;
			}else {
				mSeq = NetHelper.allocSeq();
				mProto.getHead().seq = mSeq; 	
			}
		}

		sendProto(mProto);
		if (trackNetRequestLog) {
			// we did not receive the respond yet, but the max seq is coming
			JLog.info(this, String.format("[NetDataChannel][NetRequest] send a proto(%s) with seq(%d) local seq(%d)",
                    mProto.getHead().toString(), mProto.getHead().getSeq(), mSeq));
		}

		// add a timeout logic
		if (mTimeOut > 0) {
			JUtils.jAssert(mResSub != -1);
			if (mResSub == -1) {
				JLog.error(this, "must set the respond sub protocol when ask for timeout: " + mProto.toString());
			}
			mDelayRunnable = new DelayRunnable(this);
            ThreadBus.bus().postDelayed(ThreadBus.Main, mDelayRunnable, mTimeOut);
		}
	}


	private void sendProto(Proto proto) {
		switch (mFlag) {
			case FLAG_WITH_COOKIE:
			case FLAG_WITH_TOKEN:
				NetHelper.sendProtoWithToken(proto);
				break;
			case FLAG_SEND:
				NetHelper.sendProto(proto);
				break;
			default:
				NetHelper.sendProto(proto);
				break;
		}
	}


	public static final String RespondMethod = "onRespond";
    @DNetAnnoation(group = 0, sub = 0)// make the method not mix by proguard
	public void onRespond(Proto proto){
		if (proto.getHead().getSeq() == mSeq) {
			// clear first
			clear(false);
			// deal respond
			if (mHandler != null) {
				mHandler.onRespond(proto);
			}
		}else {
			// fake proto send by self
			if (proto.getHead().getSeq() == 0) {
				return;
			}

			// no time out, so we need to clear this
			if (mTimeOut <= 0) {
				// we did not receive the respond yet, but the max seq is coming
				JLog.error(this, String.format("do receive a proto(%s) with wrong seq(%d) local seq(%d) and from seq(%d)",
                        proto.getHead().toString(), proto.getHead().getSeq(), mSeq, mProto.getHead().getSeq()));
				clear(true);	
			}
		}
	}
	
	public void onTimeOut(){
		NetHelper.removeProtoDelegate(NetHelper.makeUri(mGroup, mResSub), this, RespondMethod);
		if (mHandler != null) {
			// we did not receive the respond yet, but the time out timer is coming
			JLog.error(this, String.format("time out to receive a proto(%s) respond local seq(%d) and from seq(%d)",
                    mProto.getHead().toString(), mSeq, mProto.getHead().getSeq()));
			mHandler.onTimeOut(mProto);
		}
		NetHelper.removeNetRequestRunnable(this);
	}

	void clear(boolean timeOut){
		// remote proto delegate
		NetHelper.removeProtoDelegate(NetHelper.makeUri(mGroup, 
				mResSub), this, RespondMethod);
		// remmove time out runnalbe
		if (mDelayRunnable != null) {
			ThreadBus.bus().removeCallbacks(ThreadBus.Main, mDelayRunnable, null);
			mDelayRunnable = null;
		}
		// deal respond
		if (mHandler != null && timeOut) {
			mHandler.onTimeOut(mProto);
		}
		// free runnable
		NetHelper.removeNetRequestRunnable(this);	
	}
	
	/// handler, get respond and timeout callback
	public static interface ProtoHandler {
		public void onRespond(Proto proto);
		public void onTimeOut(Proto proto);
	}

	public static class ProtoHandlerWrapper implements ProtoHandler {

		private ProtoHandler mHandler;

		public ProtoHandlerWrapper(ProtoHandler handler) {
			setHandler(handler);
		}

		public void setHandler(ProtoHandler handler) {
			mHandler = handler;
		}

		@Override
		public void onRespond(Proto proto) {
			if(mHandler != null) {
				mHandler.onRespond(proto);
			}
		}

		@Override
		public void onTimeOut(Proto proto) {
			if(mHandler != null) {
				mHandler.onTimeOut(proto);
			}
		}
	}

    public static class SoftProtoHandlerWrapper implements ProtoHandler {

        private WeakReference<ProtoHandler> mHandler;

        public SoftProtoHandlerWrapper(ProtoHandler handler) {
            setHandler(handler);
        }

        public void setHandler(ProtoHandler handler) {
            mHandler = new WeakReference<>(handler);
        }

        @Override
        public void onRespond(Proto proto) {
            if(mHandler != null) {
                ProtoHandler handler = mHandler.get();
                if(handler != null) {
                    handler.onRespond(proto);
                }
            }
        }

        @Override
        public void onTimeOut(Proto proto) {
            if(mHandler != null) {
                ProtoHandler handler = mHandler.get();
                if(handler != null) {
                    handler.onTimeOut(proto);
                }
            }
        }
    }
	
	public static ProtoHandler NullHandler = new ProtoHandler() {
		@Override
		public void onTimeOut(Proto proto) { }
		@Override
		public void onRespond(Proto proto) { }
	};
	
	/// send protocol
	public static NetRequest newBuilder(ProtoEnum group, 
			ProtoEnum reqSub, 
			ProtoBody message){
		return new NetRequest(group, reqSub, message);
	}
	
	/// send protocol and require the callback
	public static NetRequest newBuilder(ProtoEnum group, 
			ProtoEnum reqSub,
			ProtoEnum resSub, 
			ProtoBody message){
		NetRequest requestBuilder = new NetRequest(group, reqSub, message);
		requestBuilder.setResSub(resSub);
		return requestBuilder;
	}
	
	/// send protocol and require the callback and timeout
	public static NetRequest newBuilder(ProtoEnum group, 
			ProtoEnum reqSub,
			ProtoEnum resSub, 
			long timeOut,
			ProtoBody message){
		return new NetRequest(group, reqSub, message)
		.setResSub(resSub)
		.setTimeOut(timeOut);
	}
	
	/// send protocol
	public static NetRequest newBuilder(Proto proto){
		return new NetRequest(proto);
	}
	
	public static NetRequest newBuilder(){
		return new NetRequest();
	}
	
	/// send protocol and require the callback
	public static NetRequest newBuilder(int group, 
			int reqSub,
			int resSub, 
			ProtoBody message){
		NetRequest requestBuilder = new NetRequest(group, reqSub, resSub, message);
		return requestBuilder;
	}
}
