package com.xuzhiyong.comego.module.analysis;

public class StatsConst {

    public static final String LAUNCH_APP = "launch_app"; //打开客户端

    /// login
    public static final String USER_LOGIN = "user_login";
    public static final String UID = "uid";

    /// DB
    public static final String DATABASE_ERROR = "database_error";
    public static final String NULL_WDB = "null_wdb";
    public static final String NULL_RDB = "null_rdb";

    //pay
    public static final String PAY_ERROR = "pay_error";

    /// update
    public static final String UPDATE_CHECK_SUCCESS = "update_check_success";
    public static final String UPDATE_CHECK_FAILED = "update_check_failed";
    public static final String UPDATE_FIND_PATCH = "update_find_patch";
    public static final String UPDATE_USER_CANCEL = "update_user_cancel";
    public static final String UPDATE_DOWNLOAD_SUCCESS = "update_download_success";
    public static final String UPDATE_DOWNLOAD_FAILED = "update_download_failed";
    public static final String UPDATE_APPLY_SUCCESS = "update_apply_success";
    public static final String UPDATE_APPLY_FAILED = "update_apply_failed";
    public static final String UPDATE_INSTALL_SUCCESS = "update_install_success";
    public static final String UPDATE_INSTALL_FAILED = "update_install_failed";

    public static final String CHECK_TIMEOUT = "timeout";
    public static final String IO_EXCEPTION = "ioException";
    public static final String JSON_SYNTAX_ERROR = "json_syntax_exception";
    public static final String LATEST_VERSION = "latest_version";
    public static final String DOWNLOAD_FAILED_CODE = "download_failed_";
    public static final String APPLY_ERROR = "apply_";

    //上报友盟注册测试设备需要的deviceId和mac
    public static final String REPORT_DEVICEID_AND_MAC = "report_deviceid_and_mac";

    public static final String DECRYPT_EXCEPTION = "decrypt_exception";

	//登录
	public static final String GO_LOGIN = "go_login";
	//搜索用户
	public static final String SEARCH_USER = "search_user";
	//点击分享
	public static final String CLICK_SHARE = "click_share";
	//点击联系人
	public static final String CLICK_CONTACT = "click_contact";

    /// MINE
    public static final String MINE_CLICK_PORTRAIT = "mine_click_portrait"; //点击头像转至个人主页
    public static final String MINE_CLICK_EDIT = "mine_click_edit"; //个人主页-点击编辑
    public static final String MINE_COMMIT_EDIT = "mine_commit_edit"; //编辑完成个人信息并提交
    public static final String MINE_CLICK_WEALTH = "mine_click_wealth"; //点击我的财富
    public static final String MINE_CLICK_TASK = "mine_click_task"; //点击我的任务
    public static final String MINE_CLICK_SETTING = "mine_click_setting"; //点击设置
    public static final String MINE_CLICK_CLEAN_CACHE = "mine_click_clean_cache"; //设置-点击清理缓存
    public static final String MINE_CLICK_ABOUT = "mine_click_about"; //点击关于
    public static final String MINE_CLICK_UPDATE = "mine_click_update"; //关于-点击检查更新
    public static final String MINE_CLICK_FEEDBACK = "mine_click_feedback"; //关于-点击意见反馈
    public static final String MINE_CLICK_QA = "mine_click_qa"; //关于-点击常见问题

	public static final String LISTVIEW_INDEXOUTOFBOUNDS_EXCEPTION = "listview_indexoutofbounds_exception";
	public static final String LISTVIEW_ONINITIALIZEACCESSIBILITY_EXCEPTION = "listview_oninitializeaccessibility_exception";
	public static final String LISTVIEW_ONINITIALIZEACCESSIBILITYNODEINFO_EXCEPTION = "listview_oninitializeaccessibilitynodeinfo_exception";

	public static final String YCLOUD_NOT_SUPPORTED_DEVICE = "YCLOUD_NOT_SUPPORTED_DEVICE";
}

