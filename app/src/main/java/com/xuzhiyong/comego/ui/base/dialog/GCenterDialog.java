package com.xuzhiyong.comego.ui.base.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

public class GCenterDialog extends GDialog {

	public GCenterDialog(Context context) {
		super(context);
		
		initCenterDialog();
	}

	public GCenterDialog(Context context, int theme) {
		super(context, theme);
		
		initCenterDialog();
	}

	private void initCenterDialog() {
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		getWindow().setGravity(Gravity.CENTER);
		setCanceledOnTouchOutside(true);
	}
}
