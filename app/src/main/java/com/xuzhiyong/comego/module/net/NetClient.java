package com.xuzhiyong.comego.module.net;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JConfig;
import com.duowan.fw.util.JFP;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JUtils;
import com.xuzhiyong.comego.module.Ln;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import protocol.ErrCode;
import protocol.PType;
import protocol.ProtoBody;
import protocol.SPLogin;

/**
 * Created by hydra on 2015/10/12.
 *
 */
public class NetClient {

	// log
	public static JLog.JLogModule KNet;

	// init
	static {
		KNet = JLog.JLogModule.makeOne("Net");
	}

	public static int LocalErrCode_LoginFailed = -1;
	public static int LocalErrCode_ActiveFailed = -2;
	public static int LocalErrCode_ActiveSuccess = -3;
	public static int LocalErrCode_LoginNetErr = -10;

	public static ArrayList<String> sDispatchIps = new ArrayList<String>();

	public static void fillDispatchIps(ArrayList<String> ips) {
		sDispatchIps.clear();

		if(!JFP.empty(ips)) {
			sDispatchIps.addAll(ips);
		}
	}

	public static ArrayList<String> dispatchIps(String hostName){
		ArrayList<String> dispatchIps = null;
		if(hostName != null && sAddressStr.equals(hostName)){
			dispatchIps = sDispatchIps;
		}
		return dispatchIps;
	}

	/// sever config
	public static final String sAddressKey = "__last_valid_server_address";
	public static final String sAddressPortKey = "__last_valid_server_address_port";
	/**
	 * 测试环境也用的一样的端口号
	 */
	public static final String sPort = "12501";
	public static final String sTestAddressStr = "test.win.yy.com";
	public static final String sAddressStr = "winsvr.yy.com"; //smart dns

	public static InetSocketAddress sISAAddress;
	//记录sISAAddress的HostName，和sISAAddress一起变化，为了防止需要获取hostName时，在主线程的bug
	public static String sIsAAddressHostName;
	public static ArrayList<String> sFallbackIps;

	static {
		sFallbackIps = new ArrayList<>();
		sFallbackIps.add("120.195.155.43");   // 移动
		sFallbackIps.add("122.193.200.171");  // 联通
		sFallbackIps.add("58.215.183.172");   // 电信
		sIsAAddressHostName = sAddressStr;

		ThreadBus.bus().callThreadSafe(ThreadBus.Net, new Runnable() {
			@Override
			public void run() {
				sISAAddress = new InetSocketAddress(sAddressStr, Integer.valueOf(sPort));
			}
		});
	}

	public static void setISAAddress(String host, int port) {
		sISAAddress = new InetSocketAddress(host, port);
		sIsAAddressHostName = sISAAddress.getHostName();

		JLog.debug(KNet, "setISAAddress to %s", sISAAddress.toString());
	}

	public static void setISAAddress(InetAddress address, int port) {
		sISAAddress = new InetSocketAddress(address, port);
		sIsAAddressHostName = sISAAddress.getHostName();

		JLog.debug(KNet, "setISAAddress to %s", sISAAddress.toString());
	}

	public static ArrayList<String> fallbackIps(String hostName){
		ArrayList<String> fallbackIps = null;
		if(hostName != null && sAddressStr.equals(hostName)){
			fallbackIps = sFallbackIps;
		}
		return fallbackIps;
	}

