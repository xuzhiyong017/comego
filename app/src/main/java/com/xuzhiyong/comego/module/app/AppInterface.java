package com.xuzhiyong.comego.module.app;

import android.app.Activity;

import rx.Observable;

/**
 * Created by jerryzhou on 15/11/2.
 *
 */
public interface AppInterface {

    void startup(Activity activity);

    Observable<Class<? extends Activity>> splashing(Activity activity);

    Observable<Class<? extends Activity>> guiding(Activity activity);

    void onMainActivityCreate(Activity activity);

    void startupDone(Activity activity);

    void onLogout();

    Activity getCurrentActivity();

    boolean isAppOnForeground();
}
