package com.xuzhiyong.comego.module.analysis;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

public interface AnalysisInterface {

    // stats
    void onPause(Activity activity);
    void onResume(Activity activity);
    void reportLogin(long uid);
    void reportTimesEvent(Context context, long uid, String eventId);
    void reportTimesEvent(Context context, long uid, String eventId, String label);
    void reportTimesEvent(Context context, long uid, String eventId, String label, Map<String, String> property);
    void reportCountEvent(Context context, long uid, String eventId, double value, Map<String, String> property);

    // crash and log report
    void reportCrash(Throwable t);
    void reportDeviceIdAndMac();

	void reportMeBySdk(long gid);
}
