package com.xuzhiyong.comego.ui.base.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JFP;
import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.ui.utils.UIConst;
import com.xuzhiyong.comego.ui.utils.UIHelper;

import java.util.Arrays;
import java.util.List;

public class CommonActionDialog extends GBottomDialog {

    public static class ActionInfo {
        public ActionInfo(int id, int text) {
            this.id = id;
            this.textResId = text;
        }

        public int id;
        public int textResId;
    }

    public interface ICommonActionClickListener {
        void onClicked(CommonActionDialog dialog, ActionInfo info, Object obj);
    }

    private static final int BUTTON_ID_BASE = 0x20ff0800;

    private ICommonActionClickListener mListener;
    private List<ActionInfo> mInfos;
    private Object mObject;
    private TextView mTitle;
    private LinearLayout mContainer;
    private TextView mBtnCancel;

    public CommonActionDialog(Context context, ICommonActionClickListener listener) {
        super(context);
        mListener = listener;
        init();
    }

    public CommonActionDialog(Context context) {
        super(context);
        init();
    }

    public void setCommonActionListener(ICommonActionClickListener l) {
        mListener = l;
    }

    public void setTitle(int resId) {
        mTitle.setText(resId);
        mTitle.setVisibility(View.VISIBLE);
    }

    public void show(List<ActionInfo> infos) {
        if (!JFP.empty(infos)) {
            updateView(infos);
        }
        super.show();
    }

    public void show(List<ActionInfo> infos, Object obj) {
        mObject = obj;
        show(infos);
    }

    public void show(Object obj, ActionInfo... infos) {
        mObject = obj;
        show(Arrays.asList(infos));
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mObject = null;
    }

    @SuppressLint("InflateParams")
    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_common_action, null);
        setContentView(view, new ViewGroup.LayoutParams(
                UIConst.SCREEN_WIDTH,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mTitle = UIHelper.getView(view, R.id.dialog_common_action_title);
        mContainer = UIHelper.getView(view, R.id.dialog_common_action_container);
        mBtnCancel = UIHelper.getView(view, R.id.dialog_common_action_cancel);
        ThreadBus.bus().post(ThreadBus.Main, new Runnable() {
            @Override
            public void run() {
                asyncInit();
            }
        });
    }

    private void asyncInit() {
        mBtnCancel.setOnClickListener(mOnClickListener);
    }

    private void updateView(List<ActionInfo> infos) {
        mInfos = infos;
        mContainer.removeAllViews();

        LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getContext().getResources().getDimensionPixelSize(R.dimen.common_action_dialog_separator_height));
        lineLp.setMargins(0, 0, 0, (int)(5 * getContext().getResources().getDisplayMetrics().density));

        int size = infos.size();
        for (int index = 0; index < size; index++) {

            mContainer.addView(createSeparator());

            ActionInfo info = infos.get(index);
            TextView button = createButton();
            button.setId(BUTTON_ID_BASE + index);
            button.setOnClickListener(mOnClickListener);
            if (info.textResId != 0) {
                button.setText(info.textResId);
            }

            mContainer.addView(button);
        }
    }

    private TextView createButton() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R.layout.common_action_dialog_button, mContainer, false);
    }

    private View createSeparator() {
        return LayoutInflater.from(getContext()).inflate(R.layout.divider, mContainer, false);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() != R.id.dialog_common_action_cancel
                    && null != mListener && null != mInfos) {
                int index = v.getId() - BUTTON_ID_BASE;
                if (index >= 0 && index < mInfos.size()) {
                    mListener.onClicked(CommonActionDialog.this, mInfos.get(index), mObject);
                }
            } else if (v.getId() == R.id.dialog_common_action_cancel) {
                dismiss();
            }
        }
    };
}
