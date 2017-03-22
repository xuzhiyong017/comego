package com.xuzhiyong.comego.module.app;

import com.duowan.fw.ModuleData;
import com.duowan.fw.kvo.KvoAnnotation;

/**
 * Created by jerryzhou on 15/11/2.
 *
 */
public class AppModuleData extends ModuleData {

    public enum ActivityState {
        ActivityStateOnCreate,
        ActivityStateOnResume,
        ActivityStateOnPause,
        ActivityStateOnStop,
        ActivityStateOnDestroy,
    }

    public static final String Kvo_appOnForeground = "appOnForeground";
    @KvoAnnotation(name = Kvo_appOnForeground)
    public boolean appOnForeground;

}
