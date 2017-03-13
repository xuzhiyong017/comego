package com.xuzhiyong.comego.model.app;

import android.app.Activity;

import rx.Observable;

/**
 * Created by jerryzhou on 15/11/2.
 *
 */
public interface AppInterface {

    void startup(Activity activity);

    Observable<Class<? extends Activity>> splashing(Activity activity);

    void onMainActivityCreate(Activity activity);

    void startupDone(Activity activity);

    void onLogout();

    Activity getCurrentActivity();

    boolean isAppOnForeground();
}
