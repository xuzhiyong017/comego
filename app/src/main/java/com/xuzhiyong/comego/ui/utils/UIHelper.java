package com.xuzhiyong.comego.ui.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

/**
 * User: Xu zhiyong(18971269648@163.com)
 * Date: 17/3/8
 */
public class UIHelper {

    public static <T extends View> T getView(View parent, int resId) {
        return (T) (parent.findViewById(resId));
    }

    public static <T extends View> T getView(Dialog dialog, int resId) {
        return (T) (dialog.findViewById(resId));
    }

    public static <T extends View> T getView(Activity activity, int resId) {
        return (T) (activity.findViewById(resId));
    }
}
