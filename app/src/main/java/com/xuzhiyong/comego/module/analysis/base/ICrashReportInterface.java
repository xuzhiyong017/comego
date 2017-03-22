package com.xuzhiyong.comego.module.analysis.base;

import android.content.Context;

/**
 * Created by xuzhiyong on 14-12-18.
 */
public interface ICrashReportInterface {
    void initCrashReport();
    void reportCrash(Context context, Throwable t);
}
