package com.xuzhiyong.comego.ui.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JStringUtils;
import com.xuzhiyong.comego.R;

/***
 * 
 * @author L
 *simple jump to target activity or web from current activity 
 */
public class ActivityUtils {

    public static final int DEFAULT_IN_ANIM = R.anim.activity_right_in;
    public static final int DEFAULT_OUT_ANIM = R.anim.activity_left_out;

	/**
	 * 改成用这个参数结构来jump，里面的字段后续有需要用到的，自己添加不同参数的方法
	 *
	 */
	public static class ActivityJumpParams {
		public Activity fromAct;
		public Fragment fromFrag;
		public Class<?> to;
		public boolean finish;
		public int enterAnim;
		public int exitAnim;
		public Intent intent;
		public ActivityRequestCode requestCode;
		
		public static ActivityJumpParams build(Activity fromAct, Class<?> to) {
			return build(fromAct, to, DEFAULT_IN_ANIM, DEFAULT_OUT_ANIM);
		}
		
		public static ActivityJumpParams build(Activity fromAct, Class<?> to, Bundle b) {
			Intent i = new Intent(fromAct, to);
			i.replaceExtras(b);
			return build(fromAct, false, i, DEFAULT_IN_ANIM, DEFAULT_OUT_ANIM);
		}
		
		public static ActivityJumpParams build(Activity fromAct, Class<?> to, Bundle b, 
				int enterAnim, int exitAnim) {
			Intent i = new Intent(fromAct, to);
			i.replaceExtras(b);
			return build(fromAct, false, i, enterAnim, exitAnim);
		}
		
		public static ActivityJumpParams build(Activity fromAct, Class<?> to, 
				Bundle b, ActivityRequestCode requestCode) {
			Intent i = new Intent(fromAct, to);
			i.replaceExtras(b);
			
			ActivityJumpParams params = build(fromAct, false, i, DEFAULT_IN_ANIM, DEFAULT_OUT_ANIM);
			params.requestCode = requestCode;
			return params;
		}
		
		public static ActivityJumpParams build(Activity fromAct, Class<?> to, ActivityRequestCode requestCode) {
			ActivityJumpParams params = build(fromAct, to, DEFAULT_IN_ANIM, DEFAULT_OUT_ANIM);
			params.requestCode = requestCode;
			return params;
		}

		public static ActivityJumpParams build(Activity fromAct, Class<?> to, ActivityRequestCode requestCode,int enterAnim, int exitAnim) {
			ActivityJumpParams params = build(fromAct, to, enterAnim, exitAnim);
			params.requestCode = requestCode;
			return params;
		}
		
		public static ActivityJumpParams build(Fragment fromFrag, Class<?> to, ActivityRequestCode requestCode) {
			ActivityJumpParams params = build(fromFrag, to, DEFAULT_IN_ANIM, DEFAULT_OUT_ANIM);
			params.requestCode = requestCode;
			return params;
		}
		
		public static ActivityJumpParams build(Activity fromAct, Class<?> to, boolean finish, Bundle b) {
			Intent i = new Intent(fromAct, to);
			i.replaceExtras(b);
			return build(fromAct, finish, i, DEFAULT_IN_ANIM, DEFAULT_OUT_ANIM);
		}
		
		public static ActivityJumpParams build(Fragment fromFrag, Class<?> to) {
			return build(fromFrag, to, DEFAULT_IN_ANIM, DEFAULT_OUT_ANIM);
		}
		
		public static ActivityJumpParams build(Fragment fromFrag, Class<?> to, Bundle b) {
			Intent i = new Intent(fromFrag.getActivity(), to);
			i.replaceExtras(b);
			return build(fromFrag, false, i, DEFAULT_IN_ANIM, DEFAULT_OUT_ANIM);
		}
		
		public static ActivityJumpParams build(Activity fromAct, Class<?> to, int enterAnim, int exitAnim) {
			return build(fromAct, to, false, enterAnim, exitAnim);
		}
		
		public static ActivityJumpParams build(Fragment fromFrag, Class<?> to, int enterAnim, int exitAnim) {
			return build(fromFrag, to, false, enterAnim, exitAnim);
		}
		
