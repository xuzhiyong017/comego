package com.xuzhiyong.comego.module.net;

import android.util.Base64;

import com.duowan.fw.util.EncryptCipher;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JRsaHelper;
import com.duowan.fw.util.JUtils;
import com.xuzhiyong.comego.module.DConst;
import com.xuzhiyong.comego.module.analysis.LocalStatics;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import protocol.EncryptType;
import protocol.ProtoBody;


/**
 * Created by hydra on 2015/10/12.
 *
 */
public class NetConnection {

	private static final boolean TraceProtocol = true;//JUtils.isDebugMode(Module.gMainContext);
	/// trace the protocol size
	private final static boolean TraceProtocolSize = false;
	/// trace the protocol ping
	private final static boolean TraceProtocolPing = false;

	private final static int Read_Tag_Head = 0;
	private final static int Read_Tag_Body = 1;

	private final static int ConnectTimeOut = 6 * 1000;
	private final static int KMaxProtoBodySize = 1024* 1000;

	private Socket mSocket;
	private InetSocketAddress mAddress;
	private EncryptCipher mDataCipher = null;

	private volatile boolean mRunning = true;

	private GZIPInputStream mGZipSocketStream;

	public NetConnection() {

	}

	public synchronized boolean connect(InetSocketAddress address, ArrayList<String> fallbackIps) throws IOException {
		JUtils.jAssert(mSocket == null);
		JLog.info(NetClient.KNet, String.format("conneting to: %s", address.toString()));

		mAddress = address;

		ArrayList<String> fallbackIpCopyList = null;
		if (fallbackIps != null) {
			fallbackIpCopyList = new ArrayList<String>(fallbackIps);
		}

		InetSocketAddress toConnectAddress = mAddress;
		do {
			try {
				Socket socket = new Socket();

				socket.connect(toConnectAddress, ConnectTimeOut);

				if (socket.isConnected()) {
					synchronized (this) {
						JLog.info(NetClient.KNet, String.format("connected to %s successful",
								toConnectAddress.toString()));

						mSocket = socket;
					}

					return true;
				}
			} catch (Exception e) {
				JLog.info(NetClient.KNet, String.format("connected to %s time out", toConnectAddress.toString()));
			}

			if (fallbackIpCopyList != null && fallbackIpCopyList.size() > 0) {

				int choose = (int)(Math.random() * fallbackIpCopyList.size());

				String fallbackIp = fallbackIpCopyList.remove(choose);

				toConnectAddress = new InetSocketAddress(fallbackIp, address.getPort());
			} else {
				toConnectAddress = null;

				JLog.info(NetClient.KNet, "connect failed after try times");

				throw new IOException("connect failed after try times");
			}
		} while (toConnectAddress != null);

		return false;
	}

	public void stopReading() {
		mRunning = false;
	}

	public synchronized void disconnect() throws IOException {
		stopReading();

		if (mSocket != null) {
			JLog.info(NetClient.KNet, String.format("disconnect from: %s", mAddress.toString()));

			synchronized (this) {
				if(mGZipSocketStream != null) {
					mGZipSocketStream.close();
					mGZipSocketStream = null;
				}

				mSocket.close();
				mSocket = null;
			}
		}
	}

	protected Proto readProto(boolean useGzip) throws IOException{
		JUtils.jAssert(mSocket != null);

		Proto.ProtoHead header = readHead(mSocket, useGzip);
		if (header != null){
			return readProto(mSocket, header, useGzip);
		}
		return null;
	}

	private Proto.ProtoHead readHead(Socket socket, boolean useGzip) throws IOException{
		byte[] buffer = new byte[Proto.ProtoHead.HeadSize];
		if (readData(socket, buffer, Read_Tag_Head, useGzip) == Proto.ProtoHead.HeadSize) {
			return Proto.ProtoHead.parseFrom(buffer);
		}
		return null;
	}

	private Proto readProto(Socket socket, Proto.ProtoHead header, boolean useGZip) throws IOException{
		if (header.getLen() > 0 && header.getLen() < KMaxProtoBodySize) {
			// log the write size
			if (TraceProtocolSize) {
			}

			// log protocol have received
			if(TraceProtocol){
				JLog.info(NetClient.KNet, String.format("%s read proto: %s, %s, %d",
						NetDataChannel.NetProtocolTag,
						header.getGroupProtocolName(),
						header.getSubProtocolName(),
						header.seq));
			}

			byte[] buffer = new byte[header.getLen()];
			readData(socket, buffer, Read_Tag_Body, useGZip);
			ProtoBody body = null;

			try {
				body = NetHelper.swire.parseFrom(buffer, ProtoBody.class);
			} catch (Exception e) {
				JLog.error(NetClient.KNet, "wrong protocol format: " + e.getMessage());
			}

			if (body != null) {
				Proto proto = new Proto();
				proto.body = body;
				proto.head = header;

				// ping 值
				if (TraceProtocolPing) {
					long ping = NetPing.np().addread(header.seq);

					if (TraceProtocolSize) {
						if (ping > 0 && header.seq > 0) {
							LocalStatics.ns().addReadSize("[Ping] " + header.toString(),  ping);
						}
					}
				}
				// statics proto
				NetStatics.instance().whenRecvProto(proto);

				return proto;
			} else {
				JLog.error(NetClient.KNet, String.format("%s, wrong proto head: uri-%s, sub-%s, size-%d",
                        NetDataChannel.NetProtocolTag,
						header.getGroupProtocolName(),
						header.getSubProtocolName(),
						header.getLen()));
				return null;
			}
		} else if(header.getLen() > KMaxProtoBodySize){
			JLog.error(NetClient.KNet, String.format("%s, read wrong proto with huge body size: uri-%s, sub-%s, size-%d",
                    NetDataChannel.NetProtocolTag,
					header.getGroupProtocolName(),
					header.getSubProtocolName(),
					header.getLen()));
			throw new IOException(String.format("%s, wrong proto head: uri-%s, sub-%s, size-%d",
                    NetDataChannel.NetProtocolTag,
					header.getGroupProtocolName(),
					header.getSubProtocolName(),
					header.getLen()));
		}else if (header.getLen() == 0) {
			JLog.error(NetClient.KNet, String.format("%s, read dummy proto head: uri-%s, sub-%s, size-%d",
                    NetDataChannel.NetProtocolTag,
					header.getGroupProtocolName(),
					header.getSubProtocolName(),
					header.getLen()));
			return null;
		}else {
			throw new IOException(String.format("%s, wrong proto head: uri-%s, sub-%s, size-%d",
                    NetDataChannel.NetProtocolTag,
					header.getGroupProtocolName(),
					header.getSubProtocolName(),
					header.getLen()));
		}
	}

