package com.xuzhiyong.comego.module.net;

import com.duowan.fw.FwEvent;
import com.duowan.fw.util.JNetworkUtil;
import com.duowan.fw.util.JTimeUtils;
import com.squareup.wire.ProtoEnum;
import com.squareup.wire.Wire;
import com.xuzhiyong.comego.module.DModule;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import protocol.ProtoBody;

public class NetHelper {

	public static final Wire swire;

	public static final int curSeconds() {
		return JTimeUtils.javaTimeToUnit32(JTimeUtils.getTimeStamp());
	}

	/// sequence
	public static AtomicInteger sSeq;

	/****
	 * NB!
	 * Use The AtomicInteger And Got Increment Not Very Safe
	 * If We Send so many Protocols, then Crash And Restart The App
	 * There are still chance we got used value as the init seq
	 * */
	static {
		swire = new Wire();
		sSeq = new AtomicInteger(curSeconds());
	}

	public static int allocSeq(){
		return sSeq.getAndIncrement();
	}

	// auto binding all annotation events
	public static final void autoBindingProto(Object target){
		FwEvent.autoBindingEvent(localNetDispatcher(), DNetDelegateDestination.BUILDER, target);
	}

	// auto remove all annotation events
	public static final void autoRemoveProto(Object target){
		FwEvent.autoRemoveEvent(localNetDispatcher(), DNetDelegateDestination.BUILDER, target);
	}



	/**
	 * uri op: unsigned int shifting
	 * see: http://docs.oracle.com/javase/tutorial/java/nutsandbolts/op3.html
	 * The unsigned right shift operator ">>>" shifts a zero into the leftmost position,
	 * while the leftmost position after ">>" depends on sign extension
	 * */
	public static int makeUri(ProtoEnum type, ProtoEnum sub){
		int h = type.getValue();
		int uri = ( (h & 0xFF) << 24) | ((sub.getValue() & 0xFF) << 16);
		return uri;
	}
	public static int makeUri(int h, int l){
		int uri = ( (h & 0xFF) << 24) | ((l & 0xFF) << 16);
		return uri;
	}
	public static int getUriGroup(int uri){
		return (uri >>> 24) & 0xFF;
	}
	public static int getUriSub(int uri){
		return (uri >>> 16) & 0xFF;
	}

	/**
	 * 发送协议时用，用来引用计数
	 * */
	private static ConcurrentLinkedQueue<Runnable> mCachedNetRequestRunnables = new ConcurrentLinkedQueue<Runnable>();
	/**
	 * */
	public static void addNetRequestRunnables(Runnable r){
		mCachedNetRequestRunnables.add(r);
	}

	/**
	 * */
	public static void removeNetRequestRunnable(Runnable r){
		mCachedNetRequestRunnables.remove(r);
	}

	public static FwEvent.FwEventDispatcher localNetDispatcher(){
		return DModule.ModuleNet.cast(NetInterface.class).netDispatcher();
	}

	/// add protocol
	public static void addProtoDelegate(int uri, Object target, String delegate){
		DModule.ModuleNet.cast(NetInterface.class).addProtoDelegate(uri, target, delegate);
	}

    /// remove protocol
    public static void removeProtoDelegate(int uri, Object target, String delegate){
        DModule.ModuleNet.cast(NetInterface.class).removeProtoDelegate(uri, target, delegate);
    }

    public static Proto buildProto(ProtoEnum type,
                                   ProtoEnum sub,
                                   ProtoBody body){
        return buildProto((type.getValue()), (sub.getValue()), body);
    }

    public static Proto buildProto(int type, int  sub, ProtoBody body){
        Proto.ProtoHead head = new Proto.ProtoHead();
        head.group = (byte)(type & 0xFF);
        head.sub = (byte)(sub & 0xFF);
        head.seq = NetHelper.allocSeq();
        head.length = 0;

        Proto proto = new Proto();
        proto.head = head;
        proto.body = body;
        return proto;
    }

    public static ProtoBody.Builder pbb(){
        return new ProtoBody.Builder();
    }

	public static void sendProto(ProtoEnum type,
	                             ProtoEnum sub,
	                             ProtoBody message){
		Proto proto = buildProto(type, sub, message);
		if(proto != null){
			sendProto(proto);
		}
	}

	public static void sendProto(Proto proto) {
		DModule.ModuleNet.cast(NetModule.class).sendProto(proto);
	}

    public static boolean isWifi(){
        return JNetworkUtil.isWifiActive();
    }

    public static void startHeartBeat() {
        DModule.ModuleNet.cast(NetInterface.class).startHeartBeat();
    }

    public static void stopHeartBeat() {
        DModule.ModuleNet.cast(NetInterface.class).stopHeartBeat();
    }

    public static void sendHeartBeat() {
        DModule.ModuleNet.cast(NetInterface.class).sendHeartBeat();
    }
}
