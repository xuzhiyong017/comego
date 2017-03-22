package com.xuzhiyong.comego.module.update;

public interface UpdateInterface {

    void updateVersionData(boolean autoCheck);

    void fullDownload();

    void downloadPatch();

    void ignore();

	String getPatchNote();

    String getVersion();

}
