package com.xuzhiyong.comego.module.datacenter.tables;

import android.support.annotation.Nullable;

import com.duowan.fw.kvo.Kvo;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.util.JConstCache;
import com.duowan.fw.util.JFP;
import com.xuzhiyong.comego.module.KvoAnnotationFlags;
import com.xuzhiyong.comego.module.datacenter.DataCenterHelper;
import com.xuzhiyong.comego.module.datacenter.JAppDb;
import com.xuzhiyong.comego.module.datacenter.JDb;
import com.xuzhiyong.comego.module.datacenter.JDbTableController;
import com.xuzhiyong.comego.module.datacenter.JDbTableController.NoCacheTableQueryListener;
import com.xuzhiyong.comego.module.datacenter.JDbTableController.QueryRowsParams;

import java.util.List;

/**
 * Created by hydra on 2015/10/13.
 *
 */
public class JLoginHistoryItem extends Kvo.KvoSource {

	public static final JDbTableController<JLoginHistoryItem> TABLE_CONTROLLER;

	static {
		TABLE_CONTROLLER = new JDbTableController<JLoginHistoryItem>(JLoginHistoryItem.class,
				JAppDb.sCacheId_JLoginHistoryItem) {

			@Override
			public void fromProto(JDb db, JLoginHistoryItem info, Object proto) {
			}

			@Override
			public Object key(Object... cursorKeys) {
				return cursorKeys[0];
			}
		};
	}

	public static JConstCache buildCache() {
		return JConstCache.constCacheWithNameAndControl(
				JLoginHistoryItem.class.getName(), new JConstCache.CacheControl() {

					@Override
					public void refreshValues(Object src, Object dst) {
					}

					@Override
					public Object newObject(Object id) {
						JLoginHistoryItem info = new JLoginHistoryItem();
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

	public static final String Kvo_ts = "ts";
	@KvoAnnotation(name = Kvo_ts, order = 1)
	public long ts;

	public static final String Kvo_cookie = "cookie";
	@KvoAnnotation(name = Kvo_cookie, order = 2)
	public String cookie;

	public static final String Kvo_nick = "nick";
	@KvoAnnotation(name = Kvo_nick, order = 3)
	public String nick;

	public static final String Kvo_sex = "sex";
	@KvoAnnotation(name = Kvo_sex, order = 4)
	public int sex;

	public static final String Kvo_logo = "logo";
	@KvoAnnotation(name = Kvo_logo, order = 5)
	public String logo;

	public static final String Kvo_pushseq = "pushseq";
	@KvoAnnotation(name = Kvo_pushseq, order = 6)
	public int pushseq;

	public static final String Kvo_state = "state";
	@KvoAnnotation(name = Kvo_state, order = 7)
	public int state;

	public static final int State_autologin = 0;
	public static final int State_logout = 1;

	public static void create(JDb db) {
		TABLE_CONTROLLER.create(db);
	}

	public static void save(JLoginHistoryItem histroy) {
		TABLE_CONTROLLER.save(DataCenterHelper.appDb(), histroy);
	}

	public static void delete(long uid) {
		delete(JLoginHistoryItem.info(uid));
	}

	public static void delete(JLoginHistoryItem histroy) {
		TABLE_CONTROLLER.delete(DataCenterHelper.appDb(), histroy);
	}

	public static JLoginHistoryItem info(long uid) {
		return TABLE_CONTROLLER.queryRow(DataCenterHelper.appDb(), uid);
	}

	public static List<JLoginHistoryItem> queryLoginHistories() {
        JDbTableController.QueryRowsParams params = JDbTableController
				.QueryRowsParams.makeParams(10,
						JDbTableController.QueryRowsParams.QueryOrder.Desc,
						"ts");

		return TABLE_CONTROLLER.queryRows(DataCenterHelper.appDb(), params);
	}

	@Nullable
	public static JLoginHistoryItem getLastLoginItem() {
		JDbTableController.QueryRowsParams params = JDbTableController
				.QueryRowsParams.makeParams(1,
						JDbTableController.QueryRowsParams.QueryOrder.Desc,
						"ts");

		List<JLoginHistoryItem> list = TABLE_CONTROLLER.queryRows(DataCenterHelper.appDb(), params);

		return JFP.empty(list) ? null : list.get(0);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1024);
		if (nick != null) {
			sb.append(" (nick): ").append(nick);
		}
		sb.append(" (uid): ").append(uid);
		sb.append(" (state): ").append(state);
		if (cookie != null) {
			sb.append(" (cookie): ").append(cookie);
		}
		return sb.toString();
	}
}
