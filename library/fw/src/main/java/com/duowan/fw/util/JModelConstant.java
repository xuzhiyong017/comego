package com.duowan.fw.util;

public class JModelConstant {
	
	public static final int DEFAULT_UID = 0;
	public static final int DEFAULT_IMID = 0;
	public static final int DEFAULT_DISTANCE = -1;
	public static final String DEFAULT_BIRTHDAY = "19900101";
	public static final int MAX_AGE = 70;
	public static final int MIN_AGE = 10;
	public static final int MAX_TEXT_COUNT = 50;
	public static final String DEFAULT_UUID = "";
	public static final int INVALID_BROADCAST_MSGID = 0;
	public static final int DEFAULT_BIRTHDAY_YEAY = 1990;
	public static final int DEFAULT_BIRTHDAY_MONTH = 1;
	public static final int DEFAULT_BIRTHDAY_DAY = 1;
	
	public static interface Gender {
		int NONE = -1;
		int FEMALE = 0;
		int MALE = 1;
	}
	
	public static interface MsgType {
	    int DEFAULT_TYPE = -1;
		int TEXT_MESSAGE = 0;
		int IMAGE_MESSAGE = 1;
		int VOICE_MESSAGE = 2;
	}
	
	public static interface IncomingMsgState {
		public static final int STATE_NOT_READY = -1;
		public static final int STATE_UNREAD = 0;
		public static final int STATE_LOCAL_READ = 1; //means user read this message
		public static final int STATE_READ = 2; //means send LocalRead package to proxy server
	}

	public static interface OutGoingMsgState {
		public static final int STATE_NOT_READY = -1;
		public static final int STATE_NOT_SEND = 0;
		public static final int STATE_REACHED_SERVER = 1;
		public static final int STATE_SEND = 2;		
		public static final int STATE_ARRIVAL = 3;
		public static final int STATE_READ = 4;
	}
	
	public static interface AuthState {
		public static final int IS_FRIEND = 0;
		public static final int SENT_REQUEST = 1;
		public static final int STRANGER = 2;
    }

	public static interface ChannelType {
		public static final int TYPE_KTV = 0;
		public static final int TYPE_RADIO = 1;
		public static final int TYPE_GUILD = 2;
		public static final int TYPE_FAVORITE = 3;
		public static final int TYPE_RECENT = 4;
		
		public static final int TYPE_EDU = 5;
		public static final int TYPE_CHAT = 6;//delete
		public static final int TYPE_GAME = 7;
		public static final int TYPE_MC = 8;
		public static final int TYPE_STOCK = 9;
		public static final int TYPE_BASKET = 10;
	}

	public static interface Verify {
		public static final int NOT_VERIFY = 0;
		public static final int VERIFIED= 1;
	}
	
	public static interface BusinessCardInputLimit {
		public static final int BUSI_CARD_INPUT_LIMIT_NAME = 20;
		public static final int BUSI_CARD_INPUT_LIMIT_REGION = 20;
		public static final int BUSI_CARD_INPUT_LIMIT_SCHOOL = 50;
		public static final int BUSI_CARD_INPUT_LIMIT_COMPANY = 50;
		public static final int BUSI_CARD_INPUT_LIMIT_BRIFE = 255;
		public static final int BUSI_CARD_INPUT_LIMIT_MSN = 50;
		public static final int BUSI_CARD_INPUT_LIMIT_ADDRESS = 100;
		public static final int BUSI_CARD_INPUT_LIMIT_MOBILE = 16;
		public static final int BUSI_CARD_INPUT_LIMIT_EMAIL = 30;
	}
	
	public static interface UserStatus {
		public static final int USER_ONLINE = 0;
		public static final int USER_HIDE = 1;
		public static final int USER_OFFLINE = 2;
	}
	
	public static interface  PictureType{
		public static final int SMALL_PICTURE = 0;
		public static final int MIDDLE_PICTURE = 0;
		public static final int BIG_PICTURE = 0;
	}
	
	public static interface SwitchType {
		public static final int SWITCH_OFF = 0;
		public static final int SWITCH_ON = 1;
	}
	
	public static interface PlatformType {
	    public static final int WINDOWS = 1;
	    public static final int ANDROID = 2;
	    public static final int IOS = 3;
	    public static final int SYMBIAN = 4;
	    public static final int UNKNOW_SYSTEM_TYPE = 5;
	}
	
