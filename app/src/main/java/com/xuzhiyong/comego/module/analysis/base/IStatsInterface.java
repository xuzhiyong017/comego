package com.xuzhiyong.comego.module.analysis.base;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

/**
 * Created by xuzhiyong on 14-12-17.
 */
public interface IStatsInterface {
    void initialize();
    void onPause(Activity activity);
    void onResume(Activity activity);
    void reportLogin(long uid);
    void reportTimesEvent(Context context, long uid, String eventId);
    void reportTimesEvent(Context context, long uid, String eventId, String label);
    void reportTimesEvent(Context context, long uid, String eventId, String label, Map<String, String> property);
    void reportCountEvent(Context context, long uid, String eventId, double value, Map<String, String> property);
}
