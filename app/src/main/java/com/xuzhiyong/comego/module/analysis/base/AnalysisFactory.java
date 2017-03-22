package com.xuzhiyong.comego.module.analysis.base;

import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.module.analysis.duowan.CrashReportWrapper;
import com.xuzhiyong.comego.module.analysis.duowan.HiidoWrapper;
import com.xuzhiyong.comego.module.analysis.umeng.UmengStatsWrapper;

/**
 * Created by xuzhiyong on 14-12-17.
 */
public class AnalysisFactory {

    public enum AnalysisType {
        hiddo,
        umeng,
    }

    public static IStatsInterface getStatsHandler(AnalysisType type) {
        switch (type) {
            case hiddo:
                return HiidoWrapper.getInstance();
            case umeng:
                return UmengStatsWrapper.getInstance();
            default:
                JLog.error("AnalysisFactory", "getStatsHandler error type:" + type);
                return null;
        }
    }

    public static ICrashReportInterface getCrashHandler(AnalysisType type) {
        switch (type) {
            case hiddo:
                return CrashReportWrapper.getInstance();
            case umeng:
                return UmengStatsWrapper.getInstance();
            default:
                JLog.error("AnalysisFactory", "getCrashHandler error type:" + type);
                return null;
        }
    }
}
