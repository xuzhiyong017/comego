package com.xuzhiyong.comego.ui.base;


import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.root.BaseContext;
import com.duowan.fw.util.JThreadUtil;
import com.xuzhiyong.comego.R;
import com.xuzhiyong.comego.module.DModule;
import com.xuzhiyong.comego.module.app.AppInterface;
import com.xuzhiyong.comego.widget.TopTipsView;

import java.lang.reflect.Method;

//import com.duowan.gamego.ui.widget.TopTipsView;

public class GToast {
    // FIXME: 16-10-25
    private static Toast msToast;
    private static TopTipsView msContent;

	private static long DURATION = 1500L;

	public static void show(int resId) {
		show(BaseContext.gContext.getString(resId));
	}
	
	public static void show(CharSequence text) {
		show(text, DURATION, false);
	}

	public static void show(String text, long duration){
		show(text,duration,false);
	}

	public static void show(int resId,long duration){
		show(BaseContext.gContext.getString(resId), duration, false);
	}

	/**
	 * 不管app在不在前台，都会显示
	 * @param resId
	 */
	public static void forceShow(int resId) {
		forceShow(BaseContext.gContext.getString(resId));
	}

	/**
	 * 不管app在不在前台，都会显示
	 * @param text
	 */
	public static void forceShow(CharSequence text) {
		show(text, Toast.LENGTH_SHORT, true);
	}
	
	public static void show(int resId, long duration, boolean forceShow) {
		show(BaseContext.gContext.getString(resId), duration, forceShow);
	}
	
	public static void show(CharSequence text, long duration, boolean forceShow) {
		show(text, duration, 0, 0, forceShow);
	}

	public static void show(final CharSequence text, final long duration,
                            final int xOff, final int yOff, boolean forceShow) {
		// we are background
		if(!forceShow) {
			boolean appOnFroreground = DModule.ModuleApp.cast(AppInterface.class).isAppOnForeground();
			if (!appOnFroreground) {
				return;
			}
		}

        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (JThreadUtil.isInMainThread()) {
            doShow(text, duration, xOff, yOff);
        } else {
            ThreadBus.bus().post(ThreadBus.Main, new Runnable() {
                @Override
                public void run() {
                    doShow(text, duration, xOff, yOff);
                }
            });
        }
	}

    private static void doShow(final CharSequence text, final long duration,
                               int xOff, int yOff) {
        if (msToast == null) {
            createToast();
        }
        msToast.setGravity(Gravity.FILL_HORIZONTAL| Gravity.TOP, 0, 0);
		reflectionsetFullScreen();
		msToast.setDuration(Toast.LENGTH_LONG);
        msToast.show();
        msContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                msContent.show(text.toString(), duration);
            }
        }, 200L);
    }

	private static void createToast() {
		msToast = new Toast(BaseContext.gContext);
        msToast.setView(createToastView(R.layout.view_gtoast));
        msContent = (TopTipsView) msToast.getView().findViewById(R.id.toast_content);
	}

	private static View createToastView(int layoutId) {
		return LayoutInflater.from(BaseContext.gContext).inflate(layoutId, null);
	}





	private static boolean isFullScreen(){
		Activity mCurrent = DModule.ModuleApp.cast(AppInterface.class).getCurrentActivity();
		if(mCurrent != null){
			WindowManager.LayoutParams params = mCurrent.getWindow().getAttributes();
			if((params.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)  == WindowManager.LayoutParams.FLAG_FULLSCREEN){
				return true;
			}
		}
		return false;
	}

	private static void reflectionsetFullScreen(){
		
		try {
			Method getWindowParamsMethod = msToast.getClass().getDeclaredMethod("getWindowParams");
			getWindowParamsMethod.setAccessible(true);
			WindowManager.LayoutParams params = (WindowManager.LayoutParams) getWindowParamsMethod.invoke(msToast);
			if(isFullScreen()){
				params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
			}else{
				params.flags = params.flags &~ WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
