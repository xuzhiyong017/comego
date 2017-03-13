package com.duowan.fw.util;

import com.duowan.fw.ThreadBus;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by jerryzhou on 15/11/6.
 */
public class JANR implements Runnable{

    protected volatile long ts;
    protected volatile int key;
    protected long delta;
    protected volatile int valid;

    // keep the newest anr checker: the global anr check link list
    public static JANR sANRCur;
    public static byte[] sANRLock = new byte[0];

    /**
     * The ANR LOG IS Created For ANR Only
     * */
    protected static JLog.JLogModule KANRLog;

    public JANR(long delta) {
        this.delta = delta;
        this.valid = 1;

        // log the last anr
        sANRCur = this;
    }

    @Override
    public void run() {
        this.ts = System.currentTimeMillis();
        this.key = 0;

        ThreadBus.bus().post(ThreadBus.Main, new Runnable() {
            @Override
            public void run() {
                key = 1;
            }
        });

        ThreadBus.bus().postDelayed(ThreadBus.PoolTiming, new Runnable() {

            @Override
            public void run() {
                synchronized (sANRLock) {
                    if (JANR.this.valid == 1) {
                        long cur = System.currentTimeMillis();
                        if (key == 0) {
                            onANRHappend(cur - ts);
                        }

                        ThreadBus.bus().post(ThreadBus.PoolTiming, new JANR(JANR.this.delta));
                    }
                }
            }
        }, this.delta);
    }

