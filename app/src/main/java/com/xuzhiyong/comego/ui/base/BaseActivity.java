package com.xuzhiyong.comego.ui.base;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

import com.xuzhiyong.comego.utils.UIHelper;


/**
 * User: Xu zhiyong(18971269648@163.com)
 * Date: 17/3/21
 */
public class BaseActivity extends Activity {



    public <T extends View> T getView(@IdRes int resId){
        return UIHelper.getView(this,resId);
    }
}
