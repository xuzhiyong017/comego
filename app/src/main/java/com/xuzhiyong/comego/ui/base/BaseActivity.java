package com.xuzhiyong.comego.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;

import com.xuzhiyong.comego.ui.base.dialog.DialogManager;
import com.xuzhiyong.comego.ui.utils.UIHelper;

import cn.sharesdk.framework.ShareSDK;


/**
 * User: Xu zhiyong(18971269648@163.com)
 * Date: 17/3/21
 */
public class BaseActivity extends Activity {

    private DialogManager mDialogManager;

    public <T extends View> T getView(@IdRes int resId){
        return UIHelper.getView(this,resId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
    }

    public DialogManager getDialogManager() {
        if (mDialogManager == null) {
            mDialogManager = new DialogManager(this);
        }
        return mDialogManager;
    }
}
