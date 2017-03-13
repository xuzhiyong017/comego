package com.duowan.fw;

import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JThreadUtil;
import com.duowan.fw.util.JUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
/**
 * Design by JerryZhou@outlook.com, v4.0.0
 * weak delegate 
 * Take care of the delegate leak 
 * */
public class EventDelegate
{
	public static final String LOG_TAG = EventDelegate.class.getName();
	public static JLog.JLogModule KProfileLog = JLog.KProfile;
	public static JLog.JLogModule KEventLog = JLog.KDefault;

	public WeakReference<Object> mTarget;
	public Method mEntry;

	public static boolean haveDelegate(Object obj, String n, Class<?>[] parameterTypes){
		try {
			Method entryMethod = obj.getClass().getDeclaredMethod(n, parameterTypes);
			return entryMethod != null;
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			try {
				Method entryMethod = obj.getClass().getMethod(n, parameterTypes);
				return entryMethod != null;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return mEntry.toString();
	}

	public static EventDelegate buildDelegate(Object obj, String n, Class<?>[] parameterTypes){
		JUtils.jAssert(obj != null);
		EventDelegate delegate = new EventDelegate();

		try{
			delegate.mTarget = new WeakReference<Object>(obj);
			delegate.mEntry = obj.getClass().getDeclaredMethod(n, parameterTypes);
			return delegate;
		}
		catch(Exception e){
			if (e instanceof NoSuchMethodException) {
				try {
					delegate.mEntry = obj.getClass().getMethod(n, parameterTypes);
					return delegate;
				} catch (Exception e1) {
					e1.printStackTrace();
					JLog.error(KEventLog, String.format("Error_Module: buildDelegate failed, %s , %s, %s",
							obj.toString(), n, e.toString()));
					JLog.error(KEventLog, "cause by : " + getTraceInfo(e.getCause()));
					JUtils.jAssert(false);
				}
			}
		}

		return null;
	}

	public boolean invoke(Object[] args){
		Object target = mTarget.get();
		if (target != null){
			try{
				long ts = System.currentTimeMillis();
				mEntry.invoke(target, args);
				methodInvokeProfile(target, args, System.currentTimeMillis() - ts);
			}
			catch(Exception e){
				e.printStackTrace();
				JLog.error(KEventLog, String.format("Error_Module: invoke failed, %s , %s, %s",
						target.toString(), mEntry.toString(),
						e.toString()));
				JLog.error(KEventLog, "cause by : " + getTraceInfo(e.getCause()));
				JUtils.jAssert(false);
			}

			return true;
		}
		return false;
	}

	private static String getTraceInfo(Throwable throwable) {
		StringBuilder sb = new StringBuilder();

		if(throwable != null) {
			sb.append(throwable.toString()).append("\n");

			getStraceTrace(throwable.getStackTrace(), sb);
		} else {
			sb.append("throwable is null \n");
		}

		return sb.toString();
	}

	private static void getStraceTrace(StackTraceElement[] stacks, StringBuilder sb) {
		for (int i = 0; i < stacks.length; i++) {
			if (stacks[i].getClassName().contains("com.duowan.fw")) {
				continue;
			}
			sb.append(stacks[i].toString());
			if (i != stacks.length-1) {
				sb.append("\n");
			}
		}
	}

	public boolean invoke(FwEvent.EventArg event){
		Object target = mTarget.get();
		if (target != null){
			try{
				long ts = System.currentTimeMillis();
				mEntry.invoke(target, event);
				methodInvokeProfile(target, null, System.currentTimeMillis() - ts);
			}
			catch(Exception e){
				e.printStackTrace();
				JLog.error(KEventLog, String.format("Error_Module: invoke failed, %s , %s, %s",
						target.toString(), mEntry.toString(), e.toString()));
				JLog.error(KEventLog, "cause by : " + getTraceInfo(e.getCause()));
				JUtils.jAssert(false);
			}
			return true;
		}
		return false;
	}

	public boolean invoke(Object key, Object args){
		Object target = mTarget.get();
		if (target != null){
			try{
				long ts = System.currentTimeMillis();
				mEntry.invoke(target, key, args);
				methodInvokeProfile(target, args, System.currentTimeMillis() - ts);
			}
			catch(Exception e){
				e.printStackTrace();
				JLog.error(KEventLog, String.format("Error_Module: invoke failed, %s , %s, %s",
						target.toString(), mEntry.toString(), e.toString()));
				JLog.error(KEventLog, "cause by : " + getTraceInfo(e.getCause()));
				JUtils.jAssert(false);
			}
			return true;
		}
		return false;
	}

	private void methodInvokeProfile(Object target, Object args, long spendTime) {
		if(JThreadUtil.isInMainThread() && spendTime > 50){
			StringBuilder sb = new StringBuilder();

			if(spendTime > 500){
				makeProfileInformation(target, spendTime, 500, sb);
			} else if(spendTime > 350){
				makeProfileInformation(target, spendTime, 350, sb);
			} else if(spendTime > 200){
				makeProfileInformation(target, spendTime, 200, sb);
			} else if(spendTime > 100){
				makeProfileInformation(target, spendTime, 100, sb);
			} else if (spendTime > 50) {
				makeProfileInformation(target, spendTime, 50, sb);
			}

			getStraceTrace(Thread.currentThread().getStackTrace(), sb);

			JLog.warn(KProfileLog, sb.toString());
		}
	}

	private void makeProfileInformation(Object target, long spendTime, long threshold, StringBuilder sb) {
				sb.append("[PROFILE - (").append(threshold).append(") ]").append(""
						+ target.getClass().getSimpleName()
						+ ":" + mEntry.getName()
						+ ":" + spendTime + "\n[StakeTrace] \n\t");
	}
}