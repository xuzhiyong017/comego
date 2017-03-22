package com.xuzhiyong.comego.module.net;

import android.text.TextUtils;

import com.duowan.fw.Module;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JRsaHelper;
import com.duowan.fw.util.JUtils;
import com.xuzhiyong.comego.module.analysis.StatsConst;
import com.xuzhiyong.comego.module.analysis.StatsHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import protocol.KeyExchangeReq;
import protocol.KeyExchangeRes;
import protocol.PType;
import protocol.SPEncrypt;

/**
 * Created by hydra on 2015/10/12.
 *
 */
public class NetDataChannel {

    //模拟网络延迟
    public static long SimulateNetDelay = 0L;

    private static final boolean TraceChannelLog = false;

    public static final String NetProtocolTag = "[NetDataChannel] ";

    public static final int SocketReadTimeOut = 20 * 1000;

    public static final int HeartbeatInterval = 900 * 1000;

    //读线程是每次连接新起一个线程，写线程是全局线程
    private static final int sWriteThread;

    static {
        sWriteThread = ThreadBus.gen();
        ThreadBus.bus().addThread(sWriteThread, "net-write-thread");
    }

    /// 通过通道发送一条协议到服务器, 同时获取一条协议
    public static Proto tryChannel(NetDataChannel channel, InetSocketAddress address,
                                   Proto proto, ArrayList<String> fallbackIps){
        /// try connect the server with init proto
        try {
            JLog.debug(NetClient.KNet, "netdatachannel connect %s %s", address.toString(),
                    NetClient.getIpString(fallbackIps));
            if(channel.connect(address, fallbackIps)){
                RSAPublicKey rsaKey = JRsaHelper.generate().getPublicKey();
                // exchange key
                KeyExchangeReq exchangeRequest = new KeyExchangeReq.Builder()
                        .random(12580)
                        .gzip(true)
                        .rsaPubKeyE(rsaKey.getPublicExponent().toString())
                        .rsaPubKeyN(rsaKey.getModulus().toString())
                        .build();

                Proto exchangeProto = NetHelper.buildProto(PType.PEncrypt,
                        SPEncrypt.PKeyExchangeReq,
                        NetHelper.pbb().keyExchangeReq(exchangeRequest).build());
                JLog.debug(NetClient.KNet, "netdatachanel exchangekey %s", exchangeProto.toString());
                // send exchange proto
                if (!channel.writeOneProto(exchangeProto)) {
                    channel.disconnect();
                    return null;
                }

                // read exchange respond
                Proto exchangeRespondProto = channel.readOneProto(false);

                if (exchangeRespondProto != null) {
                    JLog.debug(NetClient.KNet, "netdatachanel exchangekey respond %s", exchangeRespondProto.toString());

                    KeyExchangeRes exchangeRespond = exchangeRespondProto.getBody().keyExchangeRes;
                    if(TraceChannelLog){
                        JLog.info(NetClient.KNet, "accept the exchange respond, encrypt type: %s, %d, %s",
                                exchangeRespond.key,
                                exchangeRespond.random,
                                exchangeRespond.enctype.toString());
                    }
                    channel.configCrypt(exchangeRespond);
                    if (!channel.mOpened.get()) {
                        // error
                        JLog.info(NetClient.KNet, "config crypt exception");
                        return null;
                    }
                }else {
                    // error
                    JLog.info(NetClient.KNet, "read exchange respond proto: null");
                    channel.disconnect();
                    return null;
                }

                // send req
                JLog.info(NetClient.KNet, "send login in channel %s", proto.toString());
                if(!channel.writeOneProto(proto)) {
                    JLog.info(NetClient.KNet, "send login in channel failed %s", proto.toString());
                    channel.disconnect();
                    return null;
                }

                Boolean useGzip = exchangeRespondProto.getBody().keyExchangeRes.gzip;
                channel.mUseGZip.getAndSet(useGzip == null ? false : useGzip);

                // read login ack
                Proto ackProto = channel.readOneProto(channel.mUseGZip.get());
                if (ackProto != null) {
                    JLog.info(NetClient.KNet, "netdatachannel read login ack %s", ackProto.toString());
                    return ackProto;
                } else {
                    // error
                    JLog.info(NetClient.KNet, "read ack proto: null");
                    channel.disconnect();
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JLog.info(NetClient.KNet, "try channel io exception %s", TextUtils.join("\n", e.getStackTrace()));
        }
        return null;
    }

    /*********************Instance Fields and Methods****************************************/

    private NetConnection mNetConnection;
    private NetModuleData.NetDataInterface mNetDataInterface;

    private AtomicBoolean mOpened = new AtomicBoolean(false);
    private AtomicBoolean mUseGZip = new AtomicBoolean(true);
    private AtomicBoolean mDealWithException = new AtomicBoolean(false);

    private Thread mReadThread;

    private final Object mLockConnect = new byte[0];

	public NetDataChannel(NetModuleData.NetDataInterface netDataInterface) {
        mNetDataInterface = netDataInterface;
	}

    public void dumpReadThread(){
        JUtils.jAssert(mNetConnection != null);
        JUtils.jAssert(mReadThread == null);

        synchronized (mLockConnect) {

            if(TraceChannelLog){
                JLog.info(NetClient.KNet, NetProtocolTag + "start the reading thread");
            }

            mReadThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    while (mOpened.get()) {

                        // read one proto
                        readProtoInternal(mUseGZip.get());

                        // sleep cpu , give execute chance for low priority thread
                        try {
                            if(SimulateNetDelay > 0) {
                                Thread.sleep(Math.max(1L, SimulateNetDelay));
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, "net-read-thread");

            mReadThread.setPriority(Thread.MAX_PRIORITY);
            mReadThread.start();

            NetHelper.startHeartBeat();
        }
    }

    public boolean connect(InetSocketAddress address, ArrayList<String> fallbackIps) throws IOException{
        JUtils.jAssert(!mOpened.get());
        JUtils.jAssert(mNetConnection == null);

        try {
            NetConnection connection = new NetConnection();
            if (connection.connect(address, fallbackIps)) {
                if (mOpened.compareAndSet(false, true)) {
                    synchronized (mLockConnect) {
                        mNetConnection = connection;
                    }
                    return true;
                }
            }
        } catch (IOException e) {
            dealWithException(e);
        }

        return false;
    }

	public void disconnect() {
        // clear the interface
        mNetDataInterface = null;
        // clear the interface we do not care it exceptions
        if (mOpened.compareAndSet(true, false)) {
            synchronized (mLockConnect) {
                NetHelper.stopHeartBeat();

                // do not block in reading
                if (mNetConnection != null) {
                    mNetConnection.stopReading();
                }

                // close the read thread
                if (mReadThread != null) {
                    mReadThread.interrupt();
                    try {
                        if (Thread.currentThread().getId() != mReadThread.getId()) {
                            mReadThread.join();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // close the socket
                if (mNetConnection != null) {
                    try {
                        mNetConnection.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mNetConnection = null;
                mReadThread = null;
            }
        }
	}

	public void sendProto(final Proto proto) {
        if (mOpened.get()) {
            ThreadBus.bus().post(sWriteThread, new Runnable() {
                @Override
                public void run() {
                    writeProtoInternal(proto);
                }
            });
        }
	}

    /// NB! DO NOT CALL THIS IF YOU DONT KNOW WHAT WILL HAPPEN
    public Proto readOneProto(boolean useGzip) throws IOException{
        try {
            mNetConnection.setSoTimeout(SocketReadTimeOut);

            Proto proto = readProtoInternal(useGzip);
            if (mNetConnection != null) {
                mNetConnection.setSoTimeout(0);
            }
            return proto;
        } catch (Exception e) {
            JLog.error(NetClient.KNet, "read One Proto failed : " + e);
        }

        return null;
    }

    private Proto readProtoInternal(boolean useGzip){
        try {
            Proto proto = mNetConnection.readProto(useGzip);

            if (mNetDataInterface != null && proto != null) {
                mNetDataInterface.onData(this, proto);
            }

            return proto;
        } catch (IOException e) {
            JLog.error(NetClient.KNet,
                    NetProtocolTag + String.format("reading thread error: %s-%s",
                            e.getClass().getName(),
                            e.getMessage()));

            dealWithException(e);
        }

        return null;
    }

    /// NB! DO NOT CALL THIS IF YOU DONT KNOW WHAT WILL HAPPEN
    public boolean writeOneProto(final Proto proto){
        try {
            return writeProtoInternal(proto);
        } catch (Exception e) {
            e.printStackTrace();
            JLog.error(NetClient.KNet, "write One Proto failed : " + e);

            return false;
        }
    }

    private boolean writeProtoInternal(final Proto proto){
        try {
            synchronized(mLockConnect){
                if (mNetConnection != null && mOpened.get()) {
                    mNetConnection.writeProto(proto);
                }
            }
            return true;
        } catch (IOException e) {
            if(TraceChannelLog){
                JLog.error(NetClient.KNet,
                        NetProtocolTag + String.format("net-protocol, write proto error: %s - %s",
                                e.getClass().getName(),
                                e.getMessage()));
            }

            dealWithException(e);
        }
        return false;
    }

	public boolean configCrypt(KeyExchangeRes keyExchange){
		if (mNetConnection != null && mOpened.get()) {
			try {
				mNetConnection.setCrypt(keyExchange.enctype, keyExchange.key);
			} catch (Exception e) {
				JLog.error(NetClient.KNet, NetProtocolTag + "wrong keyExchange respond " + e);

				Map<String, String> map = new HashMap<>();
				map.put("exception", e.toString());
				StatsHelper.reportTimesEvent(Module.gMainContext, 0L, StatsConst.DECRYPT_EXCEPTION, null, map);

                IOException ioException = new IOException(e.getMessage());
				dealWithException(ioException);
			}
		}
		return false;
	}

    private void dealWithException(IOException e){
        if (mDealWithException.compareAndSet(false, true)) {
            JLog.debug(NetClient.KNet, "deal with exception %s - %s",e.toString(),
                    TextUtils.join("\n", e.getStackTrace()));
            if (mNetDataInterface != null) {
                // send the broken to interface
                mNetDataInterface.onException(this, 0, e);
                mNetDataInterface = null;
            }

            // disconnect when netborken
            if (mOpened.get()) {
                disconnect();
            }
        }
    }
}
