package com.duowan.jni;

/**
 * Created by jerryzhou on 15/11/24.
 */
public class JSmartRadix {

    public static native String smartTime(long millis);

    public static native String smartTimeDetail(long millis, int maxunit, int depth);


    public static native String smartDiffTime(long millis, long cur);

    public static native String smartDiffTimeDetail(long millis, long cur, int maxunit, int depth);


    public static native String smartSize(long bytes);

    public static native String smartSizeDetail(long bytes, int maxunit, int depth);


    static {
        System.loadLibrary("jsmartradix");
    }
}
