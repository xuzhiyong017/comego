package com.xuzhiyong.comego.module.analysis;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.duowan.fw.FwEvent;
import com.duowan.fw.FwEventAnnotation;
import com.duowan.fw.Module;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JConstant;
import com.duowan.fw.util.JFP;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JStringUtils;
import com.duowan.fw.util.JTimeUtils;
import com.duowan.fw.util.JUtils;
import com.duowan.fw.util.LogToES;
import com.xuzhiyong.comego.module.AdaConfig;
import com.xuzhiyong.comego.module.DConst;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.Login.LoginHelper;
import com.xuzhiyong.comego.module.analysis.base.AnalysisFactory;
import com.xuzhiyong.comego.module.analysis.base.ICrashReportInterface;
import com.xuzhiyong.comego.module.analysis.base.IStatsInterface;
import com.xuzhiyong.comego.module.http.HttpHelper;
import com.xuzhiyong.comego.ui.utils.TimeStamp;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



/**
 * Created by xuzhiyong on 14-12-17.
 *
 */
public class AnalysisModule extends Module implements AnalysisInterface {

	private List<IStatsInterface> mStatsHandlers;
	private List<ICrashReportInterface> mCrashHandlers;

	public AnalysisModule() {
		AnalysisModuleData data = new AnalysisModuleData();
		DData.analysisData.link(this, data);

		DEvent.autoBindingEvent(this);

		initialize();
	}

	private void initialize() {
		initStatsHandler();
		initLogHandler();
	}

	private void initStatsHandler() {
		mStatsHandlers = new ArrayList<IStatsInterface>(2);

		if (AdaConfig.config.hiido) {
			mStatsHandlers.add(AnalysisFactory.getStatsHandler(AnalysisFactory.AnalysisType.hiddo));
		}
		if (AdaConfig.config.ym) {
			mStatsHandlers.add(AnalysisFactory.getStatsHandler(AnalysisFactory.AnalysisType.umeng));
		}
        for (IStatsInterface handler : mStatsHandlers) {
            handler.initialize();
        }
	}

	private void initLogHandler() {
		// cover the UncaughtExceptionHandler which set in BaseApp
		Thread.UncaughtExceptionHandler dueh = Thread.getDefaultUncaughtExceptionHandler();
		UncaughtExceptionHandler uEH = new UncaughtExceptionHandler(dueh);
		Thread.setDefaultUncaughtExceptionHandler(uEH);

		mCrashHandlers = new ArrayList<ICrashReportInterface>(2);

		if (AdaConfig.config.default_log) {
			ICrashReportInterface handler = AnalysisFactory.getCrashHandler(AnalysisFactory.AnalysisType.hiddo);
			if (null != handler) {
				handler.initCrashReport();
				mCrashHandlers.add(handler);
			}
		}
		if (AdaConfig.config.ym_crash && AdaConfig.config.ym) {
			mCrashHandlers.add(AnalysisFactory.getCrashHandler(AnalysisFactory.AnalysisType.umeng));
		}
	}

	@Override
	public void onPause(Activity activity) {
		if (!JFP.empty(mStatsHandlers)) {
			for (IStatsInterface handler : mStatsHandlers) {
				handler.onPause(activity);
			}
		}
	}

	@Override
	public void onResume(Activity activity) {
		if (!JFP.empty(mStatsHandlers)) {
			for (IStatsInterface handler : mStatsHandlers) {
				handler.onResume(activity);
			}
		}
	}

	@Override
	public void reportLogin(long uid) {
		if (!JFP.empty(mStatsHandlers)) {
			for (IStatsInterface handler : mStatsHandlers) {
				handler.reportLogin(uid);
			}
		}
	}

	@Override
	public void reportTimesEvent(Context context, long uid, String eventId) {
		if (!JFP.empty(mStatsHandlers)) {
			for (IStatsInterface handler : mStatsHandlers) {
				handler.reportTimesEvent(context, uid, eventId);
			}
		}
	}

	@Override
	public void reportTimesEvent(Context context, long uid, String eventId, String label) {
		if (!JFP.empty(mStatsHandlers)) {
			for (IStatsInterface handler : mStatsHandlers) {
				handler.reportTimesEvent(context, uid, eventId, label);
			}
		}
	}