    public static interface ImForumMsgSendAckType {
        public static final int SUCCESS = 0;
        public static final int INVALID_REQ = 1;
        public static final int SERVER_ERROR = 2;
        public static final int BANNED_POST = 3;
        public static final int BANNED_CAUSE_AD = 4;
        public static final int BANNED_CAUSE_UNKNOWN = 5; 
        public static final int RETRY_TOO_MANY_TIMES = 6;
        public static final int BANNED_CAUSE_ADMIN_MODE = 7;
    }
	
	public static interface RecordingCountType {
        public static final int RECORIND_COUNT_BY_STAR = 1;
        public static final int RECORIND_COUNT_BY_PUBLISHER = 2;
        public static final int FANS_COUNT = 3;
        public static final int FOLLOW_COUNT = 4;
        //RECORIND_COUNT_BY_STAR +  RECORIND_COUNT_BY_PUBLISHER = RECORDING_COUNT_BY_OWNER
        public static final int RECORDING_COUNT_BY_OWNER = 5;
    }
	  
	public static interface RecordingListType {
	    public static final int RECORDING_RECENT_LIST = 1;
	    public static final int RECORDING_HOT_LIST = 2;
	    public static final int RECORDING_LIST_BY_STAR = 3;
	    public static final int RECORDING_LIST_BY_PUBLISHER = 4;
	    public static final int FAN_LIST = 5;
        public static final int FOLLOW_LIST = 6;
        public static final int RECORDING_LIST_BY_OWNER = 7;
	}
	
	public static interface RecordingTabType{
	    public static final int FOLLOW = 1;
        public static final int RECENT = 2;
        public static final int HOT = 3;  
        public static final int OWNER = 4;
	}
	
	public static interface RecordingListUpdateTime{
        public static final int RECORDING_RECENT_LIST_TIME = 1;
        public static final int RECORDING_HOT_LIST_TIME = 2;
        public static final int RECORDING_LIST_BY_STAR_TIME = 3;  
        public static final int RECORDING_LIST_BY_PUBLISHER_TIME = 4; 
        public static final int RECORDING_TAG_LIST_TIME = 5;
        public static final int FOLLOW_LIST = 6;
        public static final int FAN_LIST = 7;
        public static final int RECORDING_LIST_BY_OWNER = 8;
        public static final int RECORDING_FOLLOW_LIST = 9;
    }
	
	public static interface RecordingUploadFileState{
	    public static final int UNPUBLISH=0;
	    public static final int QUEUE = 1;
	    public static final int UPLOADING = 2;
	    public static final int UPLOAD_FAIL = 3;
	    public static final int UPLOAD_SUCC = 4;
	    public static final int PUBLISH_SUCC = 5;
	    public static final int PUBLISH_FAIL = 6;
	    public static final int PUBLISHING   = 7;
	}
	
	public static interface SjyyWFrom {
	    public static final int INVALID = 0;
	    public static final int HOT = 1;
	    public static final int RECENT = 2;
	    public static final int FOLLOW = 3;
	    public static final int OWNER = 4;
	    public static final int CONVERSATION = 5;
	    public static final int FRIEND = 6;
	}
	
	public static interface SjyyWShareResult {
	    public static final int SUCCESS = 0;
	    public static final int ABONDON = 1;
	    public static final int FAIL = 2;
	}
	
	public static interface SjyyWShareModify {
	    public static final int SAME = 1;
	    public static final int MODIFY = 2;
	}
	
	public static interface SjyyWCommType {
	    public static final int NEW_COMM = 1;
	    public static final int CLICK_COMM = 2;
	}
	
	public static interface SjyyWeiboType {
	    public static final int INVALID = 0;
	    public static final int SINA = 1;
	    public static final int RENREN = 2;
	    public static final int QQ_WEIBO = 3;
	    public static final int QQ_ZONE = 4;
	}
	
	public static int getSJYYWFromByRecordingTabType(int tabType) {
        switch (tabType) {
        case RecordingTabType.FOLLOW:
            return SjyyWFrom.FOLLOW;
        case RecordingTabType.HOT:
            return SjyyWFrom.HOT;
        case RecordingTabType.OWNER:
            return SjyyWFrom.OWNER;
        case RecordingTabType.RECENT:
            return SjyyWFrom.RECENT;
        }
        return SjyyWFrom.INVALID;
    }
	
	public static interface SjyyWResult {
	    public static final int SUCC = 0;
	    public static final int FAIL = 1;
	}
	
	public static interface SjyyWError {
	    public static final int OTHER = 0;
	    public static final int DOWN_LYRIC = 1;
	    public static final int DOWN_SONG = 2;
	}
	
	public static interface SjyyWSongType {
	    public static final int SILENT = 1;
	    public static final int CHANNEL = 2;
	    public static final int KTV = 3;
	}
	
}
