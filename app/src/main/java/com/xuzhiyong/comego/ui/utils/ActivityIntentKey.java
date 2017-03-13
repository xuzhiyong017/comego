package com.xuzhiyong.comego.ui.utils;

import com.duowan.ada.ui.video.recorder.VideoRecordActivity;

public class ActivityIntentKey {
	
	public static final String USER_ID = "user_id";  //long
	
	public static final String GROUP_ID = "group_id";  //long

    public static final String URL = "url"; // String

    public static final String TITLE = "title"; //String

    public static final String TIMESTAMP = "timestamp"; // long

	public static final String EMPTY_KEY = "empty_key"; //int

	public static final String BET_ID = "bet_id";  //int

    public static final String PHONE_NUMBER = "mobile_phone_number"; // String

    /**
     * @see VideoRecordActivity
     */
    public static final String VIDEO_RECORD_PARMA_KEY = "video_record_param_key"; // String

	//跳转到多个tab页的activity时，先初始化的index
	public static final String FIRST_INIT_PAGE_INDEX = "first_init_page_index";  //int

	public static final String MAIN_IS_FROM_SCHEME = "main_is_from_scheme";

	public static final String INTENT_PROFILE_PATH = "profile_path"; // String
	public static final String INTENT_NEED_CROP = "needCrop"; // boolean

	public static final String PASSWORD_TITLE = "password_title"; //String

	public static final String USER_INFO_LABEL_TYPE = "userInfo_label_type";

    public static final String QUESTION_ID = "question_id"; // long

    public static final String SWITCH_QUESTION = "switch_question"; // boolean

	/**
	 * @see com.duowan.ada.ui.browser.WebBrowserActivity
	 */
	public static final String WEB_BROWSER_URL ="web_browser_url";  //String
	public static final String WEB_BROWSER_TITLE ="web_browser_title";  //String
	public static final String WEB_BROWSER_POST_DATA = "web_browser_post_data"; //String
	public static final String WEB_BROWSER_HIDE_MORE_BUTTON = "web_browser_hide_more_button"; //boolean

}
