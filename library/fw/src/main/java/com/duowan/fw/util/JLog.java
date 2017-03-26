package com.duowan.fw.util;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.duowan.jni.JRingBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Design by JerryZhou@outlook.com, v3.0.0
 *
 * (1) Support To Add A RingBuffer as the memory cache for log before write to file
 * (2) Support JLogModule to do logs in one ant solo file
 * (3) Support no mutex thread log
 * */
public class JLog {
    public static final String TAG = "xuzhiyong-comego";

    // global log module
    public static final JLogModule KProfile;
    public static final JLogModule KEvent;
    public static final JLogModule KFw;
    public static final JLogModule KDefault;
    public static final JLogModule KError;

    // log flag
    static final int JLogFlag_InSoloFile = 1;
    static final int JLogFlag_ThreadSafe = 1<<1;
    static final int JLogFlag_InCurrentThread = 1<<2;
    static final int JLogFlag_InLogLink = 1<<3;

    static final int JLogFlag_Disable = 1<<4;
    static final int JLogFlag_NotInMap = 1<<5;

    // log module
    public static class JLogModule {

        // cache
        public JRingBuffer buffer;

        // thread info
        public String name;
        public String logTag;

        // flag
        public int flag;

        // log control
        public String logHeader;
        public String logFileName;

        // log file
        File logFile;

        // all linked
        LinkedList<JLogModule> linked;

        // log depth resolve log recycle
        public volatile int logDepth;

        // init all basic information
        protected void init(int bufferSize, String xname, String xlogtag, int xflag) {
            this.buffer = new JRingBuffer(bufferSize, JRingBuffer.JRBFlag.JRB_Override.getFlag());
            this.name = xname;
            this.flag = xflag;
            this.logTag = xlogtag;
            this.logDepth = 0;

            // header
            StringBuilder sb = new StringBuilder("\n\n");
            sb.append("[##########################################################]\n")
                    .append(String.format("[#########%s#########]\n", this.logTag))
                    .append("[##########################################################]\n");
            this.logHeader = sb.toString();

            // log files
            if ((this.flag & JLogFlag_InSoloFile) != 0) {
                if (this.name.length() > 0) {
                    this.logFileName = String.format("logs-%s.txt", this.name);
                } else {
                    this.logFileName = LogToES.LOG_NAME;
                }

                // start up log
                String date = DateFormat.getDateTimeInstance().format(new Date());
                StringBuilder startSb = new StringBuilder("\n");
                startSb.append("[--------------------------------StartUp--------------------------------]\n")
                        .append(String.format("[----------------%s (%s)----------------]\n", this.logTag, date))
                        .append("[-----------------------------------------------------------------------]\n");
                this.buffer.write(startSb.toString().getBytes());

            } else {
                this.logFileName = LogToES.LOG_NAME;
            }
        }

        public boolean isSharedFile() {
            return (this.flag & JLogFlag_InSoloFile) == 0;
        }

        public boolean isNeedLock() {
            return (this.flag & JLogFlag_ThreadSafe) != 0;
        }

        public void log(String msg) {
            // if disable the log
            if ((this.flag & JLogFlag_Disable) != 0) {
                return;
            }

            // if we are already in it
            if (this.logDepth >= 1)  {
                return;
            }

            // generate the bytes
            byte[] bytes = JStringUtils.combineStr(LogToES.LOG_FORMAT.format(new Date()),
                        ' ', msg, '\n').getBytes();

            try {
                // current log
                this.logDepth++;

                // log to cache
                if (this.isNeedLock()) {
                    synchronized (this) {
                        this.buffer.write(bytes);
                    }
                } else {
                    this.buffer.write(bytes);
                }

                // log current thread
                if ((this.flag & JLogFlag_InCurrentThread) != 0) {
                    threadLogModule().log(msg);
                }

                // log other linked
                if ((this.flag & JLogFlag_InLogLink) != 0) {
                    for (JLogModule sub : this.linked) {
                        sub.log(msg);
                    }
                }
            }finally {
                this.logDepth--;
            }
        }

        /**
         * All Log this module receive will Link To sub module
         * */
        public void linkTo(JLogModule sub) {
            this.flag |= JLogFlag_InLogLink;
            if (this.linked == null) {
                this.linked = new LinkedList<JLogModule>();
            }
            this.linked.addLast(sub);
        }

