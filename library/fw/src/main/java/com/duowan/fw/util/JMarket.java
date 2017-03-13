package com.duowan.fw.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class JMarket {
	
	public static Intent getIntent(Context paramContext, String packageName) {
		StringBuilder localStringBuilder = new StringBuilder().append("market://details?id=");
		localStringBuilder.append(packageName);
		Uri localUri = Uri.parse(localStringBuilder.toString());
		Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
		if (paramContext instanceof Activity) {
		}else {
			localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		return localIntent;
	}
	
	public static Intent getIntent(Context paramContext) {
		return getIntent(paramContext, paramContext.getPackageName());
	}

	public static void start(Context paramContext, Uri localUri) {
		Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
		localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		paramContext.startActivity(localIntent);
	}
	
	public static boolean start(Context paramContext, String packageName){
		Intent intent = getIntent(paramContext, packageName);
		boolean exitsMarket = judge(paramContext, intent);
		if (exitsMarket) {
			paramContext.startActivity(intent);
			return true;
		}else {
			return false;
		}
	}

	public static boolean judge(Context paramContext, Intent paramIntent) {
		List<ResolveInfo> localList = paramContext.getPackageManager().queryIntentActivities(paramIntent,PackageManager.GET_INTENT_FILTERS);
		if ((localList != null) && (localList.size() > 0)){
			return true;  
		}else{
			return false;
		} 
	}
}