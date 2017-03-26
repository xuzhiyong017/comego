package com.xuzhiyong.comego.ui.base.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.xuzhiyong.comego.R;


public class GBottomDialog extends GDialog {

	public GBottomDialog(Context context) {
		super(context, R.style.GBottomDialog);
		
		initBottomDialog();
	}

	public GBottomDialog(Context context, int theme) {
		super(context, theme);
		
		initBottomDialog();
	}
	
	private void initBottomDialog() {
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		getWindow().setGravity(Gravity.BOTTOM);
		setCanceledOnTouchOutside(true);
	}
}
