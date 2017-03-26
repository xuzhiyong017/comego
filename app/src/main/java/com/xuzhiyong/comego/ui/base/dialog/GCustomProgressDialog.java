package com.xuzhiyong.comego.ui.base.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuzhiyong.comego.R;


public class GCustomProgressDialog extends GCenterDialog {
	
	private TextView mProgressText;
	private ImageView mAnimView;

	public GCustomProgressDialog(Context context) {
		super(context);
		
		initView();
	}

	public GCustomProgressDialog(Context context, int theme) {
		super(context, theme);
		
		initView();
	}
	
	private void initView() {
		setContentView(R.layout.dialog_gprogress);
		
		mProgressText = (TextView) findViewById(R.id.dgp_text);
		mAnimView = (ImageView) findViewById(R.id.dgp_progress);
		
		setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				if(mAnimView != null) {
					((AnimationDrawable)mAnimView.getDrawable()).start();
				}
			}
		});
		
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(mAnimView != null) {
					((AnimationDrawable)mAnimView.getDrawable()).stop();
				}
			}
		});
	}

	public void setProgressText(String text) {
		mProgressText.setText(text);
	}
}
