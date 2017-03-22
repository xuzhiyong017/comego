package com.xuzhiyong.comego.module.datacenter.tables;

import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.kvo.databinding.JObservableInt;
import com.duowan.fw.kvo.databinding.JObservableLong;
import com.duowan.fw.kvo.databinding.JObservableString;
import com.duowan.fw.util.JConstCache;
import com.duowan.fw.util.JLog;
import com.xuzhiyong.comego.model.user.UserInfo;
import com.xuzhiyong.comego.module.KvoAnnotationFlags;
import com.xuzhiyong.comego.module.datacenter.DataCenterHelper;
import com.xuzhiyong.comego.module.datacenter.JAppDb;
import com.xuzhiyong.comego.module.datacenter.JDb;
import com.xuzhiyong.comego.module.datacenter.JDbTableController;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class JUserInfo extends KvoSource {

	public static final JDbTableController<JUserInfo> tableController;

	static {
		tableController = new JDbTableController<JUserInfo>(JUserInfo.class,
				JAppDb.sCacheId_JUserInfo) {

			@Override
			public void fromProto(JDb db, JUserInfo info, Object proto) {
				if (proto instanceof UserInfo) {
					UserInfo uinfo = UserInfo.class.cast(proto);

					if (uinfo.nick != null) {
						info.setValue(Kvo_nick, uinfo.nick);
					}
					if (uinfo.signature != null) {
						info.setValue(Kvo_signature, uinfo.signature);
					}
					if (uinfo.logourl != null) {
						info.setValue(Kvo_logo, uinfo.logourl);
					}

					info.setValue(Kvo_sex, uinfo.sex);

					if (uinfo.birthday != null) {
						info.setValue(Kvo_birthday, uinfo.birthday);
					}
					if (uinfo.location != null) {
						info.setValue(Kvo_location, uinfo.location);
					}
					if (uinfo.address != null) {
						info.setValue(Kvo_address, uinfo.address);
					}
					if (uinfo.createtime != null) {
						info.setValue(Kvo_createtime, uinfo.createtime);
					}

					info.setValue(Kvo_roletype, uinfo.roletype);

					if (uinfo.loginTime != null) {
						info.setValue(Kvo_logintime, uinfo.loginTime);
					}

					info.setValue(Kvo_accounttype, uinfo.accountType);

					info.setValue(Kvo_flags, uinfo.flags);


					if (uinfo.mobilephone != null) {
						info.setValue(Kvo_phonenum, uinfo.mobilephone);
					}

					if(uinfo.extendJson != null) {
						info.setValue(Kvo_extendJson, uinfo.extendJson);

						try {
							JSONObject jsonObject = new JSONObject(uinfo.extendJson);

							info.setValue(Kvo_groupUrl, jsonObject.getString(Kvo_groupUrl));
						} catch (JSONException e) {
							JLog.error(JUserInfo.class, "parse uinfo extendJSon extendJson : "
									+ uinfo.extendJson + " error : ");
						}
					}
				}
			}

			@Override
			public Object key(Object... cursorKeys) {
				return cursorKeys[0];
			}
		};
	}

	public static JConstCache buildCache() {
		return JConstCache.constCacheWithNameAndControl(
				JUserInfo.class.getName(), new JConstCache.CacheControl() {

					@Override
					public void refreshValues(Object src, Object dst) {
					}

					@Override
					public Object newObject(Object id) {
						JUserInfo info = new JUserInfo();
						info.uid = (Long) id;
						return info;
					}

					@Override
					public void cacheKWB() {
					}
				});
	}

	public static final String Kvo_uid = "uid";
	@KvoAnnotation(name = Kvo_uid, flag = KvoAnnotationFlags.DB_PRIMARY_KEY, order = 0)
	public long uid;

	public static final String Kvo_nick = "nickname";
	@KvoAnnotation(name = Kvo_nick, order = 1)
	public final JObservableString nickname = new JObservableString();

	public static final String Kvo_signature = "signature";
	@KvoAnnotation(name = Kvo_signature, order = 2)
	public final JObservableString signature = new JObservableString();

	public static final String Kvo_logo = "logourl";
	@KvoAnnotation(name = Kvo_logo, order = 3)
	public final JObservableString logourl = new JObservableString();

	public static final String Kvo_sex = "sex";
	@KvoAnnotation(name = Kvo_sex, order = 4)
	public final JObservableInt sex = new JObservableInt();

	public static final String Kvo_birthday = "birthday";
	@KvoAnnotation(name = Kvo_birthday, order = 5)
	public final JObservableLong birthday = new JObservableLong();

	public static final String Kvo_location = "location";
	@KvoAnnotation(name = Kvo_location, order = 6)
	public final JObservableString location = new JObservableString();

	public static final String Kvo_address = "address";
	@KvoAnnotation(name = Kvo_address, order = 7)
	public final JObservableString address = new JObservableString();

	public static final String Kvo_createtime = "createtime";
	@KvoAnnotation(name = Kvo_createtime, order = 8)
	public final JObservableLong createtime = new JObservableLong();

	public static final String Kvo_roletype = "roletype";
	@KvoAnnotation(name = Kvo_roletype, order = 9)
	public final JObservableInt roletype = new JObservableInt();

	public static final String Kvo_logintime = "logintime";
	@KvoAnnotation(name = Kvo_logintime, order = 10)
	public final JObservableLong logintime = new JObservableLong();

	public static final String Kvo_accounttype = "accounttype";
	@KvoAnnotation(name = Kvo_accounttype, order = 11)
	public final JObservableInt accounttype = new JObservableInt();

	public static final String Kvo_flags = "flags";
	@KvoAnnotation(name = Kvo_flags, order = 12)
	public final JObservableInt flags = new JObservableInt();

	public static final String Kvo_phonenum = "phonenum";
	@KvoAnnotation(name = Kvo_phonenum, order = 13)
	public final JObservableString phonenum = new JObservableString();

	public static final String Kvo_hometown = "hometown";
	@KvoAnnotation(name = Kvo_hometown, order = 14)
	public final JObservableString hometown = new JObservableString();

	public static final String Kvo_loveVal = "loveVal";
	@KvoAnnotation(name = Kvo_loveVal, order = 15)
	public final JObservableLong loveVal = new JObservableLong();

	public static final String Kvo_follows = "follows";
	@KvoAnnotation(name = Kvo_follows, order = 16)
	public final JObservableInt follows = new JObservableInt();

	public static final String Kvo_fans = "fans";
	@KvoAnnotation(name = Kvo_fans, order = 17)
	public final JObservableInt fans = new JObservableInt();

    public static final String Kvo_mgold = "mgold";
    @KvoAnnotation(name = Kvo_mgold, order = 18)
    public final JObservableLong mgold = new JObservableLong(); // M币

    public static final String Kvo_mbean = "mbean";
    @KvoAnnotation(name = Kvo_mbean, order = 19)
    public final JObservableLong mbean = new JObservableLong(); // M豆

    public static final String Kvo_mchip = "mchip";
    @KvoAnnotation(name = Kvo_mchip, order = 20)
    public final JObservableLong mchip = new JObservableLong(); // 筹码

	public static final String Kvo_level = "level";
	@KvoAnnotation(name = Kvo_level, order = 21)
	public int level;

    public static final String Kvo_exp = "exp";
    @KvoAnnotation(name = Kvo_exp, order = 22)
    public long exp;

    public static final String Kvo_nextExp = "nextExp";
    @KvoAnnotation(name = Kvo_nextExp, order = 23)
    public long nextExp;

	public static final String Kvo_groupUrl = "groupUrl";
	@KvoAnnotation(name = Kvo_groupUrl, order = 24)
	public String groupUrl;

	public static final String Kvo_extendJson = "extendJson";
	@KvoAnnotation(name = Kvo_extendJson , order = 25)
	public String extendJson;

    public static final String Kvo_liveRoomGid = "liveRoomGid";
    @KvoAnnotation(name = Kvo_liveRoomGid, flag = KvoAnnotationFlags.NOT_SAVE_DB)
    public final JObservableLong liveRoomGid = new JObservableLong();

	public static void create(JDb db) {
		tableController.create(db);
	}

	public static JUserInfo info(long uid) {
		return tableController.queryRow(DataCenterHelper.appDb(), uid);
	}

	public static JUserInfo info(UserInfo info) {
		return tableController.queryTarget(DataCenterHelper.appDb(), info, info.uid);
	}

	/**
	 * 用于可能没有uid的UserInfo
	 */
	public static JUserInfo info(long uid, UserInfo info) {
		return tableController.queryTarget(DataCenterHelper.appDb(), info, uid);
	}

    public static List<JUserInfo> infos(List<UserInfo> us) {
		ArrayList<JUserInfo> list = new ArrayList<JUserInfo>();

		for (UserInfo info : us) {
			list.add(JUserInfo.info(info));
		}
		return list;
	}

	public static void save(JUserInfo info) {
		tableController.save(DataCenterHelper.appDb(), info);
	}
}
