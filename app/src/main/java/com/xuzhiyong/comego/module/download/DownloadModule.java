package com.xuzhiyong.comego.module.download;

import android.content.Context;
import android.text.TextUtils;

import com.duowan.fw.Module;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JFileUtils;

import com.mozillaonline.providers.downloads.Downloads;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;

public class DownloadModule extends Module implements DownloadInterface{

    private IDownloadServiceWrapper mDownloadService;

    public DownloadModule() {
        DownloadModuleData data = new DownloadModuleData();
        DData.downloadData.link(this, data);

        mDownloadService = new DownloadManagerWrapper();

        DEvent.autoBindingEvent(this);
    }

    private boolean mQueryStarted = false;

    private DownloadSetup.JDownloadList mDownloads;

    private class DownloadList extends DownloadSetup.JDownloadList {

        public DownloadList(Context _context) {
            super(_context);
        }

        @Override
        public void onDataSetChanged() {

        }
    }

    private synchronized void startQuery() {
        mQueryStarted = true;
        if (null == mDownloads) {
            mDownloads = new DownloadList(gMainContext);
        }
        mDownloads.query();
    }

    private synchronized void tryStopQuery() {
        if (!mDownloads.isDownloading()) {
            mDownloads.close();
            mQueryStarted = false;
        }
    }

    ///////////////////////////////////////////////////////////////////////
    // Download Interface Start
    ///////////////////////////////////////////////////////////////////////
    @Override
    public void tryStopSyncList() {
        tryStopQuery();
    }

    @Override
    public void syncDownloadList() {
        if (!mQueryStarted) {
            if (null == DownloadSetup.manager()) {
                DownloadSetup.startDownloadService(gMainContext);
            }
            startQuery();
        }
    }

    @Override
    public long add(DownloadRequestInfo info) {
        long requestID = mDownloadService.add(info);
        if (requestID > 0) {
            if (!mQueryStarted) {
                startQuery();
            } else {
                mDownloads.query();
            }
        }

        return requestID;
    }

    @Override
    public boolean start(long requestID) {
        return mDownloadService.start(requestID);
    }

    @Override
    public boolean remove(long requestID, boolean removeFile) {
        DownloadSetup.JDownloadInfo info = getInfo(requestID);
        if (info != null && !TextUtils.isEmpty(info.mHint) && info.mHint.startsWith("file://")) {
            final String fileName = info.mHint.substring(7);
            ThreadBus.bus().postDelayed(ThreadBus.Working, new Runnable() {
                @Override
                public void run() {
                    JFileUtils.removeFile(fileName);
                }
            }, 50L);
        }
        boolean ret = mDownloadService.remove(requestID, removeFile);
        if (ret && null != info) {
            info.setValue(DownloadSetup.JDownloadInfo.Kvo_status, Downloads.STATUS_CANCELED);
        }
        return ret;
    }

    @Override
    public void pause(long requestID) {
        mDownloadService.pause(requestID);
    }

    @Override
    public void resume(long requestID) {
        mDownloadService.resume(requestID);
    }

    @Override
    public void restart(long requestID) {
        mDownloadService.restart(requestID);
    }

    @Override
    public DownloadSetup.JDownloadInfo getInfo(long requestID) {
        return mDownloads.downloads.get(requestID);
    }

    @Override
    public long getRequestIDByUrl(String url) {
        if (null != url) {
            for (int index = mDownloads.downloads.size() - 1; index >= 0; index--) {
                DownloadSetup.JDownloadInfo info = mDownloads.downloads.get(index);
                if (null != info && url.equalsIgnoreCase(info.mUri)) {
                    return info.mId;
                }
            }
        }
        return -1L;
    }
    ///////////////////////////////////////////////////////////////////////
    // Download Interface End
    ///////////////////////////////////////////////////////////////////////

}