	public static String getAddressString(InetAddress[] addresses) {
		if (addresses == null) {
			return "[]";
		}

		int cnt = addresses.length;
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (InetAddress addr : addresses ) {
			sb.append(addr.toString());

			--cnt;
			if (cnt != 0) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static String getIpString(List<String> ips) {
		if (ips == null) {
			return "[]";
		}

		int cnt = ips.size();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String addr : ips ) {
			sb.append(addr);

			--cnt;
			if (cnt != 0) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static void chooseBestAddress(ArrayList<String> dispatchIps){
		final String lastAddress = JConfig.getString(sAddressKey, sAddressStr);
		final String lastPort = JConfig.getString(sAddressPortKey, sPort);
		final int nport= Integer.valueOf(lastPort);
		final int ntimeout = 7000;
		JLog.debug(KNet, "choose best address with last address: (%s:%s) and dispatchIps %s",
				lastAddress, lastPort, getIpString(dispatchIps));

		try {
			InetAddress[] addresses;

			if(JFP.empty(dispatchIps)) {
				addresses = InetAddress.getAllByName(lastAddress);
				JLog.debug(KNet, "choose best address by last address: %s", getAddressString(addresses));
			} else {
				addresses = new InetAddress[dispatchIps.size()];
				for(int i = 0; i < addresses.length; i++) {
					addresses[i] = InetAddress.getByName(dispatchIps.get(i));
				}
				JLog.debug(KNet, "choose best address by dispatchIps: %s", getAddressString(addresses));
			}

			final InetAddress[] allAddress = addresses;

			if (allAddress.length == 1) {
				setISAAddress(allAddress[0], Integer.valueOf(lastPort));
				return;
			}

			if(allAddress.length == 0) {
				setISAAddress(lastAddress, Integer.valueOf(lastPort));
				return;
			}

			final int maxThread = 5;
			final AtomicBoolean finished = new AtomicBoolean(false);
			final AtomicInteger tryedThread = new AtomicInteger();
			final AtomicInteger choose = new AtomicInteger(-1);
			final ArrayList<Thread> speedTestThreads = new ArrayList<Thread>();
			try {
				int length = allAddress.length;
				int offset = allAddress.length - maxThread;
				if (offset > 0) {
					// random choose
					offset = (int)(Math.random() * allAddress.length);
					length = maxThread;
				}else {
					offset = 0;
				}
				for (int i = offset; i < length; i++) {
					final int cur = i % allAddress.length;
					speedTestThreads.add(new Thread(new Runnable() {

						@Override
						public void run() {
							Socket socket = new Socket();
							try {
								InetSocketAddress address = new InetSocketAddress(allAddress[cur], nport);
								JLog.debug(KNet, "choose best address try connect(%s): %s", cur, address.toString());
								socket.connect(address, ntimeout);
								if (socket.isConnected() && finished.compareAndSet(false, true)) {
									// the first connection established
									tryedThread.decrementAndGet();
									choose.set(cur);
									synchronized (finished) {
										finished.notify();
									}
									JLog.debug(KNet, "choose best address of connect(%s): %s", cur, address.toString());
									return;
								}
								if(tryedThread.decrementAndGet() == 0){
									// all connection errored
									synchronized (finished) {
										finished.notify();
									}
								}
							}  catch (IOException e) {
								e.printStackTrace();
								if(tryedThread.decrementAndGet() == 0){
									// all connection exception
									synchronized (finished) {
										finished.notify();
									}
									JLog.debug(KNet, "choose best address out of connection, no valid address");
								}
							} finally{
								try {
									socket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}));
				}
				// start the speed try threads
				tryedThread.set(speedTestThreads.size());
				for (int i = 0; i < speedTestThreads.size(); i++) {
					speedTestThreads.get(i).start();
				}
				// wait the first connection build
				synchronized (finished) {
					finished.wait();
				}
			} catch (InterruptedException e) {
				// thread killed by system or other reason
				setISAAddress(lastAddress, Integer.valueOf(lastPort));
				e.printStackTrace();
			}
			// all can not connected
			if (choose.intValue() == -1) {
				choose.set((int)(Math.random() * allAddress.length));
			}
			// build address
			setISAAddress(allAddress[choose.intValue()], Integer.valueOf(lastPort));
		} catch (UnknownHostException e) {
			setISAAddress(lastAddress, Integer.valueOf(lastPort));
			e.printStackTrace();
		}
	}

	public static void solveAddress(String lastAddress, String lastPort){
		try {
			InetAddress[] allAddress = InetAddress.getAllByName(lastAddress);
			int choose = 0;
			if (allAddress.length > 1) {
				choose = (int)(Math.random() * allAddress.length);
			}
			JLog.debug(KNet, "Solve Address (%s:%s) to %s", lastAddress, lastPort,
					getAddressString(allAddress));
			setISAAddress(allAddress[choose], Integer.valueOf(lastPort));
		} catch (UnknownHostException e) {
			JLog.debug(KNet, "Solve Address Exception %s, set address direct to last address (%s:%s)",
					e.toString(), lastAddress, lastPort);
			setISAAddress(lastAddress, Integer.valueOf(lastPort));
			e.printStackTrace();
		}
	}

	//服务器切换
	public static enum Server {
		Server_Official,
		Server_Test,
	}

	//切换服务器
	public static void switchServer(Server server){
		if (server == Server.Server_Official) {
			switchServerTo(sAddressStr, sPort);
		}else if (server == Server.Server_Test) {
			switchServerTo(sTestAddressStr, sPort);
		}
	}

	//切换到指定服务器
	public static void switchServerTo(String address, String port){
		if (port==null) {
			port = sPort;
		}
		JLog.debug(KNet, "switch Server To: (%s:%s)", address, port);

		JConfig.putString(sAddressKey, address);
		JConfig.putString(sAddressPortKey, port);

		solveAddress(address, port);
	}

	/// new a channel to start communication
	public static Proto newChannelSend(NetModuleData.NetDataInterface ndi, Proto proto){
		NetDataChannel channel = new NetDataChannel(ndi);
		return NetDataChannel.tryChannel(channel, sISAAddress, proto, sFallbackIps);
	}

	/*************************Instance Field and Methods*******************************************/

	private Object mChannelLockerObject = new Object();

	private NetDataChannel mChannel;

	public NetClient(NetModuleData.NetDataInterface ndi) {
		mChannel = new NetDataChannel(ndi);
	}

	public void close() {
		mChannel.disconnect();
		mChannel = null;
	}

	public NetDataChannel channel() {
		return mChannel;
	}

	public void sendProto(Proto proto) {
		mChannel.sendProto(proto);
	}

	public long login(InetSocketAddress address, Proto proto, ArrayList<String> fallbackIps) {
        synchronized (mChannelLockerObject) {
            if (mChannel == null) {
                return LocalErrCode_LoginFailed;
            }
        }

		JLog.debug(KNet, "login to %s with fallbackIps %s in proto %s",
				address.toString(),
				getIpString(fallbackIps),
				proto.toString());

        JUtils.jAssert(proto.getHead().group == PType.PLogin.getValue()
                || proto.getHead().sub == SPLogin.PUserLoginReq.getValue());
        return doRealLogin(address, proto, fallbackIps);
    }

    private long doRealLogin(InetSocketAddress address, Proto proto, ArrayList<String> fallbackIps) {
        final NetDataChannel channel = mChannel;

	    if(channel == null) {
		    JLog.debug(KNet, "login with channel null (with return LocalErrCode_LoginNetErr)");
		    return LocalErrCode_LoginNetErr;// network_error
	    }

        // do login
        Proto ackProto = NetDataChannel.tryChannel(channel, address, proto, fallbackIps);
        if (ackProto != null) {
			JLog.debug(KNet, "login with respond %s", ackProto.toString());

            ProtoBody body = ackProto.getBody();
            if(ackProto.getBody() == null) {
                JLog.error(KNet, "Login ack proto body is null");

                channel.disconnect();

                Ln.doDealWithException(this, null, LocalErrCode_LoginFailed, null, null);

                return LocalErrCode_LoginFailed;
            }

            if(body.result == null) {
                JLog.error(KNet, "Login ack proto body's result is null");

                channel.disconnect();

                Ln.doDealWithException(this, null, LocalErrCode_LoginFailed, null, null);

                return LocalErrCode_LoginFailed;
            }

            if(body.result.code == ErrCode.Success) {
                // login successful
                channel.dumpReadThread();
            } else {
                JLog.info(KNet, String.format("login failed: %s",ackProto.getBody()
                        .result.toString()));

                channel.disconnect();

                Ln.dealWithException(this, ackProto.body.result);
            }

            // return the login result
            return ackProto.body.result.code.getValue();
        }

		JLog.debug(KNet, "login with respond null(with return LocalErrCode_LoginNetErr)");
        return LocalErrCode_LoginNetErr;// network_error
    }
}
