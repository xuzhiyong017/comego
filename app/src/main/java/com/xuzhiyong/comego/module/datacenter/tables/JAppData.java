package com.xuzhiyong.comego.module.datacenter.tables;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.duowan.fw.kvo.Kvo.KvoSource;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.util.JStringUtils;
import com.xuzhiyong.comego.module.KvoAnnotationFlags;
import com.xuzhiyong.comego.module.datacenter.DataCenterHelper;
import com.xuzhiyong.comego.module.datacenter.JDb;
import com.xuzhiyong.comego.module.datacenter.JDbTableController;
import com.xuzhiyong.comego.module.datacenter.JDbTableController.QueryRowsParams;
import com.xuzhiyong.comego.module.datacenter.JDbTableController.NoCacheTableQueryListener;


import java.util.List;

/**
 * Created by hydra on 2015/10/22.
 *
 */
public class JAppData extends KvoSource {

	public static final JDbTableController<JAppData> TABLE_CONTROLLER;

	static {
		//无cache
		TABLE_CONTROLLER = new JDbTableController<JAppData>(JAppData.class, 0) {

			@Override
			public void fromProto(JDb db, JAppData info, Object proto) {
			}

			@Override
			public Object key(Object... cursorKeys) {
				return JStringUtils.combineStr(cursorKeys[0], cursorKeys[1]);
			}

			@Override
			public void fromCursor(JDb db, JAppData dst, Cursor cursor) {
				super.fromCursor(db, dst, cursor);

				if(dst.extjson != null) {
					dst.json = DataCenterHelper.gson.fromJson(dst.extjson, JAppDataJson.class);
				}
			}
		};
	}

	// sort = 10001 显示
	public static final int APPDATA_PRE_APP_VERSION = 10001;

	public static class JAppDataJson {
		public String data1;
		public String data2;
		public String data3;
		public String datas[];

		public long ndata1;
		public long ndata2;
		public long ndatas[];
	}

	public static final String Kvo_xid = "xid";
	@KvoAnnotation(name = Kvo_xid, flag = KvoAnnotationFlags.DB_PRIMARY_KEY, order = 0)
	public long xid;

	public static final String Kvo_clazz = "clazz";
	@KvoAnnotation(name = Kvo_clazz, flag = KvoAnnotationFlags.DB_PRIMARY_KEY, order = 1)
	public int clazz;

	public static final String Kvo_version = "version";
	@KvoAnnotation(name = Kvo_version, order = 2)
	public long version;

	public static final String Kvo_nkey = "nkey";
	@KvoAnnotation(name = Kvo_nkey, order = 3)
	public long nkey;

	public static final String Kvo_ntext = "ntext";
	@KvoAnnotation(name = Kvo_ntext, order = 4)
	public String ntext;

	public static final String Kvo_extjson = "extjson";
	@KvoAnnotation(name = Kvo_extjson, order = 5)
	public String extjson;

	public JAppDataJson json;

	public static void create(JDb db) {
		TABLE_CONTROLLER.create(db);
	}

	public static void save(JDb db, JAppData info) {
		TABLE_CONTROLLER.save(db, info);
	}

	public static void deleteClazz(final JDb db, final int clazz) {
		TABLE_CONTROLLER.deleteRows(db, new String[]{"clazz"}, new Object[]{clazz});
	}

	public static void deleteUserData(final JDb db, final int clazz, final long xid) {
		TABLE_CONTROLLER.deleteRows(db, new String[]{"xid", "clazz"}, new Object[]{xid, clazz});
	}

	public static List<JAppData> queryClazz(JDb db, int clazz) {
		QueryRowsParams params = new QueryRowsParams();

		params.keys = new String[]{Kvo_clazz};
		params.values = new String[]{String.valueOf(clazz)};

		return TABLE_CONTROLLER.queryRowsWithoutCache(db, params,
				new NoCacheTableQueryListener<JAppData>() {
					@Override
					public JAppData newObject(Cursor cursor) {
						return new JAppData();
					}
				});
	}

	@Nullable
	public static JAppData queryAppData(JDb db, int clazz, long xid) {
		return TABLE_CONTROLLER.internalSelectWithoutCache(db,
				new NoCacheTableQueryListener<JAppData>() {
					@Override
					public JAppData newObject(Cursor cursor) {
						return new JAppData();
					}
				}, xid, clazz);
	}

	public static void saveClazz_Full(final JDb db, final int clazz, final List<JAppData> datas) {
		deleteClazz(db, clazz);
		TABLE_CONTROLLER.saveRows(db, datas);
	}
}
