package com.duowan.fw.util;

import android.util.Log;

import com.duowan.fw.root.BaseContext;


public final class JConstant {
    // public text screen messages 
    public final static int PUB_TXT_STRING = 0;
    public final static int PUB_TXT_AUDIOOFF = 1;
    
    // !public text screen messages 
    
    public static final int ASSISTANT_IMID = 10;
    public static final int ASSISTANT_UID = 210371277;
    public static final String PHOTO_VERIFY_TAG = "yhsh2:";
    public static final int GALLERY_PICTURE_LIMIT = 8;

    public static final int GET_PHOTO_FROM_CAMERA = 1;
    public static final int GET_PHOTO_FROM_GALLERY = 2;
    public static final int EDIT_PHOTO = 3;
    public static final int TAKE_PHOTO_DIALOG_REQ = 4;
    public static final int SET_PORTRAIT = 5;

    public static final int TYPE_BACK = -1;
    public static final int TYPE_FRIEND = 0;
    public static final int TYPE_STRANGER = 1;
    public static final int TYPE_NEARBY = 2;
    public static final int TYPE_BROADCAST = 3;
    public static final int TYPE_PHONE = 4;

    public static final int TYPE_TEXT_MSG = 1;
    public static final int TYPE_PICTURE_MSG = 2;
    public static final int TYPE_VOICE_MSG = 3;
    public static final int TYPE_VOICE_MSG_OVER_MP = 4;

    public static final int FROM_TEXT_CONV = 1;
    public static final int FROM_VOICE_CONV = 2;
    
    public static final int MAX_PHOTO_SIZE = 800;
    public static final int PORTRAIT_SIZE = 96;
    public static final int PORTRAIT_PICTURE_ID = 1;
    public static final int MIN_PHOTO_SIZE = 200;

    public static final int IMAGE_SCALE_WIDTH = 800;
    public static final int IMAGE_SCALE_HEIGHT = 800;
    public static final int IMAGE_COMPRESS_RATE = 50;
    
    public static final int PhotoWidth = 320;
    public static final float PhotoRoundPx = 8.0f;

    public static final int THUMBNAIL_WIDTH = 320;
    public static final int THUMBNAIL_HEIGHT = 320;
    public static final float THUMBNAIL_SCREEN_SCALE = 0.6f; // displayed thumb nail size, percentage to
                                                             // screen width in pixel
    // Request codes.
    public static final int CHAT_TYPE = 10;
    public static final int REQUESTCODE_PUBLISH_RECORD = 20;
    
    public static final boolean debuggable;
    
    public static int Log_Level = Log.WARN;
    
    //public static boolean powerfulCpuDevice;
    public static boolean enableBusiCard = false;

	static {
		debuggable = JUtils.isDebugMode(BaseContext.gContext);
		Log_Level = JConstant.debuggable ? Log.VERBOSE : JConstant.Log_Level;
	}
}
