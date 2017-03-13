package com.duowan.fw.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;

public class JUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Context context;
    private UncaughtExceptionHandler dueh;

    public JUncaughtExceptionHandler(Context context, UncaughtExceptionHandler defaultUncaughtExceptionHandler) {
        this.context = context;
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
                JHttpLogSender sender = new JHttpLogSender(context);
                sender.sendCrashLog(str.getBytes());

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
