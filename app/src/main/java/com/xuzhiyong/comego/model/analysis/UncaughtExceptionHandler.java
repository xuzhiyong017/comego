package com.xuzhiyong.comego.model.analysis;

import android.os.Looper;

import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JUtils;
import com.duowan.fw.util.LogToES;
import com.duowan.ada.module.DModule;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler dueh;

    public UncaughtExceptionHandler(Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler) {
        this.dueh = defaultUncaughtExceptionHandler;
    }

    public void uncaughtException(final Thread thread, final Throwable throwable) {
        if (throwable == null) {
            return;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        final String str = sw.toString();

        if (JUtils.externalStorageExist()) {
            try {
                LogToES.writeLogToFileReal(LogToES.LOG_PATH, LogToES.UE_LOG_NAME, str);
            }
            catch (IOException e) {
                JLog.error(this, e);
            }
        }

        //start
        final Runnable task = new Runnable() {
            public void run() {
                DModule.ModuleAnalysis.cast(AnalysisInterface.class).reportCrash(throwable);
                if (dueh != null) {
                    dueh.uncaughtException(thread, throwable);
                }
            }
        };
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            new Thread(task).start();
        } else {
            task.run();
        }
    }
}
