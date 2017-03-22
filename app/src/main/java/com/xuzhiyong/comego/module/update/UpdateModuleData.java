package com.xuzhiyong.comego.module.update;

import com.duowan.fw.ModuleData;
import com.duowan.fw.kvo.KvoAnnotation;

public class UpdateModuleData extends ModuleData {

    public long timestamp = 0L;

    public static final String Kvo_updateData = "updateData";
    @KvoAnnotation(name = Kvo_updateData)
    public UpdateData updateData = null;

	public static final String Kvo_versionCode = "versionCode";
	@KvoAnnotation(name=Kvo_versionCode)
	public int versionCode = 1;
	
	public static final String Kvo_versionName = "versionName";
	@KvoAnnotation(name=Kvo_versionName)
	public String versionName = "1.0";
	
	public static final String Kvo_downloadProgress = "downloadProgress";
	@KvoAnnotation(name=Kvo_downloadProgress)
	public int downloadProgress = 0;
}
