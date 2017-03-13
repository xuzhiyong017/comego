package com.duowan.fw.util;

import android.annotation.SuppressLint;
import java.util.Locale;


public class JVer {
	public int mMajor;
	public int mMinor;
	public int mBuild;

	public boolean bigThan(JVer v) {
		return (mMajor > v.mMajor) || ( (mMajor == v.mMajor) && (mMinor > v.mMinor) )
				|| ( (mMajor == v.mMajor) && (mMinor == v.mMinor) && (mBuild > v.mBuild) );
	}

	public boolean smallThan(JVer v) {
		return (mMajor < v.mMajor) || ( (mMajor == v.mMajor) && (mMinor < v.mMinor) )
				|| ( (mMajor == v.mMajor) && (mMinor == v.mMinor) && (mBuild < v.mBuild) );
	}
	
	
	public boolean equals(Object o) {
		JVer v = (JVer) o;
		return (mMajor == v.mMajor) && (mMinor == v.mMinor)
				&& (mBuild == v.mBuild);
	}

	
	@SuppressLint("DefaultLocale")
	public String toString() {
		return String.format(Locale.getDefault(), "%d.%d.%d", mMajor, mMinor, mBuild);
	}
}
