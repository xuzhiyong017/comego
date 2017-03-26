package com.xuzhiyong.comego.ui.base.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.duowan.fw.Module;
import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.ui.base.GToast;

/**
 * Created by kaibin on 15/10/30.
 *
 */
public class GCustomInputDialog extends GCenterDialog {

	private TextView mTitleText;
	private TextView mLeftBtn, mRightBtn;
	private EditText mInputText;
	private int maxLength = 0;

	public GCustomInputDialog(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		setContentView(R.layout.dialog_custom_input);

		mTitleText = (TextView) findViewById(R.id.dgcdu_title);
		mLeftBtn = (TextView) findViewById(R.id.dgcdu_left_btn);
		mRightBtn = (TextView) findViewById(R.id.dgcdu_right_btn);
		mInputText = (EditText) findViewById(R.id.dgcdu_input);

		mInputText.addTextChangedListener(watcher);
	}

	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (maxLength <= 0){
				return;
			}
			int start = mInputText.getSelectionStart();
			int end = mInputText.getSelectionEnd();
			mInputText.removeTextChangedListener(watcher);

			boolean over = false;
			while (s.toString().length() > maxLength) {
				s.delete(start - 1, end);
				--start;
				--end;
				over = true;
			}

			if (over) {
				GToast.show(String.format(Module.gMainContext.getString(R.string.show_input_limit_tips), maxLength));
//				FloatManager.getInstance().showFloatView(String.format(Module.gMainContext.getString(R.string.show_input_limit_tips), maxLength));
			}

			mInputText.setSelection(start);
			mInputText.addTextChangedListener(watcher);
		}
	};

	public void setButtonParams(View.OnClickListener leftListener, View.OnClickListener rightListener) {

		mLeftBtn.setOnClickListener(leftListener);
		mRightBtn.setOnClickListener(rightListener);
	}

	public void setButtonParams(CharSequence leftText, CharSequence rightText,
								View.OnClickListener leftListener, View.OnClickListener rightListener) {
		mLeftBtn.setText(leftText);
		mRightBtn.setText(rightText);
		mLeftBtn.setOnClickListener(leftListener);
		mRightBtn.setOnClickListener(rightListener);
	}

	public void setTitleText(CharSequence title) {
		mTitleText.setText(title);
	}

	public void setHint(CharSequence content) {
		mInputText.setHint(content);
	}

	public void setContentText(CharSequence content) {
		mInputText.setText(content);
		if (content.length() > 0) {
			mInputText.setSelection(content.length());
		}
	}

	public TextView getLeftBtn() {
		return mLeftBtn;
	}

	public TextView getRightBtn() {
		return mRightBtn;
	}

	public String getInputText() {
		return mInputText.getText().toString();
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}
