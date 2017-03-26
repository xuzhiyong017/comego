package com.xuzhiyong.comego.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.ui.utils.UIHelper;


/**
 * Created by xuzhiyong on 16-4-9.
 */
public class TopTipsView extends TextView {

    public TopTipsView(Context context) {
        super(context);
        init();
    }

    public TopTipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(0xffff8383);
        setTextColor(getResources().getColor(R.color.white));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        setTypeface(Typeface.DEFAULT_BOLD);
        setGravity(Gravity.CENTER);
        setHeight(UIHelper.dip2px(44f));
        setVisibility(GONE);
    }

    public void show(String text) {
        setText(text);
        removeCallbacks(mAutoHideRunnable);
        if (VISIBLE != getVisibility()) {
            setVisibility(VISIBLE);
            animatorIn();
        }
    }

    public void show(String text, long duration) {
        show(text);
        postDelayed(mAutoHideRunnable, duration);
    }

    public void show(int resId) {
        show(getResources().getString(resId));
    }

    public void show(int resId, long duration) {
        show(getResources().getString(resId), duration);
    }

    public void dismiss() {
        if (getVisibility() == VISIBLE && !mInHiding) {
            animatorOut();
        }
    }

    private Runnable mAutoHideRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    private static final long DURATION = 200L;
    private boolean mInHiding;

    private void animatorIn() {
        clearAnimation();
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(DURATION);
        animation.setInterpolator(new AccelerateInterpolator());
        startAnimation(animation);
        mInHiding = false;
    }

    private void animatorOut() {
        mInHiding = true;
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f);
        animation.setDuration(DURATION);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
                mInHiding = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animation);
    }
}
