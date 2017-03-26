package com.xuzhiyong.comego.ui.base.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

public class GPopupWindow extends PopupWindow {
	
	public static interface OnShowListener {
		public void onShow();
	}
	
	private OnShowListener mOnShowListener;

	public GPopupWindow() {
		// TODO Auto-generated constructor stub
	}

	public GPopupWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public GPopupWindow(View contentView) {
		super(contentView);
		// TODO Auto-generated constructor stub
	}

	public GPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GPopupWindow(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public GPopupWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public GPopupWindow(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public GPopupWindow(Context context, AttributeSet attrs, int defStyleAttr,
						int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}

	public GPopupWindow(View contentView, int width, int height,
						boolean focusable) {
		super(contentView, width, height, focusable);
		// TODO Auto-generated constructor stub
	}

	public void setOnShowListener(OnShowListener listener) {
		mOnShowListener = listener;
	}
	
	@Override
	public void showAsDropDown(View anchor) {
		super.showAsDropDown(anchor);
		
		if(mOnShowListener != null) {
			mOnShowListener.onShow();
		}
	}
	
	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		super.showAsDropDown(anchor, xoff, yoff);
		
		if(mOnShowListener != null) {
			mOnShowListener.onShow();
		}
	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		
		if(mOnShowListener != null) {
			mOnShowListener.onShow();
		}
	}
}
