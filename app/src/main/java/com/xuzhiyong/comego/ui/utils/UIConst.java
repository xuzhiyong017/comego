package com.xuzhiyong.comego.ui.utils;

import android.view.ViewConfiguration;

import com.duowan.fw.Module;

/**
 * 仅限 UI的一些 公用  常量
 *
 * @author yujian
 */
public class UIConst {

    public static final int SCREEN_WIDTH, SCREEN_HEIGHT, STATUS_BAR_HEIGHT, TOUCH_SLOP;

    static {
        //保证width一定是小于height的
        int width = UIHelper.getDispalyMetrics(Module.gMainContext).widthPixels;
        int height = UIHelper.getDispalyMetrics(Module.gMainContext).heightPixels;
        SCREEN_WIDTH = Math.min(width, height);
        SCREEN_HEIGHT = Math.max(width, height);
        STATUS_BAR_HEIGHT = UIHelper.getStatusBarHeight();
        TOUCH_SLOP = ViewConfiguration.get(Module.gMainContext).getScaledTouchSlop();
    }

    public static final int ThumbnailDefaultSmallSize = 256;

    public static final int ThumbnailDefaultBigSize = 512;

    public static final String ThumbnailDefaultSmall = "?ips_thumbnail/3/w/" + ThumbnailDefaultSmallSize + "/h/" + ThumbnailDefaultSmallSize;

    public static final String ThumbnailDefaultBig = "?ips_thumbnail/3/w/" + ThumbnailDefaultBigSize + "/h/" + ThumbnailDefaultBigSize;

}
