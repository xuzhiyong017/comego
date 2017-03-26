package com.xuzhiyong.comego.ui.base.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xuzhiyong.comego.R;


/**
 * 俩按钮的对话框
 */
public class GCustomSelectDialog extends GCenterDialog {
	
	private TextView mContentMessage, mLeftBtn, mRightBtn;

	public GCustomSelectDialog(Context context) {
		super(context);
		
		initView();
	}

	public GCustomSelectDialog(Context context, int theme) {
		super(context, theme);
		
		initView();
	}
	
	private void initView() {
		setContentView(R.layout.dialog_gselect);
		
		mContentMessage = (TextView) findViewById(R.id.dgs_content);
		mLeftBtn = (TextView) findViewById(R.id.dgs_left_btn);
		mRightBtn = (TextView) findViewById(R.id.dgs_right_btn);
	}

	public void setContentMessage(CharSequence message) {
		mContentMessage.setText(message);
	}
	
	public void setButtonParams(CharSequence leftText, CharSequence rightText,
								View.OnClickListener leftListener, View.OnClickListener rightListener) {
		mLeftBtn.setText(leftText);
		mLeftBtn.setOnClickListener(leftListener);
		
		mRightBtn.setText(rightText);
		mRightBtn.setOnClickListener(rightListener);
	}
	
	public TextView getLeftBtn() {
		return mLeftBtn;
	}
	
	public TextView getRightBtn() {
		return mRightBtn;
	}
	
	public TextView getContentTextView() {
		return mContentMessage;
	}
}
