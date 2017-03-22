package com.xuzhiyong.comego.module.analysis.duowan;

import android.content.Context;

import com.xuzhiyong.comego.module.analysis.base.ICrashReportInterface;


public class CrashReportWrapper implements ICrashReportInterface {

    private static final class Holder {
        private static final CrashReportWrapper sInstance = new CrashReportWrapper();
    }

    public static CrashReportWrapper getInstance() {
        return Holder.sInstance;
    }

    @Override
    public void initCrashReport() {
        // FIXME: 16-10-25
//        CrashReport.init(Module.gMainContext, "5707", DConst.sChannelID);
//        CrashReport.setUserLogFile("/sdcard/duowan/gamego/log/logs.txt");
    }

    @Override
    public void reportCrash(Context context, Throwable t) {
        // do nothing
    }
}
