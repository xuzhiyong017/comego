package com.xuzhiyong.comego.ui.utils;

import android.app.Activity;
import android.util.Patterns;

import com.duowan.fw.util.JLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppSchemeHandler {

	public static enum AppSchemeRegex {

		// 用户
		user(Pattern.compile("^(http://)?(win.yy.com/u/)([0-9]+)$")) {
			@Override
			public void handle(String data, final Matcher matcher,
			                   Activity act, boolean finish) {
//				UserInfoActivity.gotoUserInfoActivity(Long.valueOf(matcher.group(3)), act);
			}
		},

		weburl(Patterns.WEB_URL) {
			@Override
			public void handle(String data, final Matcher matcher,
			                   Activity act, boolean finish) {
//				WebBrowserActivity.goWebBrowser(act, data.contains("win.yy.com")
//						? data : JStringUtils.combineStr("http://win.yy.com/app/jump.html?url=",
//						data), finish);
			}
		};

		public abstract void handle(final String data, final Matcher matcher,
		                            final Activity act, boolean finish);

		private Pattern mRegex;

		private AppSchemeRegex(Pattern p) {
			mRegex = p;
		}

		public Pattern getPattern() {
			return mRegex;
		}
	}

	public static void handle(final String data, final Activity act) {
		handle(data, act, true);
	}

	public static void handle(final String data, final Activity act, boolean finish) {
		if (data == null) {
			JLog.error(act, "AppSchemeHandler : url is null");
			return;
		}

		for(AppSchemeRegex regex : AppSchemeRegex.values()) {
			Matcher matcher = regex.getPattern().matcher(data);
			if(matcher.matches()) {
				regex.handle(data, matcher, act, finish);
				return;
			}
		}

		JLog.error(act, "can't handle data : " + data);
	}
}