	@Override
	public void reportTimesEvent(Context context, long uid, String eventId, String label, Map<String, String> property) {
		if (!JFP.empty(mStatsHandlers)) {
			for (IStatsInterface handler : mStatsHandlers) {
				handler.reportTimesEvent(context, uid, eventId, label, property);
			}
		}
	}

	@Override
	public void reportCountEvent(Context context, long uid, String eventId, double value, Map<String, String> property) {
		if (!JFP.empty(mStatsHandlers)) {
			for (IStatsInterface handler : mStatsHandlers) {
				handler.reportCountEvent(context, uid, eventId, value, property);
			}
		}
	}

	@Override
	public void reportCrash(Throwable t) {
		if (!JFP.empty(mCrashHandlers)) {
			for (ICrashReportInterface handler : mCrashHandlers) {
				handler.reportCrash(gMainContext, t);
			}
		}
	}

	@FwEventAnnotation(event = DEvent.E_UserChange)
	public void onUserChange(FwEvent.EventArg event) {
		HashMap<String, String> map = new HashMap<String, String>(2);
		map.put("version_code", String.valueOf(DConst.KC_VersionCode));
		long uid = LoginHelper.getUid();
		if (uid > 0L) {
			map.put("uid", String.valueOf(uid));
//			CrashReport.setExtInfo(map);
		}
	}

	@Override
	public void reportDeviceIdAndMac() {
		String info = getUmengNeedDeviceInfo();
		if (!TextUtils.isEmpty(info)) {
			Map<String, String> prop = new HashMap<String, String>();
			prop.put("content", info);
			reportTimesEvent(Module.gMainContext, LoginHelper.getUid(),
					StatsConst.REPORT_DEVICEID_AND_MAC, null, prop);
		}
	}

	public String getUmengNeedDeviceInfo() {
		try {
			org.json.JSONObject json = new org.json.JSONObject();

            String device_id = JUtils.getImei(Module.gMainContext);

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) Module.gMainContext
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						Module.gMainContext.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			JLog.error(this, "getUmengNeedDeviceInfo failed");
		}

		return null;
	}

	@Override
	public void reportMeBySdk(long gid) {
	}

