package com.duowan.fw.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class LogToES {
	/// data members
	public static String LOG_PATH = "/duowan/ada/log";
	public static final int MAX_FILE_SIZE = 2;// M bytes
	public static final String LOG_NAME = "logs.txt";
	public static final String UE_LOG_NAME = "uncaught_exception.txt";
	public static final String LOG_BAK_FILE_SUFFIX = ".bak";
	public static final String LOG_BAK_FILE_DATE_FORMAT = "-MM-dd-kk-mm-ss";
	public static final SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss.SSS");

	/// init the log path
	static {
		LOG_PATH = JFileUtils.getRootLogDir();
	}

	/// log to file
	@SuppressLint("SimpleDateFormat")
	public synchronized static void writeLogToFileReal(String path, String fileName, String msg)
			throws IOException {

		// get current log file
		File logFile = smartLogFile(path, fileName);

		String strLog = LOG_FORMAT.format(new Date());

		StringBuffer sb = new StringBuffer(strLog);
		sb.append(' ');
		sb.append(msg);
		sb.append('\n');
		strLog = sb.toString();

		// we can make FileWriter static, but when to close it
		FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(logFile, true);
            fileWriter.write(strLog);
            fileWriter.flush();
        } finally {
            if (null != fileWriter) {
                fileWriter.close();
            }
        }
	}
	
	@SuppressLint("SimpleDateFormat")
	public static void writeThreadLogToFileReal(String path, String fileName, JLog.JLogModule info)
			throws IOException {
		// get current log file
		File logFile = info.logFile;
		if (logFile == null || (logFile.length() >>> 20) > MAX_FILE_SIZE) {
			info.logFile = smartLogFile(path, fileName);
			logFile = info.logFile;
		}

		// we can make FileWriter static, but when to close it
		FileWriter fileWriter = null;
		if (info.buffer.ready() > 0 && logFile != null) {
			try {
				fileWriter = new FileWriter(logFile, true);
				if (info.isSharedFile()) {
					fileWriter.write(info.logHeader);
				}
				fileWriter.write(info.buffer.readAllAsString());
				fileWriter.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != fileWriter) {
					fileWriter.close();
				}
			}
		}
	}
	
    private static final long DAY_DELAY = 10 * 24 * 60 * 60 * 1000;

	private static File smartLogFile(String path, String fileName) {

		Date date = new Date();

		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File logFile = new File(path + File.separator + fileName);
		if (logFile.exists() && (logFile.length() >>> 20) > MAX_FILE_SIZE) {
			deleteOldLogs();

			SimpleDateFormat simpleDateFormate = new SimpleDateFormat(LOG_BAK_FILE_DATE_FORMAT);
			String fileExt = simpleDateFormate.format(date);

			StringBuilder sb = new StringBuilder(path);
			sb.append(File.separator).append(fileName).append(fileExt).append(LOG_BAK_FILE_SUFFIX);

			// rename
            File fileNameTo = new File(sb.toString());
            logFile.renameTo(fileNameTo);

			// new file
            logFile = new File(path + File.separator + fileName);
		}

		// make exits
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		return logFile;
	}

    private static void deleteOldLogs() {
        File esdf = Environment.getExternalStorageDirectory();
        if (!esdf.exists()) {
            return;
        }
        String dir = esdf.getAbsolutePath() + LOG_PATH;
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            return;
        }

        long now = System.currentTimeMillis();
        File files[] = dirFile.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(LOG_BAK_FILE_SUFFIX)) {
                long lastModifiedTime = file.lastModified();
                if (now - lastModifiedTime > DAY_DELAY) {
                    file.delete();
                }
            }
        }

    }
	
	private static final SimpleDateFormat FILE_NAME_FORMAT = new SimpleDateFormat(
			"MM-dd_HH-mm-ss");
	private static final String LOGCAT_CMD[] = { "logcat", "-d", "-v", "time" };

	// to use this method, we should add permission(android.permission.READ_LOGS) in Manifest
	public static void writeAllLogsToFile() {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					Date date = new Date();
					Process process = Runtime.getRuntime().exec(LOGCAT_CMD);
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(process.getInputStream()),
							1024);
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						sb.append(line);
						sb.append(System.getProperty("line.separator"));
					}
					bufferedReader.close();
					
					Log.i("huncent-jb", "all logs: "+sb.toString());

					File esdf = Environment.getExternalStorageDirectory();
					String dir = esdf.getAbsolutePath() + LOG_PATH;
					File dirFile = new File(dir);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					String fileName = dir + File.separator + FILE_NAME_FORMAT.format(date) + ".log";
					File file = new File(fileName);
					if (!file.exists()) {
						file.createNewFile();
					}
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(sb.toString().getBytes());
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("huncent-jb", "writeAllLogsToFile " + e.toString());
				}
			}
		}).start();
	}
}