        /**
         * */
        public static JLogModule makeOne(String name) {
            return makeOne(name, sLogBufferSize);
        }

        /**
         * */
        public static JLogModule makeOne(String name, int buffersize) {
            return makeOne(name,
                    name,
                    buffersize,
                    (long)name.hashCode(),
                    JLogFlag_InSoloFile | JLogFlag_ThreadSafe);
        }

        /**
         * */
        public static JLogModule makeOne(String name, String tag, int buffersize, long key, int flag) {
            if (sLogMap.containsKey(key)) {
                return null;
            }
            JLogModule module = new JLogModule();
            module.init(buffersize, name, tag, flag);
            if ((flag & JLogFlag_NotInMap) == 0){
                sLogMap.put(key, module);
            }
            return module;
        }
    }

    /**
     * global log module
     * */
    private static Map<Long, JLogModule> sLogMap;
    private static HandlerThread mWriteFileThread;
    private static Handler sWriteFileHandler;
    private static Runnable sWriteFileRunnable;
    private static long writeFileInterval;
    private static int sLogBufferSize;

    static {
        sLogMap = new ConcurrentHashMap<Long, JLogModule>();
        mWriteFileThread = new HandlerThread("write-log-file");
        writeFileInterval = JConstant.Log_Level >= Log.INFO ? (30 * 1000) : (10 * 1000);
        sLogBufferSize = JConstant.Log_Level >= Log.INFO ? 8192 : 8192 * 2;

        mWriteFileThread.start();
        sWriteFileHandler = new Handler(mWriteFileThread.getLooper());
        sWriteFileRunnable = new Runnable() {

            @Override
            public void run() {
                writeAllBufferToFile();

                sWriteFileHandler.postDelayed(sWriteFileRunnable, writeFileInterval);
            }
        };

        sWriteFileHandler.postDelayed(sWriteFileRunnable, writeFileInterval);

        KDefault = JLogModule.makeOne("", sLogBufferSize * 32); // 8 * 32 = 256 Kb
        KProfile = JLogModule.makeOne("Profile");
        KError = JLogModule.makeOne("Error");
        KEvent = KDefault;  //JLogModule.makeOne("Event");
        KFw = KDefault;     //JLogModule.makeOne("Fw");

        // the profile logs also will deliver to default
        KProfile.linkTo(KDefault);
        // the error logs alsow will deliver to default
        KError.linkTo(KDefault);
    }

