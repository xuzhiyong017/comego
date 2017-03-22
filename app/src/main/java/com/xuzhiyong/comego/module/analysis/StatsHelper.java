package com.xuzhiyong.comego.module.analysis;

import android.app.Activity;
import android.content.Context;

import com.duowan.fw.Module;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.Login.LoginHelper;


import java.util.Map;

public class StatsHelper {

    public static void onPause(Activity activity) {
        DModule.ModuleAnalysis.cast(AnalysisInterface.class).onPause(activity);
    }

    public static void onResume(Activity activity) {
        DModule.ModuleAnalysis.cast(AnalysisInterface.class).onResume(activity);
    }

    public static void reportLogin(long uid) {
        DModule.ModuleAnalysis.cast(AnalysisInterface.class).reportLogin(uid);
    }

    public static void reportTimesEvent(Context context, long uid, String eventId) {
        DModule.ModuleAnalysis.cast(AnalysisInterface.class).reportTimesEvent(context, uid, eventId);
    }

	public static void reportTimesEvent(String eventId) {
		DModule.ModuleAnalysis.cast(AnalysisInterface.class)
				.reportTimesEvent(Module.gMainContext, LoginHelper.getUid(), eventId);
	}

    public static void reportTimesEvent(Context context, long uid, String eventId, String label) {
        DModule.ModuleAnalysis.cast(AnalysisInterface.class).reportTimesEvent(context, uid, eventId, label);
    }

    public static void reportTimesEvent(Context context, long uid, String eventId, String label, Map<String, String> property) {
        DModule.ModuleAnalysis.cast(AnalysisInterface.class).reportTimesEvent(context, uid, eventId, label, property);
    }

    public static void reportCountEvent(Context context, long uid, String eventId, double value, Map<String, String> property) {
        DModule.ModuleAnalysis.cast(AnalysisInterface.class).reportCountEvent(context, uid, eventId, value, property);
    }

}
