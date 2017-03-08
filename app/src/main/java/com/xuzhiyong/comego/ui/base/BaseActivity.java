package com.xuzhiyong.comego.ui.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xuzhiyong.comego.ui.utils.UIHelper;

import cn.sharesdk.framework.ShareSDK;


/**
 * User: Xu zhiyong(18971269648@163.com)
 * Date: 17/3/8
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
    }

    public <T extends View> T getView(@IdRes int resId) {
        return UIHelper.getView(this, resId);
    }
}
