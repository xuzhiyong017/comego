package com.xuzhiyong.comego.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.ui.base.BaseActivity;
import com.xuzhiyong.comego.ui.main.MainActivity;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;


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
                Log.d(TAG, "curent main thread id ="+Looper.getMainLooper().getThread());
                mQQPlatform = ShareSDK.getPlatform(this, QQ.NAME);
                mQQPlatform.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Log.d(TAG, "onComplete: "+mQQPlatform.getDb().getUserName());
                        finishActivity();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(Platform platform, int i) {

                    }
                });

                mQQPlatform.authorize();
                break;
            case R.id.weixin_login:
                Toast.makeText(LoginActivity.this,"登录成功0+"+ mQQPlatform.getDb().getUserId(),Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void finishActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