	private int readData(Socket socket, byte[] buffer, int tag, boolean useGZip) throws IOException {
		int len = readDataDirect(socket, buffer, tag, useGZip);

		if (mDataCipher != null && len > 0) {
			mDataCipher.decrypt(buffer);
		}

		return len;
	}

	public int readDataDirect(Socket socket, byte[] buffer, int tag, boolean useGZip) throws IOException {
		int offset = 0;

		while (offset < buffer.length) {
			int available = socket.getInputStream().available();

			if (available > 50 * 1024) {
				JLog.warn(NetClient.KNet, "[Socket-Buffer] Kernel Buffer Size: " + available);
			}

			int len = -1;
			if(useGZip) {
				if(mGZipSocketStream == null) {
					mGZipSocketStream = new GZIPInputStream(socket.getInputStream());
				}

				len = mGZipSocketStream.read(buffer, offset,  buffer.length - offset);
			} else {
				len = socket.getInputStream().read(buffer, offset,  buffer.length - offset);
			}

			if (len < 0) {
				JLog.info(NetClient.KNet, String.format("%s, read data error: (%d), offset: (%d) buffer-lenght: (%d)",
                        NetDataChannel.NetProtocolTag,
						len, offset, buffer.length));

				throw new IOException("socket read return -1");
			}

			offset += len;

			if (mRunning == false){
				throw new IOException("stop reading");
			}
		}

		return offset;
	}

	protected void writeProto(Proto proto) throws IOException {
        if(TraceProtocol){
            JLog.info(NetClient.KNet,
                    String.format("%s, write proto: %s, %s, %d",
                            NetDataChannel.NetProtocolTag,
                            proto.getHead().getGroupProtocolName(),
                            proto.getHead().getSubProtocolName(),
                            proto.getHead().seq));
        }

        if (null == mSocket) {
            throw new IOException("socket is null");
        }

        // TODO: compressed
        if(proto.getBody().clientVersion == null || proto.getBody().clientVersion == 0){
	        proto.body = new ProtoBody.Builder(proto.body)
			        .clientVersion(DConst.KC_ClientVersion).build();
        }
        byte[] data = proto.getBody().toByteArray();
        proto.head.length = data.length;

        writeData(mSocket, proto.head.toByteArray());
        writeData(mSocket, data);

        mSocket.getOutputStream().flush();

        // log the write size
        if (TraceProtocolSize) {
            LocalStatics.ns().addWriteSize(proto.head.toString()
                    , data.length + Proto.ProtoHead.HeadSize);
            LocalStatics.ns().addWriteSize(LocalStatics.KTag_SigWrite
                    , data.length + Proto.ProtoHead.HeadSize);
        }

        // log the tick
        if (TraceProtocolPing) {
            NetPing.np().addsend(proto.head.seq);
        }

		// statics proto
		NetStatics.instance().whenSendProto(proto);
	}

	protected void writeData(Socket socket, byte[] buffer) throws IOException {
		if (mDataCipher != null) {
			mDataCipher.encrypt(buffer);
		}

		OutputStream stream = socket.getOutputStream();

		if (!socket.isOutputShutdown() && stream != null && buffer != null) {
			stream.write(buffer);
		}else {
			throw new IOException();
		}
	}

	//设置为0的时候是no timeout
	public void setSoTimeout(int timeout) {
		try {
			mSocket.setSoTimeout(timeout);
		} catch (SocketException e) {
			JLog.error(NetClient.KNet, "set socket timeout error : " + e);
		}
	}

	public boolean setCrypt(EncryptType type, String key){
		if (type == EncryptType.EncryptTypeRC4) {
			byte[] bytes = Base64.decode(key, Base64.DEFAULT);
			bytes = JRsaHelper.generate().decryptData(bytes);
			mDataCipher = new EncryptCipher(bytes);
		}else {
			mDataCipher = null;
		}
		return true;
	}
}
