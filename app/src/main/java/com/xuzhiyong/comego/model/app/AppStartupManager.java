package com.xuzhiyong.comego.model.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.duowan.ada.R;
import com.duowan.ada.module.DEvent;
import com.duowan.ada.module.datacenter.tables.JUserInfo;
import com.duowan.ada.module.login.LoginHelper;
import com.duowan.ada.ui.main.MainActivity;
import com.duowan.ada.ui.main.SplashActivity;
import com.duowan.ada.ui.utils.ActivityIntentKey;
import com.duowan.ada.ui.utils.ActivityUtils;
import com.duowan.fw.Module;
import com.duowan.fw.util.JStringUtils;

import protocol.UserInfoFlags;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

/**
 * Created by xiehao on 16-4-6.
 */
class AppStartupManager {
    // FIXME: 16-10-25
    private static final int STATE_BEFORE_START = 0;
    private static final int STATE_STARTING = 1;
    private static final int STATE_STARTUP = 2;
    private static final int STATE_RUNNING = 3;
    private int mCurrentState = STATE_BEFORE_START;
    private String mStartUri = null;

    void startup(Activity activity) {
        Uri uri = activity.getIntent().getData();
        if (null != uri && isValidUri(uri.toString())) {
            mStartUri = uri.toString();
        }

        switch (mCurrentState) {
            case STATE_BEFORE_START:
            case STATE_STARTING:
                onFirstStartup(activity);
                break;
            case STATE_STARTUP:
                break;
            case STATE_RUNNING:
                if (null != mStartUri) {
                    onNewIntent(activity);
                }
                break;
            default:
                break;
        }

        activity.finish();
    }

    Observable<Class<? extends Activity>> splashing(Activity activity) {
        final Class<? extends Activity> target;

        target = MainActivity.class;

        return Observable.create(  new OnSubscribe<Class<? extends Activity>>() {
            @Override
            public void call(Subscriber<? super Class<? extends Activity>> subscriber) {
                subscriber.onNext(target);
                subscriber.onCompleted();
            }
        });
    }


    void onMainActivityCreate(Activity activity) {
        switch (mCurrentState) {
            case STATE_STARTING:
            case STATE_STARTUP:
                setCurrentState(STATE_RUNNING);
                if (null != mStartUri) {
                    handleStartUri();
                    mStartUri = null;
                }
                break;
        }
    }

    void startupDone(Activity activity) {
        setCurrentState(STATE_STARTUP);
}


    void onLogout() {
        setCurrentState(STATE_STARTING);
    }

    private void setCurrentState(int state) {
        mCurrentState = state;
    }

    private void onFirstStartup(Activity activity) {
        setCurrentState(STATE_STARTING);

        Intent i = new Intent(activity, SplashActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);
    }

    private void onNewIntent(Activity activity) {
        // TODO: 16-4-6 handle uri
        Intent i = new Intent(activity, MainActivity.class);
        i.putExtra(ActivityIntentKey.FIRST_INIT_PAGE_INDEX, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(ActivityIntentKey.MAIN_IS_FROM_SCHEME, true);
        activity.startActivity(i);
    }

    private boolean isValidUri(String uri) {
        return uri.startsWith(JStringUtils.combineStr(
                Module.gMainContext.getString(R.string.app_scheme),
                "://", Module.gMainContext.getString(R.string.app_host)));
    }

    private boolean showGuide() {
//        String preShowGuideVersionStr = (String) DSetting.globalSetting().getValue(DSetting.Kvo_show_guide_version, null, "");
//        JVer preShowGuideVersion = JVersionUtil.getVerFromStr(preShowGuideVersionStr);
//        JVer currJVer = JVersionUtil.getLocalVer(Module.gMainContext);
//
//        if (preShowGuideVersion == null || currJVer.bigThan(preShowGuideVersion)) {
//            DSetting.globalSetting().setValue(DSetting.Kvo_show_guide_version, null, currJVer.toString());
//            return true;
//        }
        return false;
    }

    private boolean showUpdateUserInfo() {
        // FIXME: 16-11-2
        int flags = JUserInfo.info(LoginHelper.getUid()).flags.getValue();
        return ( flags & UserInfoFlags.UserInfoFlags_NotUserInfo_VALUE)
                == UserInfoFlags.UserInfoFlags_NotUserInfo_VALUE;
    }
    private void handleStartUri() {
    }
}