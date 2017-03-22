package com.xuzhiyong.comego.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

/**
 * Created by xiehao on 16-4-22.
 */
public class PermissionUtils {

    public static final int GROUP_STORAGE = 0xeea001;
    public static final int GROUP_PHONE = 0xeea002;
    public static final int GROUP_LOCATION = 0xeea003;
    public static final int GROUP_CAMERA = 0xeea004;
    public static final int GROUP_MICROPHONE = 0xeea005;

    public static boolean checkPermission(Context context, String permission) {
        if (checkOsVersion()) {
            return true;
        }
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        if (checkOsVersion()) {
            return;
        }
        activity.requestPermissions(new String[]{permission}, requestCode);
    }

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (checkOsVersion()) {
            return;
        }
        activity.requestPermissions(permissions, requestCode);
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        if (checkOsVersion()) {
            return false;
        }
        return activity.shouldShowRequestPermissionRationale(permission);
    }

    private static boolean checkOsVersion() {
        return VERSION.SDK_INT < VERSION_CODES.M;
    }
}
