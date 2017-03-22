package com.xuzhiyong.comego.module.download;


import com.duowan.fw.Module;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;

public class DownloadModule extends Module implements DownloadInterface{

    private IDownloadServiceWrapper mDownloadService;

    public DownloadModule() {
        DownloadModuleData data = new DownloadModuleData();
        DData.downloadData.link(this, data);


        DEvent.autoBindingEvent(this);
    }


}
