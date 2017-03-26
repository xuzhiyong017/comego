package com.xuzhiyong.comego.ui.launch;

import android.content.Intent;
import android.os.Bundle;

import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.ui.login.LoginActivity;
import com.xuzhiyong.comego.ui.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(this, LoginActivity.class));
    }
}