		public static ActivityJumpParams build(Activity fromAct, Class<?> to, boolean finish) {
			return build(fromAct, to, finish, DEFAULT_IN_ANIM, DEFAULT_OUT_ANIM);
		}
		
		public static ActivityJumpParams build(Activity fromAct, Class<?> to, boolean finish, int enterAnim, int exitAnim) {
			return build(fromAct, finish, new Intent(fromAct, to), enterAnim, exitAnim);
		}

        public static ActivityJumpParams build(Activity fromAct, Class<?> to, Bundle b, boolean finish, int enterAnim, int exitAnim) {
            Intent i = new Intent(fromAct, to);
            i.replaceExtras(b);
            return build(fromAct, finish, i, enterAnim, exitAnim);
        }

        public static ActivityJumpParams build(Activity fromAct, boolean finish, Intent intent) {
            ActivityJumpParams params = new ActivityJumpParams();

            params.fromAct = fromAct;
            params.finish = finish;
            params.intent = intent;
            params.enterAnim = DEFAULT_IN_ANIM;
            params.exitAnim = DEFAULT_OUT_ANIM;

            return params;
        }
		
		public static ActivityJumpParams build(Activity fromAct, boolean finish, 
				Intent intent, int enterAnim, int exitAnim) {
			ActivityJumpParams params = new ActivityJumpParams();
			
			params.fromAct = fromAct;
			params.finish = finish;
			params.intent = intent;
			params.enterAnim = enterAnim;
			params.exitAnim = exitAnim;
			
			return params;
		}
		
		public static ActivityJumpParams build(Fragment fromFrag, Class<?> to, boolean finish, int enterAnim, int exitAnim) {
			return build(fromFrag, finish, new Intent(fromFrag.getActivity(), to), 
					enterAnim, exitAnim);
		}
		
		public static ActivityJumpParams build(Fragment fromFrag, boolean finish,
		                                       Intent intent, int enterAnim, int exitAnim) {
			ActivityJumpParams params = new ActivityJumpParams();
			
			params.fromFrag = fromFrag;
			params.finish = finish;
			params.intent = intent;
			params.enterAnim = enterAnim;
			params.exitAnim = exitAnim;
			
			return params;
		}
	}
	
	public static void jump(ActivityJumpParams params) {
		
		if(params.fromAct == null) {
            if(params.finish) {
                params.fromFrag.getActivity().finish();
            }

			if(params.requestCode == null) {
				params.fromFrag.startActivity(params.intent);
			} else {
				params.fromFrag.startActivityForResult(params.intent, 
						params.requestCode.intValue());
			}
			
			params.fromFrag.getActivity().overridePendingTransition(params.enterAnim,
					params.exitAnim);
		} else {
            if(params.finish) {
                params.fromAct.finish();
            }

			if(params.requestCode == null) {
				params.fromAct.startActivity(params.intent);
			} else {
				params.fromAct.startActivityForResult(params.intent, 
						params.requestCode.intValue());
			}
			
			params.fromAct.overridePendingTransition(params.enterAnim, params.exitAnim);
		}
	}
	
	public static void jumpWeb(Activity from, String url){
		Uri uri = Uri.parse(url);
		from.startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}
	
	// jump system action
	public static void jumpSystemAction(Activity from, String action){
		Intent intent =new Intent(action);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        from.startActivity(intent);  
	}
	
	public static void jumpNetworkSetting(Activity from){
		jumpSystemAction(from, android.provider.Settings.ACTION_WIRELESS_SETTINGS);
	}
	
	public static void jumpWifiSetting(Activity from){
		jumpSystemAction(from, android.provider.Settings.ACTION_WIFI_SETTINGS);
	}
	
	public static void jumpAppStore(Activity from){
		Intent i = new Intent(Intent.ACTION_VIEW);
		Uri localUri = Uri.parse(JStringUtils.combineStr("market://details?id=",
                from.getPackageName()));
		i.setData(localUri);
        try {
            from.startActivity(i);
        } catch (ActivityNotFoundException e) {
            ThreadBus.bus().post(ThreadBus.Main, new Runnable() {
                @Override
                public void run() {
                    // FIXME: 16-10-25
//                    GToast.show(R.string.market_not_found);
                }
            });
        }
	}
}