    /**
     * When Happen ANR, Will Call This
     * */
    protected void onANRHappend(long delta) {
        StringBuilder sb = new StringBuilder();
        sb.append("[############ ANR: take ");
        sb.append(delta);
        sb.append(" ms #############]\n");

        printAllStackTraces(sb.toString(), KANRLog);

        try {
            // there are may be problem
            LogToES.writeThreadLogToFileReal(LogToES.LOG_PATH,
                    KANRLog.logFileName,
                    KANRLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printAllStackTraces(String info, JLog.JLogModule log) {
        JLog.debug(log, captureThreadDump(info));
    }

    public static String captureThreadDump(String msg) {
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
        Iterator<Map.Entry<Thread, StackTraceElement[]>> iterator = allThreads.entrySet().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n\n[########################################################]\n");
        stringBuffer.append("[############# ANR-Happend-CreateDump ###################]\n");
        stringBuffer.append("[########################################################]\n");
        stringBuffer.append(msg);
        while(iterator.hasNext()) {
            Map.Entry<Thread, StackTraceElement[]> entry = iterator.next();
            StackTraceElement[] trace = entry.getValue();
            int cnt = trace.length;
            if (cnt == 1
                    && trace[0].isNativeMethod()) {
                /**
                 * dalvik.system.NativeStart.run(Native Method)
                 * */
                continue;
            }
            if (cnt == 4
                    && trace[0].getMethodName().equals("finalize")
                    && trace[1].getMethodName().equals("doFinalize")) {
                /**
                 * com.android.internal.os.BinderInternal$GcWatcher.finalize(BinderInternal.java:47)
                 * java.lang.Daemons$FinalizerDaemon.doFinalize(Daemons.java:187)
                 * java.lang.Daemons$FinalizerDaemon.run(Daemons.java:170)
                 * java.lang.Thread.run(Thread.java:841)
                 * */
                continue;
            }
            if (cnt == 7
                    && trace[0].getMethodName().equals("sleep")
                    && trace[1].getMethodName().equals("sleep")
                    && trace[cnt-1].getMethodName().equals("run")) {
                /**
                 * java.lang.VMThread.sleep(Native Method)
                 * java.lang.Thread.sleep(Thread.java:1013)
                 * java.lang.Thread.sleep(Thread.java:995)
                 * java.lang.Daemons$FinalizerWatchdogDaemon.sleepFor(Daemons.java:248)
                 * java.lang.Daemons$FinalizerWatchdogDaemon.waitForFinalization(Daemons.java:258)
                 * java.lang.Daemons$FinalizerWatchdogDaemon.run(Daemons.java:212)
                 * java.lang.Thread.run(Thread.java:841)
                 * */
                continue;
            }
            if (cnt <= 6 && cnt >= 3
                   && trace[0].getMethodName().equals("wait")
                    && trace[1].getMethodName().equals("wait")
                    && trace[cnt-1].getMethodName().equals("run")) {
                /**
                 * java.lang.Object.wait(Native Method)
                 * java.lang.Object.wait(Object.java:401)
                 * java.util.Timer$TimerImpl.run(Timer.java:238)
                 *
                 * java.lang.Object.wait(Native Method)
                 * java.lang.Object.wait(Object.java:364)
                 * java.lang.Daemons$ReferenceQueueDaemon.run(Daemons.java:130)
                 * java.lang.Thread.run(Thread.java:841)
                 * */
                continue;
            }
            if (trace.length == 4
                    && trace[0].isNativeMethod()
                    && trace[0].getMethodName().equals("nativePollOnce")) {
                /**
                 *  Skip This Shit
                 * android.os.MessageQueue.nativePollOnce(Native Method)
                 * android.os.MessageQueue.next(MessageQueue.java:136)
                 * android.os.Looper.loop(Looper.java:197)
                 * android.os.HandlerThread.run(HandlerThread.java:61)
                 * */
                continue;
            }

            if (trace.length >= 4
                    && trace[cnt-1].getMethodName().equals("run")
                    && trace[cnt-1].getClassName().equals("java.lang.Thread")) {
                if (trace[0].getMethodName().equals("wait")
                    && trace[1].getMethodName().equals("parkFor")
                        && trace[2].getMethodName().equals("park")) {
                    /**
                     * java.lang.Object.wait(Native Method)
                     * java.lang.Thread.parkFor(Thread.java:1205)
                     * sun.misc.Unsafe.park(Unsafe.java:325)
                     * java.util.concurrent.locks.LockSupport.park(LockSupport.java:159)
                     * java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2019)
                     * java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:413)
                     * java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1013)
                     * java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1073)
                     * java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:573)
                     * java.lang.Thread.run(Thread.java:841)
                     * */
                    continue;
                }
            }

            if (trace.length == 11
                    && trace[5].getMethodName().equals("onANRHappend")) {

                /**
                 * dalvik.system.VMStack.getThreadStackTrace(Native Method)
                 * java.lang.Thread.getStackTrace(Thread.java:579)
                 * java.lang.Thread.getAllStackTraces(Thread.java:521)
                 * com.duowan.fw.util.JANR.captureThreadDump(JANR.java:72)
                 * com.duowan.fw.util.JANR.printAllStackTraces(JANR.java:68)
                 * com.duowan.fw.util.JANR.onANRHappend(JANR.java:55)
                 * com.duowan.fw.util.JANR$2.run(JANR.java:43)
                 * android.os.Handler.handleCallback(Handler.java:730)
                 * android.os.Handler.dispatchMessage(Handler.java:92)
                 * android.os.Looper.loop(Looper.java:213)
                 * android.os.HandlerThread.run(HandlerThread.java:61)
                 * */
                continue;
            }
            String name = entry.getKey().getName();
            stringBuffer.append("[------------thread: "+ name+"\n");
            for(int i = 0; i < trace.length; i++) {
                stringBuffer.append(" "+trace[i]+"\n");
            }
            stringBuffer.append("");
        }
        return stringBuffer.toString();
    }

    /**
     * Start A ANR Recycle
     * */
    public static void startANRCheck(long delta) {
        stopANRCheck();

        synchronized (sANRLock) {
            if (KANRLog == null) {
                // Not In Center, Not Thread Safe
                KANRLog = JLog.JLogModule.makeOne("ANR",
                    "ANR",
                    8192*2,
                    (long)"ANR-log-Key".hashCode(),
                    JLog.JLogFlag_InSoloFile | JLog.JLogFlag_NotInMap);
                // Will Also Link To Default
                KANRLog.linkTo(JLog.KDefault);
            }
            ThreadBus.bus().post(ThreadBus.PoolTiming, new JANR(delta));
        }
    }

    /**
     * Stop The ANR Recycle
     * */
    public static void stopANRCheck() {
        synchronized (sANRLock) {
            if (sANRCur != null) {
                sANRCur.valid = 0;
            }
        }
    }

}
