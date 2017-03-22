package com.xuzhiyong.comego.module.net;

import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.util.JLog;

public class NetPing extends KvoSource {

	public static final boolean OpenPing = false;
	
	public static final int MaxSeq = 1024;
	public static final long MaxMissDelay = 15*1000;
	public static final int DefaultPingInterval = 5;
	
	public static final int IndexSeq = 0;
	public static final int IndexRead = 1;
	public static final int IndexSend = 2;

	public long pings[][];	// a ring buffer for seq ping
	public String tags[];

	public boolean OpenLogDetail = false;
	
	public int seq;
	
	public static final String Kvo_nping = "nping";
	@KvoAnnotation(name=Kvo_nping)
	public long nping;
	
	public int npingseq;

	public NetPing() {
		pings = new long[MaxSeq][3];
		tags = new String[MaxSeq];
		seq = 0;
	}
	
	public void triggernping() {
		this.ping();
	}
	
	// 记录开始时间点
	public void addsend(int seq) {
		this.addsendtag(seq, "");
	}
	
	// 记录开始时间点
    public void addsendtag(int seq, String tag) {
    	if (!OpenPing) {
    		return;
    	}
        int index = seq % MaxSeq;
        
        pings[index][IndexSeq] = seq;
        pings[index][IndexSend] = System.currentTimeMillis();
        pings[index][IndexRead] = 0;

        tags[index] = tag;
        
        JLog.debug(this, "[NetPing] send %d, %s", seq, tag);
        
        // trigger kvo
        triggernping();
    }
	
	// 记录协议返回时间点，返回当前协议的ping值
	public long addread(int seq) {
		if (!OpenPing) {
    		return 0;
    	}

		// 0 not valid
		if(seq == 0) {
			return 0;
		}
		JLog.debug(this, "[NetPing] read %d", seq);
		if(seq > this.seq) {
			this.seq = seq;
		}
		int index = seq % MaxSeq;
		while (index < 0) {
			index += MaxSeq;
		}
		
		if (pings[index][IndexSeq] == seq) {
			pings[index][IndexRead] = System.currentTimeMillis();
			
			return (pings[index][IndexRead] - pings[index][IndexSend]) / 2;
		}
		
		return 0;
	}
	
	// 某一条协议的ping值
	public long pingseq(int seq) {
		int index = seq % MaxSeq;
		while (index < 0) {
			index += MaxSeq;
		}
		long delay = 0;
		if(pings[index][IndexSeq] == seq ) {
			if (pings[index][IndexRead] != 0) {
				delay = pings[index][IndexRead] - pings[index][IndexSend];
			} else {
				delay = System.currentTimeMillis() - pings[index][IndexSend];
				if (delay > MaxMissDelay) {
					delay = MaxMissDelay;
				}
			}
		}
		return delay/2;
	}

	// 某一条协议的tag值
    public String tagseq(int seq) {
		int index = seq % MaxSeq;
		while (index < 0) {
			index += MaxSeq;
		}
		if(pings[index][IndexSeq] == seq ) {
			return tags[index];
			
		}
		return null;
	}
	
	// 最近几条协议的平均ping值
	public long pinginterval(int interval) {
		StringBuilder sb = null;
		if (OpenLogDetail) {
			sb = new StringBuilder();
			sb.append("caculate the delay begin: ");
			sb.append(this.seq);
			sb.append(" ");
		}
				
		int cseq = this.seq;
		long delay = 0;
        int i=0;
		for(;i<interval; ) {
			long pingseq = pingseq(cseq);
			if (pingseq == 0){
				break;
			}
			
			if (sb != null) {
				String tag = tagseq(cseq);
				if (tag != null) {
					sb.append(tag);
				}

				sb.append("seq:");
				sb.append(cseq);
				sb.append(" with delay:");
				sb.append(pingseq);
				sb.append("    ");
			}
						
			delay += pingseq;
			--cseq;
			++i;
		}
		
		if (sb != null ) {
			sb.append(" in dealys:");
			sb.append(delay);
			sb.append(" interval:");
			sb.append(i);

			long rdelay = delay/ Math.max(i, 1);
			if (rdelay >= 300) { 
				for(int i1 = this.seq; i1 >= this.seq - 5; --i1){
                    int index = i1 % MaxSeq;
                    while (index < 0) {
                            index += MaxSeq;
                    }
                    sb.append(" [:]");
                    sb.append(i1);
                    sb.append(" tags:");
                    sb.append(tags[index]);
                    sb.append(" seq: ");
                    sb.append(pings[index][IndexSeq]);
                    sb.append(" send: ");
                    sb.append(pings[index][IndexSend]);
                    sb.append(" read: ");
                    sb.append(pings[index][IndexRead]);
                } 
				JLog.debug(this, "[NetPing] %s", sb.toString());
			}
		}

		return delay/ Math.max(i, 1);
	}
	
	// 默认取最近5条协议的ping值, 并出发kvo
	public long ping() {
		if(this.seq == this.npingseq) {
			return this.nping;
		}
		this.npingseq = this.seq;
		this.setValue(Kvo_nping, pinginterval(DefaultPingInterval));
		
		return this.nping;
	}
	
	// *****************************************************************
	static NetPing ping;
	
	static {
		ping = new NetPing();
	}
	
	public static NetPing np() {
		return ping;
	}
}