//	@DNetAnnoation(group = PType.PLogin_VALUE, sub = SPLogin.PUserLoginRes_VALUE, thread = ThreadBus.Whatever, order = 1)
//	public void onLoginProtoAck(Proto proto) {
//		UserLoginRes loginRespond = proto.getBody().userLoginRes;
//
//		if (loginRespond != null && proto.getBody().result.success) {
//			if (loginRespond.logfileupload != null) {
//				uploadLogs(loginRespond.logfileupload);
//			}
//
//			//loglevel 0代表无效
//			if (loginRespond.loglevel != null && loginRespond.loglevel != 0) {
//				JConstant.Log_Level = loginRespond.loglevel;
//			} else {
//				//如果没有这个字段，就重置Log Level
//				//切换帐号的时候
//				JConstant.Log_Level = JConstant.debuggable ? Log.VERBOSE : Log.WARN;
//			}
//		}
//	}

	private void uploadLogs(final long time) {
		final long currentTime = JTimeUtils.getCurrentTime();

		if (currentTime <= time) {
			JLog.error(this, "can't find log files before : " + time
					+ ", currentTime : " + currentTime + " is smaller than time");
			return;
		}

        ThreadBus.bus().post(ThreadBus.Shit, new Runnable() {

            @Override
            public void run() {
                FileFilter fileFilter = new LogFileFilter(time);
                File logDir = new File(LogToES.LOG_PATH);
                File[] logs = logDir.listFiles(fileFilter);

                ArrayList<File> files = new ArrayList<>(logs.length);
                files.addAll(Arrays.asList(logs));

                if (JFP.empty(files)) {
                    JLog.error(this, "can't find log files before : " + time);
                    return;
                }

				TimeStamp currentTs = TimeStamp.parseFromUnixTime(currentTime);
				TimeStamp startTs = TimeStamp.parseFromUnixTime(time);

				String zipFilePath = JStringUtils.combineStr(logDir, File.separator,
						"Log_Android_", LoginHelper.getUid(), "_",
						startTs.getYear(),
						String.format("%02d", startTs.getMonth()),
						String.format("%02d", startTs.getDay()),
						String.format("%02d", startTs.getHour()),
						String.format("%02d", startTs.getMinute()),
						String.format("%02d", startTs.getSecond()), "_",
						currentTs.getYear(),
						String.format("%02d", currentTs.getMonth()),
						String.format("%02d", currentTs.getDay()),
						String.format("%02d", currentTs.getHour()),
						String.format("%02d", currentTs.getMinute()),
						String.format("%02d", currentTs.getSecond()));

				File f = new File(zipFilePath);

				try {
					if (f.exists()) {
						f.delete();
					}
					f.createNewFile();

					zipLogs(files, zipFilePath);
					doUploadLogs(files, zipFilePath);
				} catch (Exception e) {
					JLog.error(this, "upload Logs Failed : " + e);

					//如果压缩文件失败，删除临时压缩文件
					if (f.exists()) {
						f.delete();
					}
				}
			}
		});
	}

    private class LogFileFilter implements FileFilter {

        private long time;

        private LogFileFilter(long time) {
            this.time = time;
        }

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return false;
            }

            String fileName = file.getName();

            if (LogToES.LOG_NAME.equals(fileName)
                    || LogToES.UE_LOG_NAME.equals(fileName)) {
                return true;
            }

            if (!(fileName.startsWith("logs")
                    || fileName.startsWith("ycmedia")
                    || fileName.startsWith("yysdk"))) {
                return false;
            }
            try {
                TimeStamp toUploadTime = TimeStamp.parseFromUnixTime(time);
                TimeStamp ts = TimeStamp.parseFromUnixTime(file.lastModified() / 1000);
                return TimeStamp.compare(ts, toUploadTime) >= 0;
            } catch (Exception e) {
                JLog.error(AnalysisModule.this, "parse log bak file failed : " + e);
                return false;
            }
        }
    }

	private void doUploadLogs(final List<File> files, final String filePath) {
		File logZipFile = new File(filePath);
        HttpHelper.uploadZip(logZipFile, logZipFile.getName(), new HttpHelper.UploadListener() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onUploadSucceed(String key) {
                for(File f : files) {
                    if (f.exists()) {
                        f.delete();
                    }
                }
                reportUploadLogs(HttpHelper.getImageUrl(key));
                JLog.info(this, "upload Logs success");
                //删除生成的压缩文件
				File f = new File(filePath);
				if (f.exists()) {
					f.delete();
				}
            }

            @Override
            public void onUploadFailed(String key, String reason) {
                //暂时不需要加重传，因为每次登录，只要加上这个字段
                //就可以进行上传，直到哪次登录上传成功为止
                //只要有一次上传成功就可以去掉UserLoginRes里的标志位或者删掉文件
                JLog.error(this, "upload logs failed");
				//删除生成的压缩文件
				File f = new File(filePath);
				if (f.exists()) {
					f.delete();
				}
            }

            @Override
            public void onError(Exception error) {
                JLog.error(this, "upload logs error:" + error.getMessage());
				//删除生成的压缩文件
				File f = new File(filePath);
				if (f.exists()) {
					f.delete();
				}
            }
        });
	}

	private void reportUploadLogs(String url) {
//		ReportLogReq req = ReportLogReq.newBuilder().report(url).build();
//		NetRequest builder = NetRequest.newBuilder(PType.PReport,
//				SPReport.PReportLogReq,
//				NetHelper.pbb().reportLogReq(req).build());
//        builder.setResSub(SPReport.PReportLogRes);
//		builder.setTimeOut(DConst.KC_MaxNetOperatorTimeOut);
//		builder.setHandler(new ProtoHandlerWrapper(null));
//		builder.request();
	}

	private void zipLogs(List<File> files, String zipFilePath)
			throws IOException {
		ZipOutputStream zipout = null;
		try {
			zipout = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(zipFilePath), 4 * 1024));

			for (File resFile : files) {
				doZipLog(resFile, zipout);
			}

			zipout.setComment(zipFilePath);
		} finally {
			if (zipout != null) {
				zipout.close();
			}
		}
	}

	private void doZipLog(File resFile, ZipOutputStream zipout)
			throws IOException {
		byte buffer[] = new byte[4096];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				resFile), 4096);
		zipout.putNextEntry(new ZipEntry(resFile.getName()));
		int realLength;
		while ((realLength = in.read(buffer)) != -1) {
			zipout.write(buffer, 0, realLength);
		}
		in.close();
		zipout.flush();
		zipout.closeEntry();
	}
}
