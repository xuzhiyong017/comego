package com.xuzhiyong.comego.module.net;

import android.util.SparseArray;

import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.module.DConst;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by jerryzhou on 15/11/2.
 *
 */
public class NetTracker {

    /// tracker struct
    protected class TrackerValue {
        public long ts;

        public int seq;
        public int uri;
        public int uriack;

        // is time out
        public boolean isTimeOut(long xts, long duration) {
            return xts - ts >= duration;
        }

        // is my ack
        public boolean isMyAck(int xseq, int xuriack) {
            return seq == xseq && uriack == xuriack;
        }
    }

    /// all seqs
    protected SparseArray<LinkedList<TrackerValue>> mAllSeqs = new SparseArray<LinkedList<TrackerValue>>();
    /// max capacity
    protected int mCapacity;
    /// will be timeout
    protected long mMaxTsDuration;

    /// default constructor
    public NetTracker() {
        mCapacity = 5;
        mMaxTsDuration = DConst.KC_MaxNetOperatorTimeOut;
    }

    /// constructor
    public NetTracker(int capacity, long duration) {
        mCapacity = capacity;
        mMaxTsDuration = duration;
    }

    /// track protocol
    public void track(Proto p) {
        int uri = p.head.getUri();
        int uriack = NetHelper.makeUri(p.head.getGroup(), p.head.getSub() + 1);
        int seq = p.head.getSeq();

        trackIt(uri, uriack, seq);
    }

    /// is right my ack
    public boolean isCareAck(Proto p) {
        int uriack = p.head.getUri();
        int seq = p.head.getSeq();
        boolean found = false;

        synchronized (mAllSeqs) {
            // get uri seqs
            LinkedList<TrackerValue> seqs = mAllSeqs.get(uriack);
            if (seqs == null) {
                return false;
            }

            // for each
            ListIterator<TrackerValue> iterator = seqs.listIterator();
            while (iterator.hasNext()) {
                TrackerValue value = iterator.next();
                if (value.isMyAck(seq, uriack)) {
                    iterator.remove();
                    found = true;
                    break;
                }
            }

        }
        return found;
    }


    ///////////////////////////////////////////////
    // implements Private
    ///////////////////////////////////////////////

    protected void trackIt(int uri, int uriack, int seq) {
        long ts = System.currentTimeMillis();

        synchronized (mAllSeqs) {
            // get uri seqs
            LinkedList<TrackerValue> seqs = mAllSeqs.get(uriack);
            if (seqs == null) {
                seqs = new LinkedList<TrackerValue>();
                mAllSeqs.put(uriack, seqs);
            }

            // shrinking trackers
            if (seqs.size() > mCapacity) do {
                TrackerValue value = seqs.getLast();
                if (mMaxTsDuration > 0 && value.isTimeOut(ts, mMaxTsDuration)) {
                    seqs.removeLast();
                } else {
                    if (seqs.size() > mCapacity) {
                        JLog.debug(this, "too much protocol track in ");

                    }
                    break;
                }
            } while (seqs.size() > mCapacity/2);

            TrackerValue value = new TrackerValue();
            value.seq = seq;
            value.ts = ts;
            value.uri = uri;
            value.uriack = uriack;

            seqs.addFirst(value);
        }
    }
}
