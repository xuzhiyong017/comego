package com.xuzhiyong.comego.module.analysis;

import com.duowan.fw.util.JDayDayUp;
import com.duowan.fw.util.JNetworkUtil;
import com.xuzhiyong.comego.module.datacenter.DataCenterHelper;
import com.xuzhiyong.comego.module.datacenter.tables.ProtoStatistics;

import java.util.List;

public class LocalStatics {

	private static final boolean OpenStatics = false;
    /// control keys
    private static final String KTagCount = " : Count";

    private static final long KSaveDistance = 256;
    private static final long KSaveCount = 1;

    /// tags
    public static final String KTag_SigRead = "SigRead";    // Long
    public static final String KTag_SigWrite = "SigWrite";    // Long

    public JDayDayUp mDayDayUp = new JDayDayUp();

    public static final int NetWay_2G3G = 1;
    public static final int NetWay_Wifi = 2;
    public static final int OpWay_Read = 1;
    public static final int OpWay_Write = 2;

    public LocalStatics() {
    }

    public int curNet() {
        if (JNetworkUtil.is2GOr3GActive()) {
            return NetWay_2G3G;
        } else {
            return NetWay_Wifi;
        }
    }

    public long curDay() {
        return JDayDayUp.day(0);
    }

    public void addReadSize(String tag, long size) {
        addSizeWay(tag, size, OpWay_Read, curNet(), curDay());
    }

    public void addWriteSize(String tag, long size) {
        addSizeWay(tag, size, OpWay_Write, curNet(), curDay());
    }


    public void addSizeWay(String tag, long size, int opWay, int netWay, long day) {
        if (addSizeTag(tag, size, opWay, netWay, day, KSaveDistance)) {
            addSizeTag(tag + KTagCount, 1L, opWay, netWay, day, KSaveCount);
        } else {
            addSizeTag(tag + KTagCount, 1L, opWay, netWay, day, Long.MAX_VALUE);
        }
    }


    public boolean addSizeTag(String tag, long size, int opWay, int netWay, long day, long threshold) {
    	if (!OpenStatics) {
    		return false;
    	}
        String key = ProtoStatistics.key(day, tag, netWay, opWay);
        long newSize = size(key, day) + size;

        if (newSize >= threshold) {
            mDayDayUp.done(key, 0L, day);
            save(day, tag, netWay, opWay, newSize);
            return true;
        } else {
            mDayDayUp.done(key, newSize, day);
            return false;
        }
    }

    public long size(String key, long day) {
        Object o = mDayDayUp.objectForKey(key, day);
        Long value = o != null ? Long.class.cast(o) : 0L;
        return value;
    }

    public void save(long day, String tag, int netWay, int opWay, long newSize) {
        String key = ProtoStatistics.key(day, tag, netWay, opWay);
        synchronized (this) {
            ProtoStatistics u = ProtoStatistics.info(DataCenterHelper.appDb(), String.valueOf(key.hashCode()));
            if (u == null) {
                u = new ProtoStatistics(day, tag, netWay, opWay);
            }
            u.size += newSize;
            ProtoStatistics.save(DataCenterHelper.appDb(), u);
        }
    }

    private static LocalStatics gs;

    static {
        gs = new LocalStatics();
    }

    public static LocalStatics ns() {
        return gs;
    }

    public List<ProtoStatistics> checkAll(){
        return ProtoStatistics.queryAll(DataCenterHelper.appDb());
    }

}
