package com.xuzhiyong.comego.module.datacenter.tables;

import android.database.Cursor;


import com.duowan.fw.kvo.Kvo;
import com.duowan.fw.kvo.KvoAnnotation;
import com.duowan.fw.util.JStringUtils;
import com.xuzhiyong.comego.module.KvoAnnotationFlags;
import com.xuzhiyong.comego.module.analysis.LocalStatics;
import com.xuzhiyong.comego.module.datacenter.JDb;
import com.xuzhiyong.comego.module.datacenter.JDbTableController;
import com.xuzhiyong.comego.module.datacenter.JDbTableController.QueryRowsParams;
import com.xuzhiyong.comego.module.datacenter.JDbTableController.NoCacheTableQueryListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ProtoStatistics extends Kvo.KvoSource {

	public static final JDbTableController<ProtoStatistics> TABLE_CONTROLLER;

	static {
		//没有缓存
		TABLE_CONTROLLER = new JDbTableController<ProtoStatistics>(ProtoStatistics.class, 0) {

			@Override
			public void fromProto(JDb db, ProtoStatistics info, Object proto) {
			}

			@Override
			public Object key(Object... cursorKeys) {
				return cursorKeys[0];
			}
		};
	}

	public static final String Kvo_key = "key";
	@KvoAnnotation(name = Kvo_key, flag = KvoAnnotationFlags.DB_PRIMARY_KEY, order = 0)
	public int key;

	public static final String Kvo_day = "day";
	@KvoAnnotation(name = Kvo_day, order = 1)
	public long day;

	public static final String Kvo_tag = "tag";
	@KvoAnnotation(name = Kvo_tag, order = 2)
	public String tag;

	public static final String Kvo_netway = "netway";
	@KvoAnnotation(name = Kvo_netway, order = 3)
	public int netway;

	public static final String Kvo_opway = "opway";
	@KvoAnnotation(name = Kvo_opway, order = 4)
	public int opway;

	public static final String Kvo_size = "size";
	@KvoAnnotation(name = Kvo_size, order = 5)
	public long size;

	public ProtoStatistics() {

	}

	public ProtoStatistics(long day, String tag, int netway, int opway) {
		this.tag = tag;
		this.opway = opway;
		this.netway = netway;
		this.day = day;
		this.key = key(day, tag, netway, opway).hashCode();
	}

	public String getNetWay() {
		return netway == LocalStatics.NetWay_2G3G ? "2G3G" : "Wifi";
	}

	public String getOpWay() {
		return opway == LocalStatics.OpWay_Read ? "Read" : "Write";
	}

	public String getDay() {
		GregorianCalendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.add(Calendar.DAY_OF_YEAR, -day + day % 365);
		calendar.add(Calendar.YEAR, -year + day / 365);
		Date d = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		return formatter.format(d);
	}

	public static String key(long day, String tag, int netWay, int op) {
		return JStringUtils.combineStr(day, "_", tag, "_", netWay, "_", op);
	}

	public static void create(JDb db) {
		TABLE_CONTROLLER.create(db);
	}

	public static void save(final JDb db, final ProtoStatistics info) {
		TABLE_CONTROLLER.save(db, info);
	}

	public static ProtoStatistics info(JDb db, String key) {
		return TABLE_CONTROLLER.internalSelectWithoutCache(db,
				new NoCacheTableQueryListener<ProtoStatistics>() {
					@Override
					public ProtoStatistics newObject(Cursor cursor) {
						return new ProtoStatistics();
					}
				}, key);
	}

	public static List<ProtoStatistics> queryAll(JDb db) {
		QueryRowsParams qrp = new QueryRowsParams();

		qrp.orderByFields = new String[]{"day", "opway", "tag"};
		qrp.orders = new JDbTableController.QueryRowsParams.QueryOrder
				[]{JDbTableController.QueryRowsParams.QueryOrder.Desc,
				JDbTableController.QueryRowsParams.QueryOrder.Asc,
				JDbTableController.QueryRowsParams.QueryOrder.Asc};

//		Cursor cursor = qrp.getQueryRowsCursor(db, TABLE_CONTROLLER.table);

		return TABLE_CONTROLLER.queryRowsWithoutCache(db, qrp,
				new NoCacheTableQueryListener<ProtoStatistics>() {
					@Override
					public ProtoStatistics newObject(Cursor cursor) {
						return new ProtoStatistics();
					}
				});
	}
}