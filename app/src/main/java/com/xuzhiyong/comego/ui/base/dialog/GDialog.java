package com.xuzhiyong.comego.ui.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.ui.utils.UIHelper;


public class GDialog extends Dialog {

	public GDialog(Context context) {
		super(context, R.style.GDialog);
	}

	public GDialog(Context context, int theme) {
		super(context, theme);
	}

    public <T extends View> T getView(@IdRes int resId) {
        return UIHelper.getView(this, resId);
    }
}
