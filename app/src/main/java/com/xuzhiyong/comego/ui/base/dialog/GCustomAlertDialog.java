package com.xuzhiyong.comego.ui.base.dialog;

import android.content.Context;
import android.widget.TextView;

import com.xuzhiyong.comego.R;


/**
 * 单按钮弹框
 */
public class GCustomAlertDialog extends GCenterDialog {
	
	private TextView mContentMessage;
	private TextView mButton;

	public GCustomAlertDialog(Context context) {
		super(context);
		
		initView();
	}

	public GCustomAlertDialog(Context context, int theme) {
		super(context, theme);
		
		initView();
	}
	
	private void initView() {
		setContentView(R.layout.dialog_galert);
		
		mContentMessage = (TextView) findViewById(R.id.dga_content);
		mButton = (TextView) findViewById(R.id.dga_btn);
	}
	
	public void setAlertMessage(CharSequence message) {
		mContentMessage.setText(message);
	}
	
	public void setBtnText(CharSequence message) {
		mButton.setText(message);
	}
	
	public void setOnBtnClickListener(android.view.View.OnClickListener listener) {
		mButton.setOnClickListener(listener);
	}
	
	public TextView getAlertBtn() {
		return mButton;
	}
	
	public TextView getContentTextView() {
		return mContentMessage;
	}
}