    public static void writeAllBufferToFile() {
        Iterator<Map.Entry<Long, JLogModule>> iter = sLogMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Long, JLogModule> entry = (Map.Entry<Long, JLogModule>) iter.next();

            JLogModule info = entry.getValue();
            if (info.buffer.ready() > 0) {
                try {
                    LogToES.writeThreadLogToFileReal(LogToES.LOG_PATH, info.logFileName,
                            info);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Output a verbose debug log.
     * This version aims to improve performance by 
     * removing the string concatenated costs on release version.
     * @param obj
     * @param format	The format string, such as "This is the %d sample : %s".
     * @param args		The args for format.
     */
    public static void verbose(Object obj, String format, Object ... args) {
    	if (JConstant.Log_Level <= Log.VERBOSE) {
    		try {
    			log(obj, Log.VERBOSE, msgForTextLog(obj, getCaller(), String.format(format, args)));
    		} catch (java.util.IllegalFormatException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public static void verbose(Object obj, String msg) {
    	if (JConstant.Log_Level <= Log.VERBOSE) {
    		log(obj, Log.VERBOSE, msgForTextLog(obj, getCaller(), msg));
    	}
    }

    /**
     * Output debug log.
     * This version aims to improve performance by 
     * removing the string concatenated costs on release version.
     * Exception will be caught if input arguments have format error.
     * @param obj
     * @param format	The format string such as "This is the %d sample : %s".
     * @param args		The args for format.
     * 
     * Reference : 
     * boolean : %b.
     * byte, short, int, long, Integer, Long : %d.
     * String : %s.
     * Object : %s, for this occasion, toString of the object will be called,
     * and the object can be null - no exception for this occasion.
     * 
     */
    public static void debug(Object obj, String format, Object ... args) {
    	if (JConstant.Log_Level <= Log.DEBUG) {
    		try {
    			log(obj, Log.DEBUG, msgForTextLog(obj, getCaller(), String.format(format, args)));
    		} catch (java.util.IllegalFormatException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public static void debug(Object obj, String msg) {
    	if (JConstant.Log_Level <= Log.DEBUG) {
    		log(obj, Log.DEBUG, msgForTextLog(obj, getCaller(), msg));
    	}
    }

    public static void info(Object obj, String msg) {
    	if (JConstant.Log_Level <= Log.INFO) {
    		log(obj, Log.INFO, msgForTextLog(obj, getCaller(), msg));
    	}
    }
    
    /**
     * Output information log.
     * Exception will be caught if input arguments have format error.
     * @param obj
     * @param format    The format string such as "This is the %d sample : %s".
     * @param args      The args for format.
     * 
     * Reference : 
     * boolean, Boolean : %b or %B.
     * byte, short, int, long, Integer, Long : %d.
     * String : %s.
     * Object : %s, for this occasion, toString of the object will be called,
     * and the object can be null - no exception for this occasion.
     * 
     */
    public static void info(Object obj, String format, Object ... args) {
    	if (JConstant.Log_Level <= Log.INFO) {
    		try {
    			log(obj, Log.INFO, msgForTextLog(obj, getCaller(), String.format(format, args)));
    		} catch (java.util.IllegalFormatException e) {
    			e.printStackTrace();
    		}
    	}
    }

    public static void warn(Object obj, String format, Object ... args) {
    	if (JConstant.Log_Level <= Log.WARN) {
    		try {
    			log(obj, Log.WARN, msgForTextLog(obj, getCaller(), String.format(format, args)));
    		} catch (java.util.IllegalFormatException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public static void warn(Object obj, String msg) {
    	if (JConstant.Log_Level <= Log.WARN) {
    		log(obj, Log.WARN, msgForTextLog(obj, getCaller(), msg));
    	}
    }

    public static void error(Object obj, String format, Object ... args) {
    	if (JConstant.Log_Level <= Log.ERROR) {
    		try {
    			log(obj, Log.ERROR, msgForTextLog(obj, getCaller(), String.format(format, args)));
    		} catch (java.util.IllegalFormatException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public static void error(Object obj, String msg) {
    	if (JConstant.Log_Level <= Log.ERROR) {
            log(obj, Log.ERROR, msgForTextLog(obj, getCaller(), msg));
    	}
    }

    public static void error(Object obj, Throwable t) {
		if (JConstant.Log_Level <= Log.ERROR) {
			String logText = msgForException(obj, getCaller());
			
			Log.e(TAG, logText, t);
			
			if (JUtils.externalStorageExist()) {
		        StringWriter sw = new StringWriter();
		        sw.write(logText);
		        sw.write("\n");
		        t.printStackTrace(new PrintWriter(sw));
				logToFile(obj, sw.toString());
			}
		}
    }

    /**
     * */
    public static String getCurrentStackTrace() {
        StringBuilder sb = new StringBuilder(4096);
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i].getClassName().contains("com.duowan.fw")) {
                continue;
            }
            sb.append(stacks[i].toString());
            if (i != stacks.length-1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    /**
     * 
     * @param logLevel  must between Log.VERBOSE and Log.ERROR
     * @param logText
     */
    private static String[] sLogLevelTags = {"", "",
            /*VERBOSE*/ "VERBOSE",
            /*DEBUG*/   "DEBUG",
            /*INFO*/    "INFO",
            /*WARN*/    "WARN",
            /*ERROR*/   "ERROR",
            /*ASSERT*/  "ASSERT",
    };
    private static void log(Object obj, int logLevel, String logText) {
    	Log.println(logLevel, TAG, logText);
    	
    	if(JUtils.externalStorageExist()) {
            // log it
            String msg = String.format("[%s] %s", sLogLevelTags[logLevel], logText);
    		logToFile(obj, msg);

            // if error will log to a error file
            if (logLevel >= Log.ERROR && obj != KError) {
                writeLog(KError, msg);
            }
    	}
    }

    /***
     *
     * @param logLevel
     * @param logText
     * @param module
     * */
    private static void log(JLogModule module, int logLevel, String logText) {
        Log.println(logLevel, TAG, logText);

        if(JUtils.externalStorageExist()) {
            logToFile(module, logText);
        }
    }
    
    private static String objClassName(Object obj) {
    	if (obj == null) {
    		return "Global";
		}
        if (obj instanceof String)
            return (String) obj;
        else
            return obj.getClass().getSimpleName();
    }

    private static long LEN_128K = ((1 << 10) * 128); // 
    private static long LEN_256K = ((1 << 10) * 256); // 

    public static String getFeedBackLogString() {
        File esdf = Environment.getExternalStorageDirectory();
        String dir = esdf.getAbsolutePath() + LogToES.LOG_PATH;
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File logFile = new File(dir + File.separator + LogToES.LOG_NAME);
        if (!logFile.exists()) {
            return null;
        }
        String nettype = JUtils.getNetworkType();
        long len = 0l;
        if(nettype.equals(JUtils.NetworkType.Mobile3G)
                || nettype.equals(JUtils.NetworkType.Wifi)){
            len = LEN_256K;
        }else {
            len = LEN_128K;
        }
        
        byte[] buf = new byte[(int) len];
        len = logFile.length();
        FileInputStream fin = null;
        try {            
            fin = new FileInputStream(logFile);
            if (len > buf.length) {
                fin.skip(len - buf.length);
            }
            int read = 0;
            while (true) {
                int tmp = fin.read(buf, read, buf.length - read);
                if (tmp <= 0)
                    break;
                read += tmp;
            }
            return new String(buf);
        }
        catch (Exception e) {
            return null;
        }
        finally {
            if (null != fin) {
                try {
                    fin.close();
                }
                catch (Exception e) {
                }
            }
        }
    }

    private static void logToFile(Object module, String logText) {
        if (module != null && module instanceof JLogModule) {
            writeLog(JLogModule.class.cast(module), logText);
        } else {
            writeThreadLog(logText);
        }
    }

    private static String msgForException(Object obj, String caller) {
        StringBuilder sb = new StringBuilder();
        if (obj instanceof String)
            sb.append((String) obj);
        else
            sb.append(obj.getClass().getSimpleName());
        sb.append(" Exception occurs at ");
        sb.append(caller);
        String ret = sb.toString();
        return ret;
    }

    private static String msgForTextLog(Object obj, String caller, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(msg);
        sb.append(" at ");
        sb.append(caller);
        String ret = sb.toString();
        return ret;
    }

    private static int getCallerLineNumber() {
        return Thread.currentThread().getStackTrace()[4].getLineNumber();
    }

    private static String getCallerFilename() {
        return Thread.currentThread().getStackTrace()[4].getFileName();
    }

    private static String getCallerMethodName() {
        return Thread.currentThread().getStackTrace()[4].getMethodName();
    }

    private static String getCaller() {
        return Thread.currentThread().getStackTrace()[4].toString();
    }

    // thread local module
    private static ThreadLocal<JLogModule> sThreadLocal = new ThreadLocal<JLogModule>();

    private static String[] ShouldBeLogInThreadFile = {"-J-"};

    private static JLogModule makeThreadOne(Thread thread) {
        String tname = thread.getName();
        String name = String.format("thread:%s", tname);
        String tag = String.format("thread:%s(%d)-priority:%d", thread.getName(),
                thread.getId(),
                thread.getPriority());
        /**
         * https://en.wikipedia.org/wiki/Filename
         * http://stackoverflow.com/questions/4814040/allowed-characters-in-filename
         *
         * NB! Some MODI Android System Not Allowed ':' in file name
         * */
        name = name.replace(':', '-');

        int flag = 0;
        for (String key: ShouldBeLogInThreadFile ) {
            if (tname.contains(key)) {
                flag |= JLogFlag_InSoloFile;
                break;
            }
        }
        JLogModule module = JLogModule.makeOne(name, tag, sLogBufferSize, thread.getId(), flag);

        // need to link default
        if (tname.contains("-D-")) {
            module.linkTo(KDefault);
        }
        return module;
    }

    public static JLogModule threadLogModule() {
        JLogModule info = sThreadLocal.get();

		if(info == null) {
            info = makeThreadOne(Thread.currentThread());
			sThreadLocal.set(info);
		}
        return info;
    }

	private static void writeThreadLog(String msg) {
        JLogModule info = threadLogModule();
        if (info.isSharedFile()) {
            info = KDefault;
        }
        writeLog(info, msg);
	}

    private static void writeLog(JLogModule info, String msg) {
        info.log(msg);
    }
}
