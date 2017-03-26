package com.xuzhiyong.comego.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.duowan.fw.FwEvent;
import com.duowan.fw.FwEventAnnotation;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.kvo.Kvo;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.module.DData;
import com.xuzhiyong.comego.module.DEvent;
import com.xuzhiyong.comego.module.Login.JLoginData;
import com.xuzhiyong.comego.module.Login.LoginHelper;
import com.xuzhiyong.comego.module.Login.LoginModuleData;
import com.xuzhiyong.comego.ui.base.BaseActivity;
import com.xuzhiyong.comego.ui.base.GToast;
import com.xuzhiyong.comego.ui.main.MainActivity;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import protocol.Result;


/**
 * 登陆Activity
 * User: Xu zhiyong(18971269648@163.com)
 * Date: 17/3/8
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{


    private static final String TAG = LoginActivity.class.getSimpleName();
    private ImageView mQQLogin;
    private ImageView mWXLogin;
    Platform mQQPlatform;

    private JLoginData mLoginData;
    private boolean mHasBindLoginState;
    private boolean mInWeixinLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mQQLogin = getView(R.id.qq_login);
        mWXLogin = getView(R.id.weixin_login);
        mQQLogin.setOnClickListener(this);
        mWXLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qq_login:
                loginWithQQ();
                break;
            case R.id.weixin_login:
                Toast.makeText(LoginActivity.this,"登录成功0+"+ mQQPlatform.getDb().getUserId(),Toast.LENGTH_LONG).show();
                break;
        }
    }


    private void loginWithQQ() {
        if (null != mLoginData) {
            Kvo.removeKvoBinding(mLoginData, JLoginData.Kvo_loginResult, this, "onLoginResult");
        }
        getDialogManager().showProgressDialog(getString(R.string.logining), false);
        LoginHelper.loginWithQQ(ShareSDK.getPlatform(this, QQ.NAME), new LoginHelper.LoginWithQQResultListener() {
            @Override
            public void onCancel() {
                loginFinish();
            }
        });
        mLoginData = LoginHelper.curLoginData();
        Kvo.addKvoBinding(mLoginData, JLoginData.Kvo_loginResult, this, "onLoginResult");
        bindEvent();
        ThreadBus.bus().removeCallbacks(ThreadBus.Main, mLoginTimeoutRunnable, null);
        ThreadBus.bus().postDelayed(ThreadBus.Main, mLoginTimeoutRunnable, LoginHelper.LOGIN_TIMEOUT_DURATION);
    }

    private void loginWithWeixin() {
        if (null != mLoginData) {
            Kvo.removeKvoBinding(mLoginData, JLoginData.Kvo_loginResult, this, "onLoginResult");
        }
        getDialogManager().showProgressDialog(getString(R.string.logining), false);
//        LoginHelper.loginWithWeixin(ThirdParty.INSTANCE.getWeixinApi());
        mLoginData = LoginHelper.curLoginData();
        Kvo.addKvoBinding(mLoginData, JLoginData.Kvo_loginResult, this, "onLoginResult");
        bindEvent();
        ThreadBus.bus().removeCallbacks(ThreadBus.Main, mLoginTimeoutRunnable, null);
        ThreadBus.bus().postDelayed(ThreadBus.Main, mLoginTimeoutRunnable, LoginHelper.LOGIN_TIMEOUT_DURATION);
    }

    private void bindEvent() {
        if (!mHasBindLoginState) {
            mHasBindLoginState = true;
            Kvo.addKvoBinding(DData.loginModuleData.data(), LoginModuleData.Kvo_loginState, this, "onLoginStateChange");
        }
    }

    @FwEventAnnotation(event = DEvent.E_ForceLogout)
    public void onForceLogout(FwEvent.EventArg event) {
        getDialogManager().showAlertDialog(event.arg0(String.class), getString(R.string.alert_confirm),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDialogManager().dismissAlertDialog();
                    }
                }, true);
    }

    @KvoAnnotation(name = LoginModuleData.Kvo_loginState, targetClass = LoginModuleData.class, thread = ThreadBus.Main)
    public void onLoginStateChange(Kvo.KvoEvent event) {
        LoginModuleData.LoginState state = event.caseNewValue(LoginModuleData.LoginState.class);
        if (state.equals(LoginModuleData.LoginState.Login_Online)) {
            loginFinish();
//            App.inst().startupDone(this);
        }
    }

    @KvoAnnotation(name = JLoginData.Kvo_loginResult, targetClass = JLoginData.class, thread = ThreadBus.Main)
    public void onLoginResult(Kvo.KvoEvent event) {
        Result result = event.caseNewValue(Result.class);
        if (null != result && !result.success) {
            loginFinish();
            GToast.show(result.reason);
        }
    }


    private void loginFinish() {
        ThreadBus.bus().removeCallbacks(ThreadBus.Main, mLoginTimeoutRunnable, null);
        getDialogManager().dismissProgressDialog();
    }

    private void loginFailed() {
        loginFinish();
        GToast.show(R.string.exception_local_code_login_failed);
    }

    private void doLoginTimeout() {
        if (isFinishing()) {
            return;
        }
        getDialogManager().dismissProgressDialog();
        GToast.show(R.string.login_timeout_retry);
    }

    private Runnable mLoginTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            doLoginTimeout();
        }
    };



    private void finishActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
