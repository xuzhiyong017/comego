package com.xuzhiyong.comego.module.net;

import android.util.SparseArray;

/**
 * Created by jerryzhou on 15/10/24.
 *
 */
public class NetStatics {

    SparseArray<Long> mSendTicks = new SparseArray<Long>();
    byte[] mSendLock = new byte[0];

    SparseArray<Long> mRecvTicks = new SparseArray<Long>();
    byte[] mRecvLock = new byte[0];

    // 发送协议的时候统计
    public void whenSendProto(Proto p) {
        int uri = p.getHead().getUri();
        synchronized (mSendLock) {
            mSendTicks.put(uri, System.currentTimeMillis());
        }
    }

    // 收到协议的时候统计
    public void whenRecvProto(Proto p) {
        int uri = p.getHead().getUri();
        synchronized (mRecvLock) {
            mRecvTicks.put(uri, System.currentTimeMillis());
        }
    }

    // 上次发送这条协议已经经过了多久时间
    public boolean havePassTimeLastSend(int group, int sub, long duration) {
        long cur = System.currentTimeMillis();
        int uri = NetHelper.makeUri(group, sub);
        long tick = 0;

        synchronized (mSendLock) {
            tick = mSendTicks.get(uri, 0l);
        }

        return  cur - tick >= duration;
    }

    // 上次收到这条协议已经经过了多久时间
    public boolean havePassTimeLastRecv(int group, int sub, long duration) {
        long cur = System.currentTimeMillis();
        int uri = NetHelper.makeUri(group, sub);
        long tick = 0;

        synchronized (mRecvLock) {
            tick = mSendTicks.get(uri, 0l);
        }

        return  cur - tick >= duration;
    }

    // singleton
    static NetStatics _statics = null;
    static public NetStatics instance() {
        if (_statics == null) {
            _statics = new NetStatics();
        }
        return _statics;
    }
}
