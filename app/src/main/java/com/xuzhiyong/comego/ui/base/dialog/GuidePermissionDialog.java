package com.xuzhiyong.comego.ui.base.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuzhiyong.comego.R;


/**
 * Created by Administrator on 2016/5/31 0031.
 */

public class GuidePermissionDialog extends GCenterDialog {

    private TextView mLeftBtn, mRightBtn;
    private LinearLayout mContentView;

    public GuidePermissionDialog(Context context) {
        super(context);
        initView();
    }

    public GuidePermissionDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_custom_permission);
        mContentView = (LinearLayout) findViewById(R.id.content_view);
        mLeftBtn = (TextView) findViewById(R.id.dgcdu_left_btn);
        mRightBtn = (TextView) findViewById(R.id.dgcdu_right_btn);
    }

    public void setContentViewByResId(int resId){
        View view =  LayoutInflater.from(getContext()).inflate(resId,null);
        mContentView.removeAllViews();
        mContentView.addView(view);
    }

    public void setButtonParams(View.OnClickListener leftListener, View.OnClickListener rightListener) {
        mLeftBtn.setOnClickListener(leftListener);
        mRightBtn.setOnClickListener(rightListener);
    }

    public void setButtonText(CharSequence leftText, CharSequence rightText){
        mLeftBtn.setText(leftText);
        mRightBtn.setText(rightText);
    }

}
