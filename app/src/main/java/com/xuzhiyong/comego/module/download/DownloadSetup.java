package com.xuzhiyong.comego.module.download;

import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import com.duowan.fw.kvo.Kvo;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.util.JConstCache;
import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.DownloadManager.Request;
import com.mozillaonline.providers.downloads.Constants;
import com.mozillaonline.providers.downloads.DownloadService;
import com.mozillaonline.providers.downloads.Downloads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DownloadSetup {
	
	/****
	 * 1. 调用 startDownloadService() 开启下载服务
	 * 
	 * 2. 通过用 JDownloadList 获取下载列表，
	 *  JDownloadList downloads = new JDownloadList(){
	 *  	public void onDataSetChange(){
	 *  		// do notify ui
	 *  	}
	 *  }
	 *  获取数据：download.query();
	 *  
	 *  不用后必须 调用 download.close();
	 *  
	 *  用下面的函数控制下载
	 *  startDownload
	 *  pauseDownload
	 *  resumeDownload
	 *  removeDownload
	 *  restartDownload
	 * */
	
	public static DownloadManager manager;

	/// 开启下载服务
	public static void startDownloadService(Context context){
		Intent service = new Intent();
		service.setClass(context, DownloadService.class);
		context.startService(service);

		manager = new DownloadManager(context.getContentResolver(), context.getPackageName());
	}

	public static DownloadManager manager(){
		return manager;
	}

	public static long startDownload(String url, String description){
		Uri srcUri = Uri.parse(url);
		Request request = new Request(srcUri);
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS, "/");
		request.setDescription(description);
		return manager.enqueue(request);
	}

	public static long startDownload(Request request){
		return manager.enqueue(request);
	}

	public static void pauseDownload(long id){
		manager.pauseDownload(id);
	}

	public static void resumeDownload(long id){
		manager.resumeDownload(id);
	}

	public static int removeDownload(long id){
		return manager.remove(id);
	}

	public static void restartDownload(long id){
		manager.restartDownload(id);
	}

	public static class JDownloadInfo extends Kvo.KvoSource {
		public long mId;
		public String mUri;
		public boolean mNoIntegrity;
		public String mHint;

        public static final String Kvo_fileName = "mFileName";
        @KvoAnnotation(name = Kvo_fileName)
		public String mFileName;

		public String mMimeType;
		public int mDestination;
		public int mVisibility;
		public int mControl;

        public static final String Kvo_status = "mStatus";
        @KvoAnnotation(name = Kvo_status)
		public int mStatus;

		public int mNumFailed;
		public int mRetryAfter;
		public long mLastMod;
		public String mPackage;
		public String mClass;
		public String mExtras;
		public String mCookies;
		public String mUserAgent;
		public String mReferer;

        public static final String Kvo_totalBytes = "mTotalBytes";
        @KvoAnnotation(name = Kvo_totalBytes)
		public long mTotalBytes;

        public static final String Kvo_currentBytes = "mCurrentBytes";
        @KvoAnnotation(name = Kvo_currentBytes)
		public long mCurrentBytes;

		public String mETag;
		public boolean mDeleted;
		public boolean mIsPublicApi;
		public int mAllowedNetworkTypes;
		public boolean mAllowRoaming;
		public String mTitle;
		public String mDescription;
		public int mBypassRecommendedSizeLimit;

		public int mFuzz;

		public JDownloadInfo(){

		}

        private static JConstCache cache = JConstCache.constCacheWithNameAndControl(JDownloadInfo.class.getName(),
				new JConstCache.CacheControl() {
					@Override
					public Object newObject(Object id) {
						JDownloadInfo info = new JDownloadInfo();
						info.mId = Long.class.cast(id);
						return info;
					}
				});

        public static JDownloadInfo info(long id) {
            JConstCache.CacheResult result = cache.cacheResultForKey(id, true);
            return result.valueOf(JDownloadInfo.class);
        }

		public static class Reader {
			private Cursor mCursor;
			private CharArrayBuffer mOldChars;
			private CharArrayBuffer mNewChars;

			public Reader(Cursor cursor) {
				mCursor = cursor;
			}

			public JDownloadInfo newDownloadInfo(long id) {
				JDownloadInfo info = info(id);
				updateFromDatabase(info);
				return info;
			}

			public void updateFromDatabase(JDownloadInfo info) {
				info.mId = getLong(Downloads._ID);
				info.mUri = getString(info.mUri, Downloads.COLUMN_URI);
				info.mNoIntegrity = getInt(Downloads.COLUMN_NO_INTEGRITY) == 1;
				info.mHint = getString(info.mHint, Downloads.COLUMN_FILE_NAME_HINT);
                info.setValue(JDownloadInfo.Kvo_fileName, getString(info.mFileName, Downloads._DATA));
				info.mMimeType = getString(info.mMimeType, Downloads.COLUMN_MIME_TYPE);
				info.mDestination = getInt(Downloads.COLUMN_DESTINATION);
				info.mVisibility = getInt(Downloads.COLUMN_VISIBILITY);
                info.setValue(JDownloadInfo.Kvo_status, getInt(Downloads.COLUMN_STATUS));
				info.mNumFailed = getInt(Constants.FAILED_CONNECTIONS);
				int retryRedirect = getInt(Constants.RETRY_AFTER_X_REDIRECT_COUNT);
				info.mRetryAfter = retryRedirect & 0xfffffff;
				info.mLastMod = getLong(Downloads.COLUMN_LAST_MODIFICATION);
				info.mPackage = getString(info.mPackage, Downloads.COLUMN_NOTIFICATION_PACKAGE);
				info.mClass = getString(info.mClass, Downloads.COLUMN_NOTIFICATION_CLASS);
				info.mExtras = getString(info.mExtras, Downloads.COLUMN_NOTIFICATION_EXTRAS);
				info.mCookies = getString(info.mCookies, Downloads.COLUMN_COOKIE_DATA);
				info.mUserAgent = getString(info.mUserAgent, Downloads.COLUMN_USER_AGENT);
				info.mReferer = getString(info.mReferer, Downloads.COLUMN_REFERER);
                info.setValue(JDownloadInfo.Kvo_totalBytes, getLong(Downloads.COLUMN_TOTAL_BYTES));
                info.setValue(JDownloadInfo.Kvo_currentBytes, getLong(Downloads.COLUMN_CURRENT_BYTES));
				info.mETag = getString(info.mETag, Constants.ETAG);
				info.mDeleted = getInt(Downloads.COLUMN_DELETED) == 1;
				info.mIsPublicApi = getInt(Downloads.COLUMN_IS_PUBLIC_API) != 0;
				info.mAllowedNetworkTypes = getInt(Downloads.COLUMN_ALLOWED_NETWORK_TYPES);
				info.mAllowRoaming = getInt(Downloads.COLUMN_ALLOW_ROAMING) != 0;
				info.mTitle = getString(info.mTitle, Downloads.COLUMN_TITLE);
				info.mDescription = getString(info.mDescription, Downloads.COLUMN_DESCRIPTION);
				info.mBypassRecommendedSizeLimit =
						getInt(Downloads.COLUMN_BYPASS_RECOMMENDED_SIZE_LIMIT);

				synchronized (this) {
					info.mControl = getInt(Downloads.COLUMN_CONTROL);
				}
			}

			/**
			 * Returns a String that holds the current value of the column, optimizing for the case
			 * where the value hasn't changed.
			 */
			 private String getString(String old, String column) {
				 int index = mCursor.getColumnIndexOrThrow(column);
				 if (old == null) {
					 return mCursor.getString(index);
				 }
				 if (mNewChars == null) {
					 mNewChars = new CharArrayBuffer(128);
				 }
				 mCursor.copyStringToBuffer(index, mNewChars);
				 int length = mNewChars.sizeCopied;
				 if (length != old.length()) {
					 return new String(mNewChars.data, 0, length);
				 }
				 if (mOldChars == null || mOldChars.sizeCopied < length) {
					 mOldChars = new CharArrayBuffer(length);
				 }
				 char[] oldArray = mOldChars.data;
				 char[] newArray = mNewChars.data;
				 old.getChars(0, length, oldArray, 0);
				 for (int i = length - 1; i >= 0; --i) {
					 if (oldArray[i] != newArray[i]) {
						 return new String(newArray, 0, length);
					 }
				 }
				 return old;
			 }

			 private Integer getInt(String column) {
				 return mCursor.getInt(mCursor.getColumnIndexOrThrow(column));
			 }

			 private Long getLong(String column) {
				 return mCursor.getLong(mCursor.getColumnIndexOrThrow(column));
			 }
		}
	}

	public static abstract class JDownloadList{
		public Context context;
		public DownloadManager manager;
		public Cursor dataSortedCursor;
		public JContentObserver contentObservable;
		public JDataSetObserver dataSetObserver;
		public Handler handler;
		
		public Map<Long, JDownloadInfo> downloads = new HashMap<Long, JDownloadInfo>();
		public List<JDownloadInfo> allDownloadInfos = new ArrayList<JDownloadInfo>();

		private class JContentObserver extends ContentObserver {
			public JContentObserver() {
				super(new Handler());
			}

			@Override
			public void onChange(boolean selfChange) {
				handleDownloadsChanged();
			}
		}

		private class JDataSetObserver extends DataSetObserver {
			@Override
			public void onChanged() {
				handleDownloadsChanged();
			}
		}

		public JDownloadList(Context _context){
			manager = DownloadSetup.manager();
			handler = new Handler();
			contentObservable = new JContentObserver();
			dataSetObserver = new JDataSetObserver();
			context = _context;
		}

		private synchronized void handleDownloadsChanged(){
			long now = 0l;//System.currentTimeMillis();
			Set<Long> idsNoLongerInDatabase = new HashSet<Long>(
					downloads.keySet());
			Cursor cursor = null;
			cursor = context.getContentResolver().query(
					Downloads.ALL_DOWNLOADS_CONTENT_URI, null, null, null,
					null);
			//cursor = dataSortedCursor;
			//dataSortedCursor.moveToFirst();

			if (cursor == null) {
				return;
			}

			try {
				allDownloadInfos.clear();

				JDownloadInfo.Reader reader = new JDownloadInfo.Reader(cursor);
				int idColumn = cursor.getColumnIndexOrThrow(Downloads._ID);

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					long id = cursor.getLong(idColumn);
					JDownloadInfo info = downloads.get(id);
					idsNoLongerInDatabase.remove(id);
					if (info != null) {
						updateDownload(reader, info, now);
					} else {
						info = insertDownload(id, reader, now);
					}
					allDownloadInfos.add(info);
				}
			} finally {
				if (cursor != dataSortedCursor) {
					cursor.close();
				}
			}

			for(Long id: idsNoLongerInDatabase){
				downloads.remove(id);
			}
			
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					onDataSetChanged();
				}
			});
		}

		/**
		 * Updates the local copy of the info about a download.
		 */
		private void updateDownload(JDownloadInfo.Reader reader, JDownloadInfo info,
				long now) {
			reader.updateFromDatabase(info);
		}
		/**
		 * Keeps a local copy of the info about a download, and initiates the
		 * download if appropriate.
		 */
		private JDownloadInfo insertDownload(long id, JDownloadInfo.Reader reader, long now) {
			JDownloadInfo info = reader.newDownloadInfo(id);
			downloads.put(info.mId, info);

			return info;
		}

		public void query(){
			if (dataSortedCursor == null) {
				DownloadManager.Query baseQuery = new DownloadManager.Query()
				.setOnlyIncludeVisibleInDownloadsUi(true);
				dataSortedCursor = manager.query(baseQuery);
				dataSortedCursor.registerContentObserver(contentObservable);
				dataSortedCursor.registerDataSetObserver(dataSetObserver);	
			}
			
			handleDownloadsChanged();
			// We Can Do Order By Query
		}

		public void close(){
			try {
				if (dataSortedCursor != null) {
					dataSortedCursor.unregisterContentObserver(contentObservable);
					dataSortedCursor.unregisterDataSetObserver(dataSetObserver);

					dataSortedCursor.close();
					dataSortedCursor = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

        public boolean isDownloading() {
            for (JDownloadInfo info : allDownloadInfos) {
                if (Downloads.isStatusInformational(info.mStatus)) {
                    return true;
                }
            }
            return false;
        }
		
		public abstract void onDataSetChanged();
	}
}
