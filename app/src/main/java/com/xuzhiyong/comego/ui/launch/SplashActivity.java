package com.xuzhiyong.comego.ui.launch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.ui.login.LoginActivity;
import com.xuzhiyong.comego.ui.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    private static final int COUNT_DOWN = 5;
    private int times = COUNT_DOWN;
    private TextView mCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();


    }

    private void initView() {
        mCountView = getView(R.id.countdown);
        mCountView.post(mTimeCount);
        mCountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountView.removeCallbacks(mTimeCount);
                startActivity();
            }
        });
    }

    public Runnable mTimeCount = new Runnable() {
        @Override
        public void run() {
            mCountView.postDelayed(mTimeCount,1000L);
            mCountView.setText(String.format(getString(R.string.splash_countdown_time),times));
            if(times == 0){
                mCountView.removeCallbacks(mTimeCount);
                startActivity();
            }
            times--;
        }
    };

    public void startActivity(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


}
