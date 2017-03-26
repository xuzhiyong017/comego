package com.xuzhiyong.comego.ui.base.dialog;

import android.content.Context;
import android.view.View.OnClickListener;


import java.lang.ref.WeakReference;
import java.util.List;

import com.xuzhiyong.comego.ui.base.dialog.CommonActionDialog.ActionInfo;
import com.xuzhiyong.comego.ui.base.dialog.CommonActionDialog.ICommonActionClickListener;


public class DialogManager {

    private GCustomAlertDialog mAlertDialog;
    private GCustomProgressDialog mProgressDlg;
    private GCustomSelectDialog mSelectDlg;
    private CommonActionDialog mBottomDlg;
    private GCustomInputDialog mInputDialog;
    private GuidePermissionDialog mPermissionDialog;

    private final WeakReference<Context> mContext;

    public DialogManager(Context context) {
        mContext = new WeakReference<Context>(context);
    }

    public void showPermissionDialog(CharSequence leftText,
                                     CharSequence rightText,
                                     OnClickListener leftListener,
                                     OnClickListener rightListener,
                                     int resId,boolean cancelable){
        if(mPermissionDialog == null){
            mPermissionDialog = new GuidePermissionDialog(getContext());
        }
        mPermissionDialog.setButtonParams(leftListener,rightListener);
        mPermissionDialog.setContentViewByResId(resId);
        mPermissionDialog.setButtonText(leftText,rightText);
        mPermissionDialog.setCancelable(cancelable);
        mPermissionDialog.show();
    }

    public void showInputDialog(CharSequence title,
                                CharSequence defaultContent,
                                CharSequence leftText,
                                CharSequence rightText,
                                OnClickListener leftListener,
                                OnClickListener rightListener,
                                boolean cancelable) {
        if (mInputDialog == null) {
            mInputDialog = new GCustomInputDialog(getContext());
        }
        mInputDialog.setTitleText(title);
        mInputDialog.setContentText(defaultContent);
        mInputDialog.setButtonParams(leftListener, rightListener);
        mInputDialog.setCancelable(cancelable);
        mInputDialog.show();
    }

	public void showInputDialog(CharSequence title,
								CharSequence defaultContent,
								CharSequence leftText,
								CharSequence rightText,
								OnClickListener leftListener,
								OnClickListener rightListener,
								boolean cancelable,
								int inputMaxLength) {
		if (mInputDialog == null) {
			mInputDialog = new GCustomInputDialog(getContext());
		}
		mInputDialog.setTitleText(title);
		mInputDialog.setContentText(defaultContent);
		mInputDialog.setButtonParams(leftListener, rightListener);
		mInputDialog.setCancelable(cancelable);
		mInputDialog.setMaxLength(inputMaxLength);
		mInputDialog.show();
	}

    public void showSelectDlg(CharSequence message,
                              CharSequence leftText,
                              CharSequence rightText, OnClickListener leftListener,
                              OnClickListener rightListener, boolean cancelable) {
        if (mSelectDlg == null) {
            mSelectDlg = new GCustomSelectDialog(getContext());
        }

        mSelectDlg.setContentMessage(message);
        mSelectDlg.setButtonParams(leftText, rightText,
                leftListener, rightListener);
        mSelectDlg.setCancelable(cancelable);

        mSelectDlg.show();
    }

    public void showAlertDialog(String message, String btnText,
                                OnClickListener listener, boolean cancelable) {
        if (mAlertDialog == null) {
            mAlertDialog = new GCustomAlertDialog(getContext());
        }

        mAlertDialog.setAlertMessage(message);
        mAlertDialog.setBtnText(btnText);
        mAlertDialog.setOnBtnClickListener(listener);
        mAlertDialog.setCancelable(cancelable);

        mAlertDialog.show();
    }

    public void showProgressDialog(String message, boolean cancelable) {
        if (mProgressDlg == null) {
            mProgressDlg = new GCustomProgressDialog(getContext());
        }

        mProgressDlg.setProgressText(message);
        mProgressDlg.setCancelable(cancelable);

        mProgressDlg.show();
    }

    public void showBottomDialog(List<ActionInfo> infos, ICommonActionClickListener l) {
        if (mBottomDlg == null) {
            mBottomDlg = new CommonActionDialog(getContext());
        }

        mBottomDlg.setCommonActionListener(l);
        mBottomDlg.show(infos);
    }

    protected Context getContext() {
        return mContext.get();
    }

    public GCustomProgressDialog getProgressDialog() {
        if (mProgressDlg == null) {
            mProgressDlg = new GCustomProgressDialog(getContext());
        }
        return mProgressDlg;
    }

    public GCustomAlertDialog getAlertDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = new GCustomAlertDialog(getContext());
        }
        return mAlertDialog;
    }

    public GCustomSelectDialog getSelectDialog() {
        if (mSelectDlg == null) {
            mSelectDlg = new GCustomSelectDialog(getContext());
        }
        return mSelectDlg;
    }

    public GCustomInputDialog getInputDialog() {
        if (mInputDialog == null) {
            mInputDialog = new GCustomInputDialog(getContext());
        }
        return mInputDialog;
    }

    public GuidePermissionDialog getPermissionDialog(){
        if(mPermissionDialog == null){
            mPermissionDialog = new GuidePermissionDialog(getContext());
        }
        return mPermissionDialog;
    }

    public void dismissDialogs() {
        dismissAlertDialog();
        dismissProgressDialog();
        dismissSelectDialog();
    }

    public void dismissPermissionDialog(){
        if (mPermissionDialog != null) {
            if(mPermissionDialog.isShowing()) {
                mPermissionDialog.dismiss();
            }
            mPermissionDialog = null;
        }
    }

    public void dismissSelectDialog() {
        if (mSelectDlg != null) {
        	if(mSelectDlg.isShowing()) {
        		mSelectDlg.dismiss();
        	}
            mSelectDlg = null;
        }
    }

    public void dismissAlertDialog() {
        if (mAlertDialog != null) {
        	if(mAlertDialog.isShowing()) {
        		mAlertDialog.dismiss();
        	}
            mAlertDialog = null;
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDlg != null) {
        	if(mProgressDlg.isShowing()) {
        		mProgressDlg.dismiss();
        	}
            mProgressDlg = null;
        }
    }

    public void dismissBottomDialog() {
        if (mBottomDlg != null) {
        	if(mBottomDlg.isShowing()) {
        		mBottomDlg.dismiss();
        	}
            mBottomDlg = null;
        }
    }

    public void dismissInputDialog() {
        if (mInputDialog != null) {
            if(mInputDialog.isShowing()) {
                mInputDialog.dismiss();
            }
            mInputDialog = null;
        }
    }
}